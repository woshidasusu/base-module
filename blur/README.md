# 高斯模糊

基于 https://github.com/kikoso/android-stackblur 进行的封装，封装目的在于简化外部使用

#### 使用示例

```
compile 'com.dasu.image:blur:0.0.4'
```

```
//使用默认配置，最短调用链
Bitmap bitmap = DBlur.source(MainActivity.this).build().doBlurSync();

//同步模糊，将imageView控制的视图进行模糊，完成后自动显示到 imageView1 控件上，以淡入动画方式
DBlur.source(imageView).intoTarget(imageView1).animAlpha().build().doBlurSync();

//异步模糊，将drawable资源文件中的图片以 NATIVE 方式进行模糊，注册回调，完成时手动显示到 imageView1 控件上
DBlur.source(this, R.drawable.background).mode(BlurConfig.MODE_NATIVE).build()
      .doBlur(new OnBlurListener() {
            @Override
            public void onBlurSuccess(Bitmap bitmap) {
                imageView1.setImageBitmap(bitmap);
            }

            @Override
            public void onBlurFailed() {
                //do something
            }});
```  

[博客跳转：封装个 Android 的高斯模糊组件](https://www.jianshu.com/p/6064a14d86a3)
