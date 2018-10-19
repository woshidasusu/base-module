package com.dasu.fresco;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * Created by dasu on 2018/10/19.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * 负责引导使用
 */
public class FrescoController {

    public static class LoadStep {
        protected Uri mUri;
        protected ImageConfig mImageConfig;
        protected boolean hasEnteredImageConfig = false;
        protected int mWidth;
        protected int mHeight;

        public LoadStep(File file) {
            mUri = Uri.parse("file://" + file.getAbsolutePath());
        }

        public LoadStep(Context context, int resId) {
            mUri = Uri.parse("res://" + context.getPackageName() + "/" + resId);
        }

        public LoadStep(String url) {
            mUri = Uri.parse(url);
        }

        public LoadStep(Uri uri) {
            mUri = uri;
        }

        void setImageConfig(ImageConfig imageConfig) {
            mImageConfig = imageConfig;
        }

        /**
         * 减少图片分辨率，降低内存占用
         * @param width >0
         * @param height >0
         * @return
         */
        public LoadStep resize(int width, int height) {
            mWidth = width;
            mHeight = height;
            return this;
        }

        /**
         * 图片显示的各种配置，如：缩放，占位图等等，需结合 {@link #intoTarget(SimpleDraweeView)}
         * 或 {@link #intoTarget(SimpleDraweeView, ControllerListener)} 使用，否则配置无效
         *
         * 你也可以直接对 SimpleDraweeView 的 Hierarchy 操作
         * @return
         */
        public ImageConfigStep enterImageConfig() {
            if (hasEnteredImageConfig) {
                throw new UnsupportedOperationException("u can't enter imageConfig repeat, only set once enough.");
            }
            hasEnteredImageConfig = true;
            return new ImageConfigStep(this);
        }

        /**
         * 加载图片到 View 上
         * @param draweeView
         */
        public void intoTarget(SimpleDraweeView draweeView) {
            intoTarget(draweeView, null);
        }

        /**
         * 加载图片到 View 上，可设置回调监听
         * @param draweeView
         * @param listener
         */
        public void intoTarget(SimpleDraweeView draweeView, ControllerListener listener) {
            if ((mImageConfig != null && mImageConfig.useNewHierarchy) || !draweeView.hasHierarchy()) {
                GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder
                        .newInstance(draweeView.getResources()).build();
                draweeView.setHierarchy(hierarchy);
            }
            parseImageConfig(draweeView.getHierarchy());
            if (mUri != null) {
                ImageDecodeOptions imageDecodeOptions = ImageDecodeOptions.newBuilder()
                        .setDecodePreviewFrame(true)
                        .build();
                ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(mUri)
                        .setProgressiveRenderingEnabled(true)
                        .setImageDecodeOptions(imageDecodeOptions);
                if (mWidth > 0 && mHeight > 0) {
                    builder.setResizeOptions(new ResizeOptions(mWidth, mHeight));
                } else {
                    int width = draweeView.getMeasuredWidth();
                    int height = draweeView.getMeasuredHeight();
                    if(width > 0 && height > 0){
                        builder.setResizeOptions(new ResizeOptions(width, height));
                    }
                }

                ImageRequest request = builder.build();
                AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setControllerListener(listener)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true)
                        .build();
                draweeView.setController(controller);
            } else {
                draweeView.setImageURI("");
            }
        }

        /**
         * 加载图片获取 Bitmap 对象
         * @param subscriber
         */
        public void intoTarget(BaseBitmapDataSubscriber subscriber) {
            ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(mUri)
                    .setProgressiveRenderingEnabled(true);

            if (mWidth > 0 && mHeight > 0) {
                builder.setResizeOptions(new ResizeOptions(mWidth, mHeight));
            }

            ImageRequest imageRequest = builder.build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
            dataSource.subscribe(subscriber, UiThreadImmediateExecutorService.getInstance());
        }

        private void parseImageConfig(GenericDraweeHierarchy hierarchy) {
            if (mImageConfig == null) {
                return;
            }
            if (mImageConfig.asCircle) {
                RoundingParams roundingParams = hierarchy.getRoundingParams();
                if (roundingParams == null) {
                    roundingParams = new RoundingParams();
                }
                roundingParams.setRoundAsCircle(true);
                if (mImageConfig.roundBorderColor != -1) {
                    roundingParams.setBorderColor(mImageConfig.roundBorderColor);
                }
                if (mImageConfig.roundBorderWidth != -1) {
                    roundingParams.setBorderWidth(mImageConfig.roundBorderWidth);
                }
                hierarchy.setRoundingParams(roundingParams);
            } else if (mImageConfig.asRound) {
                RoundingParams roundingParams = hierarchy.getRoundingParams();
                if (roundingParams == null) {
                    roundingParams = new RoundingParams();
                }
                roundingParams.setCornersRadius(mImageConfig.cornerRadius);
                if (mImageConfig.roundBorderColor != -1) {
                    roundingParams.setBorderColor(mImageConfig.roundBorderColor);
                }
                if (mImageConfig.roundBorderWidth != -1) {
                    roundingParams.setBorderWidth(mImageConfig.roundBorderWidth);
                }
                hierarchy.setRoundingParams(roundingParams);
            }

            if (mImageConfig.placeholderDrawable != null) {
                if (mImageConfig.placeholderScaleType != null) {
                    hierarchy.setPlaceholderImage(mImageConfig.placeholderDrawable, mImageConfig.placeholderScaleType);
                } else {
                    hierarchy.setPlaceholderImage(mImageConfig.placeholderDrawable);
                }
            } else if (mImageConfig.placeholderResId != -1) {
                if (mImageConfig.placeholderScaleType != null) {
                    hierarchy.setPlaceholderImage(mImageConfig.placeholderResId, mImageConfig.placeholderScaleType);
                } else {
                    hierarchy.setPlaceholderImage(mImageConfig.placeholderResId);
                }
            }

            if (mImageConfig.retryDrawable != null) {
                if (mImageConfig.retryScaleType != null) {
                    hierarchy.setRetryImage(mImageConfig.retryDrawable, mImageConfig.retryScaleType);
                } else {
                    hierarchy.setRetryImage(mImageConfig.retryDrawable);
                }
            } else if (mImageConfig.retryResId != -1) {
                if (mImageConfig.retryScaleType != null) {
                    hierarchy.setRetryImage(mImageConfig.retryResId, mImageConfig.retryScaleType);
                } else {
                    hierarchy.setRetryImage(mImageConfig.retryResId);
                }
            }

            if (mImageConfig.failureDrawable != null) {
                if (mImageConfig.failureScaleType != null) {
                    hierarchy.setFailureImage(mImageConfig.failureDrawable, mImageConfig.failureScaleType);
                } else {
                    hierarchy.setFailureImage(mImageConfig.failureDrawable);
                }
            } else if (mImageConfig.failureResId != -1) {
                if (mImageConfig.failureScaleType != null) {
                    hierarchy.setFailureImage(mImageConfig.failureResId, mImageConfig.failureScaleType);
                } else {
                    hierarchy.setFailureImage(mImageConfig.failureResId);
                }
            }

            if (mImageConfig.progressBarDrawable != null) {
                if (mImageConfig.progressBarScaleType != null) {
                    hierarchy.setProgressBarImage(mImageConfig.progressBarDrawable, mImageConfig.progressBarScaleType);
                } else {
                    hierarchy.setProgressBarImage(mImageConfig.progressBarDrawable);
                }
            } else if (mImageConfig.progressBarResId != -1) {
                if (mImageConfig.progressBarScaleType != null) {
                    hierarchy.setProgressBarImage(mImageConfig.progressBarResId, mImageConfig.progressBarScaleType);
                } else {
                    hierarchy.setProgressBarImage(mImageConfig.progressBarResId);
                }
            }

            if (mImageConfig.actualScaleType != null) {
                hierarchy.setActualImageScaleType(mImageConfig.actualScaleType);
            }

            if (mImageConfig.fadeDuration != -1) {
                hierarchy.setFadeDuration(mImageConfig.fadeDuration);
            }
        }
    }

    public static class DownloadStep extends LoadStep {

        public DownloadStep(File file) {
            super(file);
        }

        public DownloadStep(Context context, int resId) {
            super(context, resId);
        }

        public DownloadStep(String url) {
            super(url);
        }

        public DownloadStep(Uri uri) {
            super(uri);
        }

        /**
         * 只下载图片到磁盘
         * @param context
         */
        public void downloadOnly(Context context) {
            downloadOnly(context, null);
        }

        /**
         * 只下载图片到磁盘，可设置下载回调
         * @param context
         * @param baseDataSubscriber
         */
        public void downloadOnly(Context context, BaseDataSubscriber baseDataSubscriber) {
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(mUri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();

            DataSource<Void> dataSource = imagePipeline.prefetchToDiskCache(imageRequest, context.getApplicationContext());
            if(baseDataSubscriber != null) {
                dataSource.subscribe(baseDataSubscriber, UiThreadImmediateExecutorService.getInstance());
            }
        }

        @Override
        public void intoTarget(SimpleDraweeView draweeView) {
            super.intoTarget(draweeView);
        }

        @Override
        public void intoTarget(SimpleDraweeView draweeView, ControllerListener listener) {
            super.intoTarget(draweeView, listener);
        }

        @Override
        public void intoTarget(BaseBitmapDataSubscriber subscriber) {
            super.intoTarget(subscriber);
        }

        @Override
        public ImageConfigStep enterImageConfig() {
            return super.enterImageConfig();
        }

        @Override
        public LoadStep resize(int width, int height) {
            return super.resize(width, height);
        }
    }

    public static class ImageConfigStep {
        private LoadStep mLoadStep;
        private ImageConfig mImageConfig;

        public ImageConfigStep(LoadStep loadStep) {
            mLoadStep = loadStep;
            mImageConfig = new ImageConfig();
        }

        /**
         * 完成配置，继续下一步引导
         * @return
         */
        public LoadStep finishImageConfig() {
            mLoadStep.setImageConfig(mImageConfig);
            return mLoadStep;
        }

        public ImageConfigStep animFade(int duration) {
            mImageConfig.fadeDuration = duration;
            return this;
        }

        public ImageConfigStep allScaleType(ScalingUtils.ScaleType scaleType) {
            mImageConfig.actualScaleType = scaleType;
            mImageConfig.placeholderScaleType = scaleType;
            mImageConfig.retryScaleType = scaleType;
            mImageConfig.failureScaleType = scaleType;
            mImageConfig.progressBarScaleType = scaleType;
            return this;
        }

        public ImageConfigStep allCenterCrop() {
            return allScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        }

        public ImageConfigStep allFitXY() {
            return allScaleType(ScalingUtils.ScaleType.FIT_XY);
        }

        public ImageConfigStep allFitCenter() {
            return allScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        }

        public ImageConfigStep allFitStart() {
            return allScaleType(ScalingUtils.ScaleType.FIT_START);
        }

        public ImageConfigStep allFitEnd() {
            return allScaleType(ScalingUtils.ScaleType.FIT_END);
        }

        public ImageConfigStep allCenter() {
            return allScaleType(ScalingUtils.ScaleType.CENTER);
        }

        public ImageConfigStep allCenterInside() {
            return allScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        }

        public ImageConfigStep actualScaleType(ScalingUtils.ScaleType scaleType) {
            mImageConfig.actualScaleType = scaleType;
            return this;
        }

        public ImageConfigStep placeholder(Drawable drawable) {
            mImageConfig.placeholderDrawable = drawable;
            return this;
        }

        public ImageConfigStep placeholder(int resId) {
            mImageConfig.placeholderResId = resId;
            return this;
        }

        public ImageConfigStep placeholderScaleType(ScalingUtils.ScaleType scaleType) {
            mImageConfig.placeholderScaleType = scaleType;
            return this;
        }

        public ImageConfigStep failure(Drawable drawable) {
            mImageConfig.failureDrawable = drawable;
            return this;
        }

        public ImageConfigStep failure(int resId) {
            mImageConfig.failureResId = resId;
            return this;
        }

        public ImageConfigStep failureScaleType(ScalingUtils.ScaleType scaleType) {
            mImageConfig.failureScaleType = scaleType;
            return this;
        }

        public ImageConfigStep retry(Drawable drawable) {
            mImageConfig.retryDrawable = drawable;
            return this;
        }

        public ImageConfigStep retry(int resId) {
            mImageConfig.retryResId = resId;
            return this;
        }

        public ImageConfigStep retryScaleType(ScalingUtils.ScaleType scaleType) {
            mImageConfig.retryScaleType = scaleType;
            return this;
        }

        public ImageConfigStep progressBar(Drawable drawable) {
            mImageConfig.progressBarDrawable = drawable;
            return this;
        }

        public ImageConfigStep progressBar(int resId) {
            mImageConfig.progressBarResId = resId;
            return this;
        }

        public ImageConfigStep progressBarScaleType(ScalingUtils.ScaleType scaleType) {
            mImageConfig.progressBarScaleType = scaleType;
            return this;
        }

        public ImageConfigStep asCircle() {
            mImageConfig.asCircle = true;
            return this;
        }

        public ImageConfigStep asRound(float radius) {
            mImageConfig.asRound = true;
            mImageConfig.cornerRadius = radius;
            return this;
        }

        public ImageConfigStep roundBorderWidth(int width) {
            mImageConfig.roundBorderWidth = width;
            return this;
        }

        public ImageConfigStep roundBorderColor(int color) {
            mImageConfig.roundBorderColor = color;
            return this;
        }

        /**
         * 所有配置项是否应用在全新的 Hierarchy 中，默认使用 SimpleDraweeView 自带的 Hierarchy
         * @return
         */
        public ImageConfigStep useNewHierarchy() {
            mImageConfig.useNewHierarchy = true;
            return this;
        }
    }

    static class ImageConfig {
        boolean useNewHierarchy = false;

        int fadeDuration = -1;
        ScalingUtils.ScaleType actualScaleType;

        Drawable placeholderDrawable = null;
        int placeholderResId = -1;
        ScalingUtils.ScaleType placeholderScaleType;

        Drawable failureDrawable;
        int failureResId = -1;
        ScalingUtils.ScaleType failureScaleType;

        Drawable retryDrawable;
        int retryResId = -1;
        ScalingUtils.ScaleType retryScaleType;

        Drawable progressBarDrawable;
        int progressBarResId = -1;
        ScalingUtils.ScaleType progressBarScaleType;

        boolean asCircle = false;
        boolean asRound = false;

        float cornerRadius = -1;

        int roundBorderWidth = -1;
        int roundBorderColor = -1;
    }
}
