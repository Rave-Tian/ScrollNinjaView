package com.rave.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Rave
 */
public class NotifyingRecyclerview extends RecyclerView {

    /**
     * @author Rave
     */
    public interface OnScrollChangedListener {
        void onScrollChanged(RecyclerView view, int dx, int dy);
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public NotifyingRecyclerview(Context context) {
        super(context);
    }

    public NotifyingRecyclerview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyingRecyclerview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, dx, dy);
        }
    }


    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

}