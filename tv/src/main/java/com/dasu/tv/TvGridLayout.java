package com.dasu.tv;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Created by dasu on 2018/4/23.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 */

public class TvGridLayout extends FrameLayout {
    private static final String TAG = "TvGridLayout";
    private static final int ANIMATED_SCROLL_GAP = 500;//滑动的时长
    private static Interpolator sInterpolator = new AccelerateDecelerateInterpolator();

    private static int[] sTwoInt = new int[2];

    private Context mContext;
    private OverScroller mScroller;
    private long mLastScroll;
    private int mCurPageIndex = 0;
    private int mRightEdge;
    private boolean mIsOnScrolling;

    private Adapter mAdapter;
    private int mWidth;
    private int mHeight;
    private int mItemSpace;
    private boolean mIsConsumeKeyEvent;

    private SparseArray<View> mFirstChildOfPage = new SparseArray<>();
    private SparseIntArray mWidthOfPage = new SparseIntArray();
    private OnBorderListener mBorderListener;
    private OnScrollListener mScrollListener;

    public TvGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setHorizontalScrollBarEnabled(false);

        mScroller = new OverScroller(context, sInterpolator);

        setClipChildren(false);
        setClipToPadding(false);
    }

    public TvGridLayout(Context context) {
        super(context);
        init(context);
    }

    public void setItemSpace(int itemSpace) {
        mItemSpace = itemSpace;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int x = mScroller.getCurrX();
            int finalX = mScroller.getFinalX();
            if (oldX != x) {
                scrollTo(x, 0);
            }
            if (x == finalX) {
                if (mIsOnScrolling) {
                    mIsOnScrolling = false;
                    if (mScrollListener != null) {
                        mScrollListener.onScrollEnd();
                    }
                }
            }
        } else {
            if (mIsOnScrolling) {
                mIsOnScrolling = false;
                if (mScrollListener != null) {
                    mScrollListener.onScrollEnd();
                }
            }
        }
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        if (mAdapter == adapter) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.onSwitchAdapter(adapter, mAdapter);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    removeAllViews();
                    layoutChildren();
                }
            });
        }
    }

    private void layoutChildren() {
        mFirstChildOfPage.clear();
        mWidthOfPage.clear();
        mRightEdge = 0;
        mCurPageIndex = 0;
//        layoutChildrenOfPages(0, mAdapter.getGroupCount());
        if (this.getLocalVisibleRect(new Rect())) {
            layoutChildrenOfPages(0, 1);
            post(new Runnable() {
                @Override
                public void run() {
                    layoutChildrenOfPages(1, mAdapter.getPageCount());
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    layoutChildrenOfPages(0, mAdapter.getPageCount());
                }
            });
        }
    }

    private void layoutChildrenOfPages(int fromPage, int toPage) {
        int contentWidth = mWidth - getPaddingLeft() - getPaddingRight();
        int contentHeight = mHeight - getPaddingTop() - getPaddingBottom();
        for (int j = fromPage; j < toPage; j++) {
            int column = mAdapter.getPageColumn(j);//列数
            int row = mAdapter.getPageRow(j);//行数
            float itemWidth = (contentWidth) * 1.0f / column;//每个item宽度
            float itemHeight = (contentHeight) * 1.0f / row;//每个item高度

            int pageWidth = 0;

            //遍历每个item
            for (int i = 0; i < mAdapter.getChildCount(j); i++) {
                ItemCoordinate childCoordinate = mAdapter.getChildCoordinate(j, i);
                if (childCoordinate == null) {
                    continue;
                }
                int pointStartX = childCoordinate.start.x;
                int pointStartY = childCoordinate.start.y;
                int pointEndX = childCoordinate.end.x;
                int pointEndY = childCoordinate.end.y;

                //item大小，包括间距
                int width = (int) ((pointEndX - pointStartX) * itemWidth);
                int height = (int) ((pointEndY - pointStartY) * itemHeight);
                //item位置
                int marginLeft = (int) (pointStartX * itemWidth + contentWidth * j);
                int marginTop = (int) (pointStartY * itemHeight);

                if (marginLeft < 0) {
                    marginLeft = 0;
                }
                if (marginTop < 0) {
                    marginTop = 0;
                }

                //获取item view
                View view = mAdapter.getChildView(j, i, width, height);
                if (view == null) {
                    continue;
                }

                //开始layout
                LayoutParams params = new LayoutParams(width - mItemSpace * 2, height - mItemSpace * 2);//扣除间距
                params.topMargin = marginTop + mItemSpace;
                params.leftMargin = marginLeft + mItemSpace;
                params.mItemCoordinate = childCoordinate;
                params.pageIndex = j;

                //记录每一页长度
                int maxWidth = marginLeft + width - contentWidth * j;
                pageWidth = Math.max(pageWidth, maxWidth);

                int maxRight = marginLeft + width;
                mRightEdge = Math.max(mRightEdge, maxRight);

                if (childCoordinate.start.x == 0 && childCoordinate.start.y == 0) {
                    mFirstChildOfPage.put(j, view);
                }

                if (j == 0 && childCoordinate.start.x == 0 && childCoordinate.start.y == 0) {
                    addView(view, 0, params);
                } else {
                    addView(view, params);
                }
            }
            mWidthOfPage.put(j, pageWidth);
        }
    }

    public View getFirstChildOfScreen(int screenIndex) {
        if (mFirstChildOfPage != null) {
            return mFirstChildOfPage.get(screenIndex);
        }
        return null;
    }

    public void setOnBorderListener(OnBorderListener listener) {
        mBorderListener = listener;
    }

    public void smoothScrollTo(int dx) {
        smoothScrollBy(dx - getScrollX());
    }

    public void smoothScrollBy(int dx) {
        if (getChildCount() == 0) {
            // Nothing to do.
            return;
        }

        long duration = AnimationUtils.currentAnimationTimeMillis() - mLastScroll;
        final int width = getWidth() - getPaddingLeft();
        final int rightEdge = mRightEdge + getPaddingRight();
        final int maxX = Math.max(0, rightEdge - width);

        if (duration > ANIMATED_SCROLL_GAP) {
            if (mScrollListener != null) {
                mScrollListener.onScrollStart();
            }
            mIsOnScrolling = true;
            final int scrollX = getScrollX();
            dx = Math.max(0, Math.min(scrollX + dx, maxX)) - scrollX;

            mScroller.startScroll(scrollX, 0, dx, 0, ANIMATED_SCROLL_GAP);
            postInvalidateOnAnimation();
        } else {
            int finalX = 0;
            boolean needAdjustScrollX = false;
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
                finalX = mScroller.getFinalX();
                needAdjustScrollX = true;
            }
            dx = Math.max(0, Math.min(finalX + dx, maxX)) - finalX;

            if (needAdjustScrollX) {
                dx = finalX + dx;
            } else {
                dx = getScrollX() + dx;
            }
            if (mScrollListener != null) {
                mScrollListener.onScrollStart();
            }
            scrollTo(dx, getScrollY());
            if (mScrollListener != null) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollListener.onScrollEnd();
                    }
                });
            }
        }
        mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    private boolean executeKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            mIsConsumeKeyEvent = false;
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (checkIfOnBorder(FOCUS_LEFT, sTwoInt)) {
                    mIsConsumeKeyEvent = true;
                    if (mBorderListener != null && mBorderListener.onLeft(sTwoInt[0], sTwoInt[1])) {
                        return true;
                    }
                    scrollToPage(sTwoInt[1]);
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (checkIfOnBorder(FOCUS_RIGHT, sTwoInt)) {
                    mIsConsumeKeyEvent = true;
                    if (mBorderListener != null && mBorderListener.onRight(sTwoInt[0], sTwoInt[1])) {
                        return true;
                    }
                    scrollToPage(sTwoInt[1]);
                }
            }
        } else {
            if (mIsConsumeKeyEvent) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean  requestFocus(int direction, Rect previouslyFocusedRect) {
        //GridMenuLayout与ViewPager合用时，当GridMenuLayout焦点在边界移动时，会去触发ViewPager的切菜单事件
        //对于下个菜单的GridMenuLayout，需要默认聚焦到第一个子View，使用系统默认的焦点寻找策略会出问题
        //所以在这个回调里进行处理
        if (previouslyFocusedRect == null && (direction != FOCUS_UP && direction != FOCUS_DOWN)) {
            final View view = mFirstChildOfPage.get(mCurPageIndex);
            if (view != null) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.requestFocus();
                    }
                });
            }
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    private boolean checkIfOnBorder(int direction, int[] twoPage) {
        if (direction == FOCUS_LEFT || direction == FOCUS_RIGHT) {
            View view = getFocusedChild();
            View childView = findChildView(view);
            if (childView != null) {
                int curPage = ((LayoutParams) childView.getLayoutParams()).pageIndex;
                twoPage[0] = twoPage[1] = curPage;
                View nextFocusView = view.focusSearch(direction);
                View nextChildView = findChildView(nextFocusView);
                if (nextChildView == null) {
                    return true;
                }
                int nextPage = ((LayoutParams) nextChildView.getLayoutParams()).pageIndex;
                twoPage[1] = nextPage;
                return curPage != nextPage;
            }
        }
        return false;
    }

    public void scrollToPage(int pageIndex) {
        scrollToPage(pageIndex, true);
    }

    private View findChildView(View view) {
        View childView = null;
        if (view != null) {
            if (view.getParent() == this) {
                childView = view;
            } else {
                boolean isChild = false;
                ViewParent parent = view.getParent();
                for (; parent.getParent() instanceof ViewGroup; ) {
                    if (parent.getParent() == this) {
                        isChild = true;
                        break;
                    }
                    parent = parent.getParent();
                }
                if (isChild) {
                    childView = (View) parent;
                }
            }
        }
        return childView;
    }

    public void scrollToPage(int pageIndex, boolean smooth) {
        if (mCurPageIndex != pageIndex) {
            int pageWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            int scrollXDelta = (pageIndex - mCurPageIndex) * (pageWidth);
            //todo 计算有问题
            if (mCurPageIndex == 0 || mCurPageIndex == mAdapter.getPageCount() - 1) {
                int w = mWidthOfPage.get(mCurPageIndex, 0);
                if (pageWidth != w) {
                    int adjustW = pageWidth - w;
                    scrollXDelta += scrollXDelta > 0 ? -adjustW : adjustW;
                }
            }

            if (scrollXDelta != 0) {
                if (smooth) {
                    smoothScrollBy(scrollXDelta);
                } else {
                    final int width = getWidth();
                    final int rightEdge = mRightEdge + getPaddingRight();
                    final int maxX = Math.max(0, rightEdge - width);
                    final int scrollX = getScrollX();
                    int dx = Math.max(0, Math.min(scrollX + scrollXDelta, maxX)) - scrollX;
                    scrollBy(dx, 0);
                }
            }
            mCurPageIndex = pageIndex;
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    public int getCurrentPage() {
        return mCurPageIndex;
    }

    public interface OnBorderListener {
        boolean onLeft(int curPageIndex, int nextPageIndex);

        boolean onRight(int curPageIndex, int nextPageIndex);
    }

    public interface OnScrollListener {
        void onScrollStart();

        void onScrollEnd();
    }

    public static abstract class Adapter {
        public abstract int getPageRow(int pageIndex);

        public abstract int getPageColumn(int pageIndex);

        public abstract ItemCoordinate getChildCoordinate(int pageIndex, int childIndex);

        public abstract View getChildView(int groupPosition, int childPosition, int childW, int childH);

        public abstract int getChildCount(int pageIndex);

        public abstract int getPageCount();

        protected void onSwitchAdapter(Adapter newAdapter, Adapter oldAdapter) {
        }
    }

    /**
     * 用于记录每个小格item项的坐标位置
     */
    public static class ItemCoordinate {
        public Point start;//左上角坐标
        public Point end;//右下角坐标

        @Override
        public String toString() {
            return "ItemCoordinate{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    private static class LayoutParams extends FrameLayout.LayoutParams {
        ItemCoordinate mItemCoordinate;
        int pageIndex;

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
