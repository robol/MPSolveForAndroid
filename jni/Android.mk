LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES = ../../mpsolve/android-ext-arm-linux-androideabi-4.8/lib/libmps.a
LOCAL_MODULE := libmps

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_SRC_FILES = ../../mpsolve/android-ext-arm-linux-androideabi-4.8/lib/libgmp.a
LOCAL_MODULE := libgmp

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES = ../mpsolve/android-ext-arm-linux-androideabi-4.8/include
LOCAL_MODULE    := libmpsolvebridge
LOCAL_SRC_FILES := mpsolvebridge.cpp
# LOCAL_LDLIBS += ../mpsolve/android-ext-arm-linux-androideabi-4.8/lib/libmps.a
# LOCAL_LDLIBS += ../mpsolve/android-ext-arm-linux-androideabi-4.8/lib/libgmp.a
LOCAL_LDLIBS += -llog
LOCAL_STATIC_LIBRARIES := libmps libgmp

include $(BUILD_SHARED_LIBRARY)
