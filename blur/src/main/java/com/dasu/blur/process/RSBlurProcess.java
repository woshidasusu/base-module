package com.dasu.blur.process;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * @see JavaBlurProcess
 * DBlur using renderscript.
 * api >= 18 可使用
 */
public class RSBlurProcess implements BlurProcess {
	public static final String TAG = "RSBlurProcess";

	private final Context context;

	public RSBlurProcess(Context context) {
		this.context = context.getApplicationContext();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public Bitmap blur(Bitmap original, float radius) {
		android.renderscript.RenderScript rs = null;
		try {
			rs = android.renderscript.RenderScript.create(context);
			rs.setMessageHandler(new android.renderscript.RenderScript.RSMessageHandler());
			android.renderscript.Allocation input =
					android.renderscript.Allocation.createFromBitmap(rs, original, android.renderscript.Allocation.MipmapControl.MIPMAP_NONE,
							android.renderscript.Allocation.USAGE_SCRIPT);
			android.renderscript.Allocation output = android.renderscript.Allocation.createTyped(rs, input.getType());
			ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, android.renderscript.Element.U8_4(rs));

			blur.setInput(input);
			blur.setRadius(radius);
			blur.forEach(output);
			output.copyTo(original);
		} finally {
			if (rs != null) {
				rs.destroy();
			}
		}

		return original;
	}

	@Override
	public String toString() {
		return TAG;
	}
}
