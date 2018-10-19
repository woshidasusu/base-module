package com.dasu.basemodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.dasu.fresco.DFresco;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FrescoActivity extends AppCompatActivity {

    @InjectView(R.id.sdv_fresco)
    public SimpleDraweeView mSimpleDraweeView;

    @InjectView(R.id.iv_fresco)
    public ImageView mImageView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);
        ButterKnife.inject(this);

        mContext = this;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            DFresco.source(this, R.drawable.aaaa).intoTarget(mSimpleDraweeView, new BaseControllerListener() {
                @Override
                public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                    Log.e("!!!!!!", "onFinalImageSet");
                }
            });
            DFresco.source(new File("/mnt/sdcard/weixin.jpg")).intoTarget(new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    Log.w("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
                    Log.w("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
                    mImageView.setImageBitmap(bitmap);
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    Log.e("!!!!!!", "onFailureImpl");
                }
            });
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            DFresco.source("https://upload-images.jianshu.io/upload_images/1924341-9e528ee638e837a5.png")
                    .enterImageConfig()
                    .allFitXY()
                    .animFade(3000)
                    .placeholderScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
                    .actualScaleType(ScalingUtils.ScaleType.CENTER)
//                    .asRound(50)
                    .roundBorderColor(Color.RED)
                    .roundBorderWidth(20)
                    .failure(R.drawable.timg)
                    .progressBar(R.drawable.aaaa)
                    .retry(R.drawable.weixin)
                    .asCircle()
                    .placeholder(R.drawable.image)
                    .finishImageConfig()
                    .intoTarget(mSimpleDraweeView);

            DFresco.source(this, R.drawable.timg)
                    .resize(500, 500)
                    .intoTarget(new BaseBitmapDataSubscriber() {
                        @Override
                        protected void onNewResultImpl(Bitmap bitmap) {
                            Log.w("!!!!!!", "bitmap：ByteCount = " + bitmap.getByteCount() + ":::bitmap：AllocationByteCount = " + bitmap.getAllocationByteCount());
                            Log.w("!!!!!!", "width:" + bitmap.getWidth() + ":::height:" + bitmap.getHeight());
                            mImageView.setImageBitmap(bitmap);
                        }

                        @Override
                        protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                            Log.e("!!!!!!", "onFailureImpl");
                        }
                    });

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            DFresco.source("https://upload-images.jianshu.io/upload_images/1018039-23c9907138c94387.png")
                    .downloadOnly(this);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            DFresco.source(mContext, R.drawable.weixin).intoTarget(mSimpleDraweeView);

        }
        return super.onKeyUp(keyCode, event);
    }
}