package com.dasu.basemodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;

import com.dasu.basemodule.wams.HomeArticlesResEntity;
import com.dasu.basemodule.wams.HomeController;
import com.dasu.basemodule.wams.WamsResEntity;
import com.dasu.fresco.DFresco;
import com.dasu.volley.DVolley;
import com.dasu.volley.VolleyListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class VolleyActivity extends AppCompatActivity {

    @InjectView(R.id.sdv_volley)
    public SimpleDraweeView mSimpleDraweeView;

    @InjectView(R.id.btn_volley_was_home)
    public Button mWasHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        ButterKnife.inject(this);

        DVolley.url("http://www.12306.cn/mormhweb/")
                .get()
                .tag(this)
                .enqueue(new VolleyListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        Log.e("!!!!!!", data + "");
                    }

                    @Override
                    public void onError(int code, String description) {
                        Log.e("!!!!!!", "onError");
                    }
                });
        DVolley.url("https://www.baidu.com")
                .get()
                .tag("ddd")
                .enqueue(new VolleyListener() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.e("!!!!!!", data + "");
                    }

                    @Override
                    public void onError(int code, String description) {
                        Log.e("!!!!!!", "onError");
                    }
                });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
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
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
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
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            DFresco.source(new File("/mnt/sdcard/abcd.png"))
                    .intoTarget(mSimpleDraweeView);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            DVolley.url("http://wanandroid.com/wxarticle/chapters/json")
                    .get()
                    .enqueue(new VolleyListener<WamsResEntity<ArrayList<WanAndroid>>>() {
                        @Override
                        public void onSuccess(WamsResEntity<ArrayList<WanAndroid>> data) {
                            Log.w("!!!!!!!", "wan: " + data.getData().size());
                            for (WanAndroid wan : data.getData()) {
                                Log.w("!!!!!!!!!", "" + wan);
                            }
                        }

                        @Override
                        public void onError(int code, String description) {

                        }
                    });
        }
        return super.onKeyUp(keyCode, event);
    }

    @OnClick(R.id.btn_volley_was_home)
    public void onWasHomeBtnClick() {
        HomeController.getArticleList(0, this, new VolleyListener<HomeArticlesResEntity>() {
            @Override
            public void onSuccess(HomeArticlesResEntity data) {
                Log.d("!!!!!", "最外层： " + data + "");
            }

            @Override
            public void onError(int code, String description) {

            }
        });
    }

    public static class WanAndroid implements Serializable {
        private List<String> children;
        private int courseId;
        private int id;
        private String name;
        private long order;
        private int parentChapterId;
        private boolean userControlSetTop;
        private int visible;

        @Override
        public String toString() {
            return "WanAndroid{" +
                    "children=" + children +
                    ", courseId=" + courseId +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", order=" + order +
                    ", parentChapterId=" + parentChapterId +
                    ", userControlSetTop=" + userControlSetTop +
                    ", visible=" + visible +
                    '}';
        }

        public List<String> getChildren() {
            if (children == null) {
                return new ArrayList<>();
            }
            return children;
        }

        public void setChildren(List<String> children) {
            this.children = children;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getOrder() {
            return order;
        }

        public void setOrder(long order) {
            this.order = order;
        }

        public int getParentChapterId() {
            return parentChapterId;
        }

        public void setParentChapterId(int parentChapterId) {
            this.parentChapterId = parentChapterId;
        }

        public boolean isUserControlSetTop() {
            return userControlSetTop;
        }

        public void setUserControlSetTop(boolean userControlSetTop) {
            this.userControlSetTop = userControlSetTop;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }
    }

    public static class EasyMockReturn implements Serializable {
        private int returnCode;
        private int type;
        private String returnMsg;
        private String content;

        public int getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(int returnCode) {
            this.returnCode = returnCode;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getReturnMsg() {
            return returnMsg == null ? "" : returnMsg;
        }

        public void setReturnMsg(String returnMsg) {
            this.returnMsg = returnMsg;
        }

        public String getContent() {
            return content == null ? "" : content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "EasyMockReturn{" +
                    "returnCode=" + returnCode +
                    ", type=" + type +
                    ", returnMsg='" + returnMsg + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    public static class VersionEntity implements Serializable {
        private String result;
        private String updateUrl;
        private String versionDesc;
        private String versionCode;
        private String packageSize;
        private String updateType;
        private String versionName;

        public String getResult() {
            return result == null ? "" : result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getUpdateUrl() {
            return updateUrl == null ? "" : updateUrl;
        }

        public void setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
        }

        public String getVersionDesc() {
            return versionDesc == null ? "" : versionDesc;
        }

        public void setVersionDesc(String versionDesc) {
            this.versionDesc = versionDesc;
        }

        public String getVersionCode() {
            return versionCode == null ? "" : versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getPackageSize() {
            return packageSize == null ? "" : packageSize;
        }

        public void setPackageSize(String packageSize) {
            this.packageSize = packageSize;
        }

        public String getUpdateType() {
            return updateType == null ? "" : updateType;
        }

        public void setUpdateType(String updateType) {
            this.updateType = updateType;
        }

        public String getVersionName() {
            return versionName == null ? "" : versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        @Override
        public String toString() {
            return "VersionEntity{" +
                    "result='" + result + '\'' +
                    ", updateUrl='" + updateUrl + '\'' +
                    ", versionDesc='" + versionDesc + '\'' +
                    ", versionCode='" + versionCode + '\'' +
                    ", packageSize='" + packageSize + '\'' +
                    ", updateType='" + updateType + '\'' +
                    ", versionName='" + versionName + '\'' +
                    '}';
        }
    }
}
