MPSolveForAndroid
=================

This is a stub of the Android application to use MPSolve (see http://mpsolve.dm.unipi.it/mpsolve). 
The application is working and can be built using the original sources of MPSolve with the following
steps: 

1. Download MPSolve from http://numpi.dm.unipi.it/mpsolve/. Unpack it in the same folder where
   the sources of MPSolveForAndroid are stored. 

2. Use the script in tools/android-build-libmps.sh. Before launching set ```ANDROID_NDK_ROOT```
   to the appropriate location. Build MPSolve for all the known architecture that you want in Android (by  
   defaults these are arm, x86 and mips, along with their 64 bit versions). 

3. If you do not have an updated it version of gradle, download it from https://gradle.org/. 

4. Configure the location of the Android Sdk and NDK. You can do this by setting the appropriate
   environment variables (```ANDROID_SDK_HOME``` and ```ANDROID_NDK_HOME```) or by creating a file
  ````local.properties```  as report below. 
5. Build the project using ```gradle```, by issuing the command ```gradle build```.


Here is an example ```local.properties``` files. 
```
# local.properties
ndk.dir=/path/to/Android/Sdk/ndk-bundle
sdk.dir=/path/to/Android/Sdk
```

    
## Moving MPSolve

If you want to move the directory where MPSolve is compiled in a different location you will have to update the paths in ```app/src/main/jni/Android.mk``` to match the new setup. At the moment, they are hardcoded to work with a directory structure where MPSolve and MPSolveForAndroid are at the same level. 
