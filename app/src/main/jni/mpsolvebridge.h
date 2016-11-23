#include <jni.h>

/* Header for class it_unipi_dm_mpsolve_android_MainActivity */

#ifndef _Included_it_unipi_dm_mpsolve_android_MainActivity
#define _Included_it_unipi_dm_mpsolve_android_MainActivity
#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT jobjectArray JNICALL Java_it_unipi_dm_mpsolve_android_PolynomialSolver_nativeSolvePolynomial
  (JNIEnv *, jobject, jstring);

JNIEXPORT jobjectArray JNICALL Java_it_unipi_dm_mpsolve_android_PolynomialSolver_nativeSolvePolynomialFile
  (JNIEnv *, jobject, jstring);

#define LOG2_10 3.32192809488736
#define MIN(x, y) ((x) < (y)) ? (x) : (y)

#ifdef __cplusplus
}
#endif
#endif
