package com.rave.library;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.nineoldandroids.view.ViewPropertyAnimator;


public class NinjaViewHelper {

    public enum ViewPosition {
        TOP, BOTTOM
    }

    private static final int SCROLL_TO_TOP = -1;

    private static final int SCROLL_TO_BOTTOM = 1;

    private static final int SCROLL_DIRECTION_CHANGE_THRESHOLD = 5;

    private Activity mActivity;

    private LayoutInflater mLayoutInflater;

    private View mNinjaView;

    private int mScrollDirection = 0;

    private int mPoppyViewHeight = -1;

    private ViewPosition mViewPosition;

    public NinjaViewHelper(Activity activity, ViewPosition position) {
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity);
        mViewPosition = position;
    }

    public NinjaViewHelper(Activity activity) {
        this(activity, ViewPosition.BOTTOM);
    }

    /**
     * 在ScrollView上添加PoppyView
     *
     * @param scrollViewId   ScrollView的ResourceID
     * @param poppyViewResId PoppyView的ResourceID
     * @return
     */
    public View createNinjaViewOnScrollView(int scrollViewId, int poppyViewResId) {
        mNinjaView = mLayoutInflater.inflate(poppyViewResId, null);
        final NotifyingScrollView scrollView = (NotifyingScrollView) mActivity.findViewById(scrollViewId);
        initPoppyViewOnScrollView(scrollView);
        return mNinjaView;
    }

    /**
     * 在RecyclerView上添加PoppyView
     *
     * @param parentView     RecyclerView的外部控件，同时也将是PoppyView的父控件
     * @param recyclerviewId RecyclerView的ResourceID
     * @param poppyViewResId PoppyView的ResourceID
     * @return
     */
    public View createNinjaViewOnRecyclerview(ViewGroup parentView, int recyclerviewId, int poppyViewResId) {
        mNinjaView = mLayoutInflater.inflate(poppyViewResId, null);
        final NotifyingRecyclerview recyclerview = (NotifyingRecyclerview) parentView.findViewById(recyclerviewId);
        initPoppyViewOnRecyclerview(recyclerview, parentView);
        return mNinjaView;
    }


    /**
     * 在ListView上添加PoppyView
     *
     * @param listViewId       ListView的ResourceID
     * @param poppyViewResId   PoppyView的ResourceID
     * @param onScrollListener ListView的滚动事件
     * @return
     */
    public View createPoppyViewOnListView(int listViewId, int poppyViewResId, OnScrollListener onScrollListener) {
        final AbsListView listView = (AbsListView) mActivity.findViewById(listViewId);
        mNinjaView = mLayoutInflater.inflate(poppyViewResId, null);
        initPoppyViewOnListView(listView, onScrollListener);
        return mNinjaView;
    }

    /**
     * 在ListView上添加PoppyView
     *
     * @param listViewId     ListView的ResourceID
     * @param poppyViewResId PoppyView的ResourceID
     * @return
     */
    public View createPoppyViewOnListView(int listViewId, int poppyViewResId) {
        return createPoppyViewOnListView(listViewId, poppyViewResId, null);
    }


    /**
     * 添加PoppyView
     *
     * @param view 需要绑定poppyView的控件
     */
    private void setPoppyViewOnView(View view) {
        LayoutParams lp = view.getLayoutParams();
        ViewParent parent = view.getParent();
        ViewGroup group = (ViewGroup) parent;
        int index = group.indexOfChild(view);
        final FrameLayout newContainer = new FrameLayout(mActivity);
        group.removeView(view);
        group.addView(newContainer, index, lp);
        newContainer.addView(view);
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = mViewPosition == ViewPosition.BOTTOM ? Gravity.BOTTOM : Gravity.TOP;
        newContainer.addView(mNinjaView, layoutParams);
        group.invalidate();
    }

    /**
     * 在RecyclerView的外部FrameLayout节点上面添加PoppyView
     *
     * @param viewGroup RecyclerView的外部FrameLayout
     */
    private void setPoppyViewOnRecyclerView(ViewGroup viewGroup) {
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = mViewPosition == ViewPosition.BOTTOM ? Gravity.BOTTOM : Gravity.TOP;
        viewGroup.addView(mNinjaView, layoutParams);
        viewGroup.invalidate();
    }

    /**
     * ScrollView滑动触发
     *
     * @param oldScrollPosition ScrollView滑动前的Y轴位置
     * @param newScrollPosition ScrollView滑动后的Y轴位置
     */
    private void onScrollPositionChanged(int oldScrollPosition, int newScrollPosition) {
        int newScrollDirection;

        if (newScrollPosition < oldScrollPosition) {
            newScrollDirection = SCROLL_TO_TOP;
        } else {
            newScrollDirection = SCROLL_TO_BOTTOM;
        }

        if (newScrollDirection != mScrollDirection) {
            mScrollDirection = newScrollDirection;
            translateYPoppyView();
        }
    }

    /**
     * RecyclerView滑动触发
     *
     * @param dy RecyclerView在Y轴的滑动距离
     */
    private void onRecyclerViewPositionChanged(int dy) {
        int newScrollDirection;
        if (dy < 0) {
            newScrollDirection = SCROLL_TO_TOP;
        } else {
            newScrollDirection = SCROLL_TO_BOTTOM;
        }

        if (newScrollDirection != mScrollDirection) {
            mScrollDirection = newScrollDirection;
            translateYPoppyView();
        }
    }

    /**
     * PoppyView的隐藏和显示动画
     */
    private void translateYPoppyView() {
        mNinjaView.post(new Runnable() {

            @Override
            public void run() {
                if (mPoppyViewHeight <= 0) {
                    mPoppyViewHeight = mNinjaView.getHeight();
                }

                int translationY = 0;
                switch (mViewPosition) {
                    case BOTTOM:
                        translationY = mScrollDirection == SCROLL_TO_TOP ? 0 : mPoppyViewHeight;
                        break;
                    case TOP:
                        translationY = mScrollDirection == SCROLL_TO_TOP ? -mPoppyViewHeight : 0;
                        break;
                }

                ViewPropertyAnimator.animate(mNinjaView).setDuration(300).translationY(translationY);
            }
        });
    }

    /**
     * 初始化ScrollView联动的PoppyView
     *
     * @param scrollView ScrollView
     */
    private void initPoppyViewOnScrollView(NotifyingScrollView scrollView) {
        setPoppyViewOnView(scrollView);
        scrollView.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener() {

            int mScrollPosition;

            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                if (Math.abs(t - mScrollPosition) >= SCROLL_DIRECTION_CHANGE_THRESHOLD) {
                    onScrollPositionChanged(mScrollPosition, t);
                }

                mScrollPosition = t;
            }
        });
    }

    /**
     * 初始化Recyclerview联动的PoppyView
     *
     * @param recyclerview Recyclerview
     * @param viewGroup    Recyclerview的外部FrameLayout节点
     */
    private void initPoppyViewOnRecyclerview(NotifyingRecyclerview recyclerview, ViewGroup viewGroup) {
        setPoppyViewOnRecyclerView(viewGroup);
        recyclerview.setOnScrollChangedListener(new NotifyingRecyclerview.OnScrollChangedListener() {

            int mScrollPosition;

            public void onScrollChanged(RecyclerView who, int dx, int dy) {
                if (Math.abs(dy - mScrollPosition) >= SCROLL_DIRECTION_CHANGE_THRESHOLD) {
                    onRecyclerViewPositionChanged(dy);
                }

                mScrollPosition = dy;
            }
        });
    }

    /**
     * 初始化ListView联动的PoppyView
     *
     * @param listView         ListView
     * @param onScrollListener ListView的滚动处理事件
     */
    private void initPoppyViewOnListView(AbsListView listView, final OnScrollListener onScrollListener) {
        setPoppyViewOnView(listView);
        listView.setOnScrollListener(new OnScrollListener() {

            int mScrollPosition;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollListener != null) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                View topChild = view.getChildAt(0);

                int newScrollPosition = 0;
                if (topChild == null) {
                    newScrollPosition = 0;
                } else {
                    newScrollPosition = -topChild.getTop() + view.getFirstVisiblePosition() * topChild.getHeight();
                }

                if (Math.abs(newScrollPosition - mScrollPosition) >= SCROLL_DIRECTION_CHANGE_THRESHOLD) {
                    onScrollPositionChanged(mScrollPosition, newScrollPosition);
                }

                mScrollPosition = newScrollPosition;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }
        });
    }
}
