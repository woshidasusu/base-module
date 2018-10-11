package com.dasu.blur.process;

import android.graphics.Bitmap;


/**
 * @see JavaBlurProcess
 * DBlur using the NDK and native code.
 *
 * 记得需要同步修改 blur.c 中 functionToBlur 的包名路径，再重新 ndk-build
 */
public class NativeBlurProcess implements BlurProcess {
	public static final String TAG = "NativeBlurProcess";

	static {
		System.loadLibrary("blur");
	}

	private static native void functionToBlur(Bitmap bitmapOut, int radius, int threadCount);

	@Override
	public Bitmap blur(Bitmap original, float radius) {
		Bitmap bitmapOut = original.copy(Bitmap.Config.ARGB_8888, true);

		int cores = Runtime.getRuntime().availableProcessors();
		functionToBlur(bitmapOut, (int)radius, cores);

		return bitmapOut;
	}

	@Override
	public String toString() {
		return TAG;
	}
}
