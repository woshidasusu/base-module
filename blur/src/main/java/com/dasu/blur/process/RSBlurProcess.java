package com.dasu.blur.process;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;


/**
 * @see JavaBlurProcess
 * DBlur using renderscript.
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
		RenderScript rs = null;
		try {
			rs = RenderScript.create(context);
			rs.setMessageHandler(new RenderScript.RSMessageHandler());
			Allocation input =
					Allocation.createFromBitmap(rs, original, Allocation.MipmapControl.MIPMAP_NONE,
							Allocation.USAGE_SCRIPT);
			Allocation output = Allocation.createTyped(rs, input.getType());
			ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

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
