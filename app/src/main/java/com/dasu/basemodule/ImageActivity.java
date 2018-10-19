package com.dasu.basemodule;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dasu.log.LogUtils;
import com.dasu.utils.DeviceUtils;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.R.attr.track;
import static android.R.attr.x;

public class ImageActivity extends AppCompatActivity {


    @InjectView(R.id.iv_image)
    ImageView mImageView;

    @InjectView(R.id.iv_image1)
    ImageView mImageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.inject(this);


        LogUtils.e("!!!!!!", DeviceUtils.getDeviceInfo(this));
        ApplicationInfo applicationInfo = getApplicationInfo();

        Field[] fields = ApplicationInfo.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(applicationInfo);
                if (value instanceof String[]) {
                    Log.e("!!!!!!!!!", key + " = " + Arrays.toString((String[])value));
                } else {
                    Log.e("!!!!!!!!!", key + " = " + String.valueOf(value));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String javaLibraryPath = System.getProperty("java.library.path");
        Log.e("!!!!!!", "java.library.path = " + javaLibraryPath);

    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i("!!!!!!", "===============================");
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                loadResImage(mImageView);
//                loadSdImage(mImageView);
//                loadNetImageByFresco(mImageView);
//                loadResImageByFresco(mImageView);
//                loadSdImageByFresco(mImageView);
                loadResImageByGlide(mImageView);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
//                loadResImage(mImageView1);
//                loadSdImage(mImageView1);
//                loadNetImageByFresco(mImageView1);
//                loadResImageByFresco(mImageView1);
//                loadSdImageByFresco(mImageView1);
                loadResImageByGlide(mImageView1);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:

                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:

                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void loadResImageByGlide(final ImageView imageView) {
//        Glide.with(this).load(R.drawable.weixin)
//                .addListener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
//                        BitmapDrawable drawable = (BitmapDrawable) resource;
//                        Bitmap bitmap = drawable.getBitmap();
//                        Log.i("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
//                        Log.i("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
//                        Log.i("!!!!!!", "imageview.width:" + imageView.getWidth() + ":::imageview.height:" + imageView.getHeight());
//                        return false;
//                    }
//                }).into(imageView);

        Glide.with(this).asBitmap().load(new File("mnt/sdcard/weixin.jpg")).addListener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                Bitmap bitmap = resource;
                Log.i("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
                Log.i("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
                Log.i("!!!!!!", "imageview.width:" + imageView.getWidth() + ":::imageview.height:" + imageView.getHeight());

                return false;
            }
        }).into(imageView/*new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Bitmap bitmap = resource;
                Log.i("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
                Log.i("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
                Log.i("!!!!!!", "imageview.width:" + imageView.getWidth() + ":::imageview.height:" + imageView.getHeight());

            }
        }*/);

    }

    private void loadResImage(ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.weixin, options);
        imageView.setImageBitmap(bitmap);
        Log.i("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
        Log.i("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
        Log.i("!!!!!!", "inDensity:" + options.inDensity + ":::inTargetDensity:" + options.inTargetDensity);
        Log.i("!!!!!!", "imageview.width:" + imageView.getWidth() + ":::imageview.height:" + imageView.getHeight());
    }

    private void loadResImageByFresco(final ImageView imageView) {
        Fresco.initialize(this);
        Uri uri = Uri.parse("res://" + this.getPackageName() + "/" + R.drawable.weixin);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(500, 500))
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(final @Nullable Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                        Log.w("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
                        Log.w("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
                        Log.w("!!!!!!", "imageview.width:" + imageView.getWidth() + ":::imageview.height:" + imageView.getHeight());
                    }
                });
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {

            }
        }, CallerThreadExecutor.getInstance());
    }

    private void loadNetImageByFresco(final ImageView imageView) {
        Fresco.initialize(this);
        Uri uri = Uri.parse("https://upload-images.jianshu.io/upload_images/1924341-f4f0bc17fe695a80.jpg");
//        Uri uri = Uri.parse("https://upload-images.jianshu.io/upload_images/1924341-d7190704b160d280.png");
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(final @Nullable Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                        Log.w("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
                        Log.w("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
                        Log.w("!!!!!!", "imageview.width:" + imageView.getWidth() + ":::imageview.height:" + imageView.getHeight());
                    }
                });
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {

            }
        }, CallerThreadExecutor.getInstance());
    }

    private void loadSdImageByFresco(final ImageView imageView) {
        Fresco.initialize(this);
        Uri uri = Uri.parse("file://" + "mnt/sdcard/weixin.png");
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(final @Nullable Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                        Log.w("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
                        Log.w("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
                        Log.w("!!!!!!", "imageview.width:" + imageView.getWidth() + ":::imageview.height:" + imageView.getHeight());
                    }
                });
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {

            }
        }, CallerThreadExecutor.getInstance());
    }

    private void loadSdImage(ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile("mnt/sdcard/weixin.png", options);
        Bitmap bitmap = BitmapFactory.decodeFile("mnt/sdcard/weixin.jpg", options);
        imageView.setImageBitmap(bitmap);
        Log.i("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
        Log.i("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
        Log.i("!!!!!!", "inDensity:" + options.inDensity + ":::inTargetDensity:" + options.inTargetDensity);
        Log.i("!!!!!!", "imageview.width:" + imageView.getWidth() + ":::imageview.height:" + imageView.getHeight());
    }
}
