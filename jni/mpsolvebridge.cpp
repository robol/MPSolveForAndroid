#include "mpsolvebridge.h"
#include <mps/mps.h>

jobjectArray Java_it_unipi_dm_mpsolve_android_PolynomialSolver_nativeSolvePolynomial
(JNIEnv * env, jobject javaThis, jstring polynomial)
{
	jobjectArray array = env->NewObjectArray(4,
			env->FindClass("java/lang/String"),
			NULL);

	cplx_t *roots = NULL;

	// TODO: Insert the actual resolution here
	mps_context * ctx = mps_context_new ();

	mps_monomial_poly *poly = mps_monomial_poly_new (ctx, 4);

	mps_monomial_poly_set_coefficient_int (ctx, poly, 0, -1, 0);
	mps_monomial_poly_set_coefficient_int (ctx, poly, 4,  1, 0);

	mps_context_set_input_poly (ctx, MPS_POLYNOMIAL (poly));
	mps_mpsolve (ctx);

	mps_context_get_roots_d (ctx, &roots, NULL);

	for (int i = 0; i < 4; i++)
	{
		char output[1023];
		cplx_get_str (output, roots[i]);
		env->SetObjectArrayElement(array, i, env->NewStringUTF(output));
	}

	free (roots);
	mps_monomial_poly_free (ctx, MPS_POLYNOMIAL (poly));
	mps_context_free (ctx);

	return array;
}
