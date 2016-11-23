LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES = $(LOCAL_PATH)/../../../../../MPSolve/android-ext-$(TARGET_ARCH)/lib/libmps.so
LOCAL_MODULE := libmps

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_SRC_FILES = $(LOCAL_PATH)/../../../../../MPSolve/android-ext-$(TARGET_ARCH)/lib/libgmp.so
LOCAL_MODULE := libgmp

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_SRC_FILES = $(LOCAL_PATH)/../../../../../MPSolve/android-ext-$(TARGET_ARCH)/lib/libgmpxx.so
LOCAL_MODULE := libgmpxx

include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES = $(LOCAL_PATH)/../../../../../MPSolve/android-ext-$(TARGET_ARCH)/include
LOCAL_MODULE    := libmpsolvebridge
LOCAL_SRC_FILES := mpsolvebridge.cpp
LOCAL_LDLIBS += -llog
LOCAL_SHARED_LIBRARIES := libmps libgmp

include $(BUILD_SHARED_LIBRARY)
