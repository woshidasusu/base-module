# DFresco 组件

基于 Fresco 图片库进行的二次封装，因为 Fresco 的使用太分散了，太麻烦了，需要自己写 Controller，写 Hierarchy 等等。

参考 Glide 的使用方式，对 fresco 进行了二次封装。

组件有如下依赖库：
```
compile 'com.facebook.fresco:fresco:0.14.1'
compile 'com.facebook.fresco:animated-gif:0.14.1'
```

#### 使用示例

```
compile 'com.dasu.image:fresco:0.0.2'
```

使用之前，需先初始化，建议在 Application 中进行：

```
DFresco.init(this);
```

然后即可开始类似 Glide 直接使用：

```
//加载 res 中的 drawable 图片到 SimpleDraweeView 控件上（默认支持 gif 图，并且会自动根据控件宽高进行分辨率压缩，降低内存占用
DFresco.source(mContext, R.drawable.weixin).intoTarget(mSimpleDraweeView);

//加载磁盘中的图片，手动设置分辨率的压缩，并获取 bitmap 对象，监听回调，手动显示到 ImageView 控件上
DFresco.source(new File("/mnt/sdcard/weixin.jpg"))
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

//加载网络图片，进行各种配置，如缩放方式，占位图，圆形，圆角，动画时长等等，最后自动显示到 SimpleDraweeView 控件上
DFresco.source("https://upload-images.jianshu.io/upload_images/1924341-9e528ee638e837a5.png")
                    .enterImageConfig() //进入配置步骤
                    .allFitXY()  //所有图片，包括占位图等等的拉伸方式
                    .animFade(3000) //淡入淡出动画时长
                    .placeholderScaleType(ScalingUtils.ScaleType.CENTER_INSIDE) //设置占位图的拉伸方式，后面设置的会覆盖前面的
                    .actualScaleType(ScalingUtils.ScaleType.CENTER)
//                    .asRound(50) //设置圆角，（圆角和圆形不能同时设置）
                    .asCircle() //设置控件显示为圆形控件
                    .roundBorderColor(Color.RED) //设置圆角或圆形的边框颜色
                    .roundBorderWidth(20)  //设置圆角或圆形的边框宽度
                    .failure(R.drawable.timg) //设置失败图
                    .progressBar(R.drawable.aaaa) //设置加载进度图
                    .retry(R.drawable.weixin) //设置重试时的图
                    .placeholder(R.drawable.image) //设置占位图
                    .finishImageConfig() //退出配置步骤
                    .intoTarget(mSimpleDraweeView);
```

其实，也就是将官方提供的 GenericDraweeHierarchy 以 Java 方式配置 DraweeView 的各种 api 再封装一层，达到只需了解一个入口 DFresco 即可链式调用各种配置项的目的，
简化外部使用和接入成本。

[博客详情解析跳转:https://www.jianshu.com/p/6b462c022ca8](https://www.jianshu.com/p/6b462c022ca8)
