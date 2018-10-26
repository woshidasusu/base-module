package com.dasu.basemodule.wams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * https://github.com/woshidasusu/DWanAndroid
 */
public class HomeArticlesResEntity implements Serializable {

    private int curPage;
    private List<ArticleEntity> datas;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;

    @Override
    public String toString() {
        return "HomeArticlesResEntity{" +
                "curPage=" + curPage +
                ", datas=" + datas +
                ", offset=" + offset +
                ", over=" + over +
                ", pageCount=" + pageCount +
                ", size=" + size +
                ", total=" + total +
                '}';
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<ArticleEntity> getDatas() {
        if (datas == null) {
            return new ArrayList<>();
        }
        return datas;
    }

    public void setDatas(List<ArticleEntity> datas) {
        this.datas = datas;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
