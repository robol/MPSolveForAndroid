LOCAL_PATH := $(call my-dir)

ifeq ($(TARGET_ARCH),x86)
MPSOLVE_ARCH := x86-4.8
else 
ifeq ($(TARGET_ARCH),mips)
MPSOLVE_ARCH := mipsel-linux-android-4.8
else
MPSOLVE_ARCH := arm-linux-androideabi-4.8
endif
endif

include $(CLEAR_VARS)

LOCAL_SRC_FILES = ../../mpsolve/android-ext-$(MPSOLVE_ARCH)/lib/libmps.a
LOCAL_MODULE := libmps

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_SRC_FILES = ../../mpsolve/android-ext-$(MPSOLVE_ARCH)/lib/libgmp.a
LOCAL_MODULE := libgmp

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES = ../mpsolve/android-ext-$(MPSOLVE_ARCH)/include
LOCAL_MODULE    := libmpsolvebridge
LOCAL_SRC_FILES := mpsolvebridge.cpp
LOCAL_LDLIBS += -llog
LOCAL_STATIC_LIBRARIES := libmps libgmp

include $(BUILD_SHARED_LIBRARY)
