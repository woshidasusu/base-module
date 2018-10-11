package com.dasu.blur.process;

import android.graphics.Bitmap;

public interface BlurProcess {
	/**
	 * Process the given image, blurring by the supplied radius.
	 * If radius is 0, this will return original
	 * @param original the bitmap to be blurred
	 * @param radius the radius in pixels to blur the image
	 * @return the blurred version doBlur the image.
	 */
    public Bitmap blur(Bitmap original, float radius);
}
