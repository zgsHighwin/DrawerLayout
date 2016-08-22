package highwin.zgs.drawerlayout;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * User: zgsHighwin
 * Email: 799174081@qq.com Or 799174081@gmail.com
 * Description: User ViewDragHelper to achive Drawer
 * Create-Time: 2016/8/22 11:49
 */
public class DrawerLayout extends FrameLayout {

    private ViewGroup mLeftView;
    private ViewGroup mMainView;
    private ViewDragHelper mHelper;
    private int widthPixels;
    private int mWidth;
    private int mHeight;
    private double mRange;

    public DrawerLayout(Context context) {
        this(context, null);
    }


    public DrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHelper = ViewDragHelper.create(this, new ViewDragHelperCallBack());
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) {
            throw new IllegalStateException("you must have two child view at least");
        }

        mLeftView = (ViewGroup) getChildAt(0);
        mMainView = (ViewGroup) getChildAt(1);

        if (!(mLeftView instanceof ViewGroup && mMainView instanceof ViewGroup)) {
            throw new IllegalArgumentException("child view must be viewgroup");
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;
        mRange = mWidth * 0.6;
    }

    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        /**
         * child  Decide whether the current view can be dragged
         * @param child     current capture view
         * @param pointerId Distinguish multi-touch id
         * @return true can drag,false otherwise
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         * 如果tryCaptureView这个方法返回false,刚这个方法不会被执行
         * if tryCaptureView return false,this method will not be called
         *
         * @param capturedChild
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }


        /**
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            /**
             * left = child.getLeft + dx
             */

            if (child == mMainView) {
                left = fixLeft(left);
            }
            return left;
        }


        @Override
        public int getViewHorizontalDragRange(View child) {
            return (int) mRange;
        }


        /**
         * when changing the position of the view,(Update, animation, repainted)
         *
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            if (changedView == mLeftView) {
                mLeftView.layout(0, 0, mWidth, mHeight);
                int mMainViewLeft = mMainView.getLeft();
                int newLeft = mMainViewLeft + dx;
                newLeft = fixLeft(newLeft);
                mMainView.layout(newLeft, 0, newLeft + mWidth, mHeight);
            }

            // Compatible low version
            // ViewDragHelper using offsetLeftAndRight (int offset) to drag,In high version,which contains refresh method,
            // In the low version,without having refresh method,so we need to write invalidate() to compatible low version
            invalidate();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }
    }

    /**
     * Limit the movement range of views
     *
     * @param left
     * @return
     */
    private int fixLeft(int left) {
        if (left < 0) {
            left = 0;
        } else if (left > mRange) {
            left = (int) mRange;
        }
        return left;
    }

}
