#include "mpsolvebridge.h"
#include <android/log.h>
#include <mps/mps.h>



static
jobjectArray solvePolynomial (JNIEnv * env, jobject javaThis, mps_context * ctx,
		mps_polynomial * poly)
{
	/* Parse options */
	mps_algorithm alg = env->GetCharField(javaThis,
			env->GetFieldID(
					env->FindClass("it/unipi/dm/mpsolve/android/PolynomialSolver"),
					"algorithm", "C")) == 's' ?
					MPS_ALGORITHM_SECULAR_GA :
					MPS_ALGORITHM_STANDARD_MPSOLVE;

	int digits = env->GetIntField(javaThis,
			env->GetFieldID(
					env->FindClass("it/unipi/dm/mpsolve/android/PolynomialSolver"),
					"digits", "I"));

	char c_goal = env->GetCharField(javaThis,
			env->GetFieldID(
					env->FindClass("it/unipi/dm/mpsolve/android/PolynomialSolver"),
							"goal", "C"));

	mps_output_goal goal = MPS_OUTPUT_GOAL_APPROXIMATE;

	switch (c_goal) {
	case 'i':
		goal = MPS_OUTPUT_GOAL_ISOLATE;
		break;
	default:
		__android_log_print(ANDROID_LOG_DEBUG, "MPSolve", "Unhandled goal options : %c",
				c_goal);
				/* no break */
	case 'a':
		goal = MPS_OUTPUT_GOAL_APPROXIMATE;
		break;
	}

	jclass approximationClass = env->FindClass(
			"it/unipi/dm/mpsolve/android/Approximation");

	jmethodID approximationConstructor = env->GetMethodID(approximationClass,
			"<init>", "()V");

	jobjectArray array = env->NewObjectArray(poly ? poly->degree : 0,
				approximationClass,
				NULL);

	if (poly)
	{
		mps_approximation **approximations = NULL;
		mps_context_set_input_poly (ctx, poly);

		mps_context_select_algorithm(ctx, alg);
		mps_context_set_output_goal(ctx, goal);
		mps_context_set_output_prec(ctx, digits * LOG2_10);

		mps_context_add_debug_domain (ctx, MPS_DEBUG_TRACE);

		mps_mpsolve (ctx);
		approximations = mps_context_get_approximations (ctx);

		for (int i = 0; i < mps_context_get_degree (ctx) + mps_context_get_zero_roots (ctx); i++)
		{
			char* output = (char*) malloc (2 * digits + 25);

			cplx_t fvalue;
			mpc_t  mvalue;
			rdpe_t drad;
			double frad = mps_approximation_get_frad (ctx, approximations[i]);

			// Prepare a multiprecision value with a sufficient number of digits
			mpc_init2 (mvalue, digits * 3);

			mps_approximation_get_fvalue (ctx, approximations[i], fvalue);
			mps_approximation_get_mvalue (ctx, approximations[i], mvalue);
			mps_approximation_get_drad   (ctx, approximations[i], drad);

			double realPartF = cplx_Re(fvalue);
			double imagPartF = cplx_Im(fvalue);

			jobject approximationObject = env->NewObject(approximationClass,
					approximationConstructor);

			int neededRealDigits = MIN (digits, log10 (fabs(realPartF)) - log10 (frad));
			int neededImagDigits = MIN (digits, log10 (fabs(imagPartF)) - log10 (frad));

			if (neededRealDigits <= 0) {
				neededRealDigits = 1;
				mpf_set_ui (mpc_Re (mvalue), 0U);
			}

			if (neededImagDigits <= 0) {
				neededImagDigits = 1;
				mpf_set_ui (mpc_Im (mvalue), 0U);
			}

			// Here we perform a basic filtering on the output. Real and imaginary
			// parts should be truncated according to the radius computed.
			if (imagPartF >= 0) {
			    gmp_sprintf (output, "%.*Fe + %.*Fei",
			    		    neededRealDigits,
			    		    mpc_Re (mvalue),
			                neededImagDigits, mpc_Im (mvalue));
			}
			else {
				mpf_t flippedValue;
				mpf_init2 (flippedValue, mpc_get_prec (mvalue));
				mpf_mul_si (flippedValue, mpc_Im (mvalue), -1);
			    gmp_sprintf (output, "%.*Fe - %.*Fei",
			    		    neededRealDigits, mpc_Re (mvalue),
			                neededImagDigits, flippedValue);
			    mpf_clear (flippedValue);
			}

			jobject valueRepresentation = env->NewStringUTF(output);
			env->SetObjectField(approximationObject,
					env->GetFieldID(approximationClass,
							"valueRepresentation",
							"Ljava/lang/String;"),
					valueRepresentation);
			env->DeleteLocalRef(valueRepresentation);

			// Get the String representation of the radius

			rdpe_get_str (output, drad);

			jobject radiusRepresentation = env->NewStringUTF(output);
			env->SetObjectField(approximationObject,
								env->GetFieldID(approximationClass,
										"radiusRepresentation",
										"Ljava/lang/String;"),
								radiusRepresentation);
			env->DeleteLocalRef(radiusRepresentation);

			// Store other floating point values
			env->SetDoubleField(approximationObject,
					env->GetFieldID(approximationClass, "radius", "D"),
							mps_approximation_get_frad(ctx, approximations[i]));
			env->SetDoubleField(approximationObject,
					env->GetFieldID(approximationClass, "realValue", "D"),
						realPartF);
			env->SetDoubleField(approximationObject,
					env->GetFieldID(approximationClass, "imagValue", "D"),
						imagPartF);

			// Store the Status of the Approximation
			jobject status = env->NewStringUTF(
					MPS_ROOT_STATUS_TO_STRING (mps_approximation_get_status (ctx, approximations[i])));
			env->SetObjectField(approximationObject,
					env->GetFieldID(approximationClass, "status", "Ljava/lang/String;"),
					status);

			env->SetObjectArrayElement(array, i, approximationObject);
			env->DeleteLocalRef(status);

			mpc_clear (mvalue);
			free (output);
			mps_approximation_free (ctx, approximations[i]);

			env->DeleteLocalRef(approximationObject);
		}

		free (approximations);
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "MPSolve",
				"Cannot parse user polynomial");
	}

	return array;
}

jobjectArray Java_it_unipi_dm_mpsolve_android_PolynomialSolver_nativeSolvePolynomial
(JNIEnv * env, jobject javaThis, jstring polynomial)
{
	jobjectArray r = NULL;
	const char * poly_string = env->GetStringUTFChars(polynomial, NULL);

	mps_context * ctx = mps_context_new ();
	mps_polynomial *poly = mps_parse_inline_poly_from_string (ctx, poly_string);

	// solvePolynomial handles the case poly == NULL in a graceful way.
	r = solvePolynomial(env, javaThis, ctx, poly);

	env->ReleaseStringUTFChars(polynomial, poly_string);

	if (poly != NULL)
		mps_monomial_poly_free(ctx, poly);
	mps_context_free(ctx);

	return r;
}

jobjectArray Java_it_unipi_dm_mpsolve_android_PolynomialSolver_nativeSolvePolynomialFile
(JNIEnv * env, jobject javaThis, jstring path)
{
	mps_context * ctx = mps_context_new ();
	const char * c_path = env->GetStringUTFChars(path, NULL);
	mps_polynomial *poly = mps_parse_file (ctx, c_path);
	jobjectArray r;

	r = solvePolynomial(env, javaThis, ctx, poly);

	if (poly) {
		mps_polynomial_free (ctx, poly);
	}

	mps_context_free (ctx);
	env->ReleaseStringUTFChars(path, c_path);
	return r;

}

