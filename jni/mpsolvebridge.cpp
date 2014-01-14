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

	mpc_t *roots = NULL;
	rdpe_t *radius = NULL;

	mps_context * ctx = mps_context_new ();
	mps_polynomial *poly = mps_parse_inline_poly_from_string (ctx, poly_string);

	jobjectArray array = env->NewObjectArray(poly ? 2 * poly->degree : 0,
				env->FindClass("java/lang/String"),
				NULL);

	if (poly)
	{
		mps_context_set_input_poly (ctx, poly);

		mps_context_select_algorithm(ctx, alg);
		mps_context_set_output_prec(ctx, digits * log(10) / log(2));

		mps_mpsolve (ctx);

		mps_context_get_roots_m (ctx, &roots, &radius);

		for (int i = 0; i < mps_context_get_degree (ctx); i++)
		{
			char* output = (char*) malloc (2 * digits + 25);

			if (mpf_sgn (mpc_Im (roots[i])) > 0)
			   gmp_sprintf (output, "%.*Fe + %.*Fei", digits, mpc_Re (roots[i]),
			                digits, mpc_Im (roots[i]));
			else
			   gmp_sprintf (output, "%.*Fe %.*Fei", digits, mpc_Re (roots[i]),
			                digits, mpc_Im (roots[i]));

			env->SetObjectArrayElement(array, 2*i, env->NewStringUTF(output));

			rdpe_get_str (output, radius[i]);
			env->SetObjectArrayElement(array, 2*i + 1, env->NewStringUTF(output));

			free (output);
		}

		mpc_vclear (roots, mps_context_get_degree (ctx));
		free (roots);
		free (radius);
		mps_monomial_poly_free (ctx, MPS_POLYNOMIAL (poly));
	}
	else
	{
		__android_log_print(ANDROID_LOG_DEBUG, "it.dm.unipi.mpsolve",
				"Cannot parse user polynomial: %s", poly_string);
	}
	mps_context_free (ctx);

	env->ReleaseStringUTFChars(polynomial, poly_string);

	return array;
}
