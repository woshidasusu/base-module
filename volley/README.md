# DVolley 组件

基于 Volley 进行的二次封装，目的也是在于简化外部使用

组件有如下两个依赖库：
```
compile 'com.android.volley:volley:1.1.1'
compile 'com.google.code.gson:gson:2.7'
```

支持以下功能：
- get  请求
- post 请求
- 图片下载在本地指定目录
- 自动根据泛型解析 json
- 取消指定请求
- 设置通用请求参数或请求头
- 支持统一处理相同的外层数据结构

#### 使用示例

```
compile 'com.dasu.net:volley:0.0.2'
```

```
//get 方法获取 wanAndroid 网站的公众号列表，内部自动进行 json 解析
DVolley.url("http://wanandroid.com/wxarticle/chapters/json")
        .get()
        .enqueue(new VolleyListener<ArrayList<WanAndroid>>() {
            @Override
            public void onSuccess(ArrayList<WanAndroid> data) {
                Log.w("!!!!!!!", "wan: " + data.size());
                for (WanAndroid wan : data) {
                   Log.e("!!!!!!!!!!", wan.toString());
                }
            }

            @Override
            public void onError(int code, String description) {

            }});

//post 方法请求，设置参数，请求头，tag（用于取消请求使用）
DVolley.url("https://easy-mock.com/mock/5b592c01e4e04f38c7a55958/ywb/is/version/checkVersion")
         .post()
         .tag("VolleyActivity")
         .addParam("name", "dasu")
         .addHeader("weixin", "dasuAndroidTv")
         .enqueue(new VolleyListener<EasyMockReturn>() {
             @Override
             public void onSuccess(EasyMockReturn data) {
                 Log.e("!!!!!", "return: " + data);
             }

             @Override
             public void onError(int code, String description) {

             }
         });

//取消tag为xxx的请求
DVolley.cancelRequests("VolleyActivity");

//下载图片文件到本地指定的目录
DVolley.url("https://upload-images.jianshu.io/upload_images/3537898-445477c7ce870988.png")
        .asImageFile()
        .downloadTo(new File("/mnt/sdcard/abcd.png"), new VolleyListener<String>() {
            @Override
            public void onSuccess(String data) {
                Log.e("!!!!!", "asImageFile: " + data);
            }

            @Override
            public void onError(int code, String description) {
                Log.e("!!!!!", "asImageFile: " + description);
            }
        });

//设置通用的请求参数或请求头
DVolley.enterGlobalConfig()
         .globalParam("t", String.valueOf(System.currentTimeMillis()))
         .globalHeader("os", "android");

//设置底层统一处理wanandroid网站api返回的相同的外层数据结构的处理
DVolley.url("http://www.wanandroid.com/article/list/1/json")
        .get()
        .responseInterceptor(new WamsResponseInterceptor())
        .enqueue(new VolleyListener<HomeArticleResEntity>(){...});
```

[博客详情介绍跳转](https://www.jianshu.com/p/17cd8218b147)
