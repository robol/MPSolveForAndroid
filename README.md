MPSolveForAndroid
=================

This is a stub of the Android application to use MPSolve (see http://mpsolve.dm.unipi.it/mpsolve). 
The application is working and can be built using the original sources of MPSolve with the following
steps: 

1. Download MPSolve from http://mpsolve.dm.unipi.it/mpsolve. At the moment being, you will need to 
   checkout the git repository since the changes to make MPSolve work flawlessly with Android are not 
   upstream, yet. 
   
2. Use the script in tools/android-build-libmps.sh. Before launching set ANDROID_NDK_ROOT to the appropriate
   location. Build MPSolve for all the known architecture that you want in Android (by defaults these are
   arm, x86 and mips). 
   
3. Build the JNI library with ndk-build keeping the sources of MPSolveForAndroid and mpsolve at the same level.
   An example of directories layout is this: 
   
   - MPSolveAndroidBuild/
     - mpsolve/
       + android-ext-arm-.../
       + android-ext-x86-.../
     + MPSolveForAndroid/
     
4. Build the app using Eclipse or ant or whatever you want. 
