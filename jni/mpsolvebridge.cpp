#include "mpsolvebridge.h"
#include <android/log.h>
#include <mps/mps.h>

jobjectArray Java_it_unipi_dm_mpsolve_android_PolynomialSolver_nativeSolvePolynomial
(JNIEnv * env, jobject javaThis, jstring polynomial)
{
	const char * poly_string = env->GetStringUTFChars(polynomial, NULL);

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

	mps_context * ctx = mps_context_new ();
	mps_polynomial *poly = mps_parse_inline_poly_from_string (ctx, poly_string);

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
		mps_context_set_output_prec(ctx, digits * log(10) / log(2));

		mps_context_add_debug_domain (ctx, MPS_DEBUG_TRACE);

		mps_mpsolve (ctx);
		approximations = mps_context_get_approximations (ctx);

		for (int i = 0; i < mps_context_get_degree (ctx) + mps_context_get_zero_roots (ctx); i++)
		{
			char* output = (char*) malloc (2 * digits + 25);

			cplx_t fvalue;
			mpc_t  mvalue;
			rdpe_t drad;

			mpc_init2 (mvalue, digits * 3);

			mps_approximation_get_fvalue (ctx, approximations[i], fvalue);
			mps_approximation_get_mvalue (ctx, approximations[i], mvalue);
			mps_approximation_get_drad   (ctx, approximations[i], drad);

			double realPartF = cplx_Re(fvalue);
			double imagPartF = cplx_Im(fvalue);

			jobject approximationObject = env->NewObject(approximationClass,
					approximationConstructor);

			// Create the String representation of the approximation

			if (realPartF >= 0)
			   gmp_sprintf (output, "%.*Fe + %.*Fei", digits, mpc_Re (mvalue),
			                digits, mpc_Im (mvalue));
			else
			   gmp_sprintf (output, "%.*Fe %.*Fei", digits, mpc_Re (mvalue),
			                digits, mpc_Im (mvalue));

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
		mps_monomial_poly_free (ctx, MPS_POLYNOMIAL (poly));
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "MPSolve",
				"Cannot parse user polynomial: %s", poly_string);
	}

	mps_context_free (ctx);

	env->ReleaseStringUTFChars(polynomial, poly_string);

	return array;
}
