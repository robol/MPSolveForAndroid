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

   Note that the folder for MPSolve is called "mpsolve" with lower case characters. 
     
You can finally build the app using Eclipse or ant or whatever you want. The instructions below will 
reproduce the steps above and compile the APK using android SDK and NDK (and assuming the relevant
folders are in your PATH): 

```shell
$ git clone git://github.com/robol/MPSolve mpsolve
$ ( cd mpsolve && ./autogen.sh && ./tools/android-build-libmps.sh )
   [ Select option 4. Build for all architectures ]
$ git clone git://github.com/robol/MPSolveForAndroid 
$ cd MPSolveForAndroid
$ android update -project -p . -t n 
  [ n is the number of the target that you want. You can find available targets
    with android list targets ]. 
$ ndk-build all 
$ cp /path/to/android/sdk/extras/support/v4/android-support-v4.jar ./libs/
$ ant debug
```
