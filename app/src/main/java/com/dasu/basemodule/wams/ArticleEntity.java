package com.dasu.basemodule.wams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * https://github.com/woshidasusu/DWanAndroid
 */
public class ArticleEntity implements Serializable {

    private String apkLink;
    private String author;
    private long chapterId;
    private String chapterName;
    private boolean collect;
    private long courseId;
    private String desc;
    private String envelopePic;
    private boolean fresh;
    private long id;
    private String link;
    private String niceDate;
    private String origin;
    private String projectLink;
    private long publishTime;
    private long superChapterId;
    private String superChapterName;
    private List<Tag> tags;
    private String title;
    private int type;
    private long userId;
    private int visible;
    private int zan;

    public String getApkLink() {
        return apkLink == null ? "" : apkLink;
    }

    public void setApkLink(String apkLink) {
        this.apkLink = apkLink;
    }

    public String getAuthor() {
        return author == null ? "" : author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName == null ? "" : chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getDesc() {
        return desc == null ? "" : desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnvelopePic() {
        return envelopePic == null ? "" : envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link == null ? "" : link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNiceDate() {
        return niceDate == null ? "" : niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getOrigin() {
        return origin == null ? "" : origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProjectLink() {
        return projectLink == null ? "" : projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public long getSuperChapterId() {
        return superChapterId;
    }

    public void setSuperChapterId(long superChapterId) {
        this.superChapterId = superChapterId;
    }

    public String getSuperChapterName() {
        return superChapterName == null ? "" : superChapterName;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

    public List<Tag> getTags() {
        if (tags == null) {
            return new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    @Override
    public String toString() {
        return "ArticleEntity{" +
                "apkLink='" + apkLink + '\'' +
                ", author='" + author + '\'' +
                ", chapterId=" + chapterId +
                ", chapterName='" + chapterName + '\'' +
                ", collect=" + collect +
                ", courseId=" + courseId +
                ", desc='" + desc + '\'' +
                ", envelopePic='" + envelopePic + '\'' +
                ", fresh=" + fresh +
                ", id=" + id +
                ", link='" + link + '\'' +
                ", niceDate='" + niceDate + '\'' +
                ", origin='" + origin + '\'' +
                ", projectLink='" + projectLink + '\'' +
                ", publishTime=" + publishTime +
                ", superChapterId=" + superChapterId +
                ", superChapterName='" + superChapterName + '\'' +
                ", tags=" + tags +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", userId=" + userId +
                ", visible=" + visible +
                ", zan=" + zan +
                '}';
    }

    public static class Tag implements Serializable {

        private String name;   //公众号
        private String url;

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Tag{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
