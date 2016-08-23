package highwin.zgs.drawerlayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * User: zgsHighwin
 * Email: 799174081@qq.com Or 799174081@gmail.com
 * Description: User ViewDragHelper to achive Drawer
 * Create-Time: 2016/8/22 11:49
 */
public class DrawerLayout extends FrameLayout {

    public static final int CLOSE = 0X000001;
    public static final int OPEN = 0X000002;
    public static final int DRAGGING = 0X000003;

    private ViewGroup mLeftView;
    private ViewGroup mMainView;
    private ViewDragHelper mHelper;
    private int widthPixels;
    private int mWidth;
    private int mHeight;
    private int mRange;

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

    /**
     * DrawerLayout Three Status
     * 设置DrawerLayout的三种状态关闭，打开，拖拽
     */
    @IntDef({CLOSE, OPEN, DRAGGING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawerLayoutStatus {
    }

    @DrawerLayout.DrawerLayoutStatus
    int mStatus;

    @DrawerLayout.DrawerLayoutStatus
    public int getStatus() {
        return mStatus;
    }

    public void setStatus(@DrawerLayout.DrawerLayoutStatus int mStatus) {
        this.mStatus = mStatus;
    }

    public interface OnDraggedStatusListener {
        void drawerClose(); //DrawerLayout关闭的时候｜while drawer is close

        void drawerOpen();  //DrawerLayout打开的时候 ｜ while drawer is open

        void drawerDragging(float percent); //DrawerLayout正在拖动的时候｜while drawer is dragging
    }

    private OnDraggedStatusListener mOnDraggedStatusListener;

    /**
     * set DrawerLayout dragger status listener
     * 设置监听事件
     * @param onDraggedStatusListener
     */
    public void setOnDraggedStatusListener(DrawerLayout.OnDraggedStatusListener onDraggedStatusListener) {
        mOnDraggedStatusListener = onDraggedStatusListener;
    }

    /**
     * Finalize inflating a view from XML
     * 当布局完成的时候加载时候调用，主要用于获取view的实例化操作
     */
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


    /**
     * This is called during layout when the size of this view has changed
     * 当布局发生改变的时候调用，主要用于获取view的宽高
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Resources resources = getResources();
        //dont't user this;
      /*  DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;*/

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRange = (int) (mWidth * 0.6);
    }

    /**
     * ViewDragHelper CallBack
     * ViewDragHelper的一些重要的回调事件
     */
    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        /**
         * child  Decide whether the current view can be dragged
         * 接触到view的时候调用的方法
         * @param child     接触到的具体的view|current capture view
         * @param pointerId 多点触控的id|Distinguish multi-touch id
         * @return 返回值true表示能拖动，false不能拖动｜true can drag,false otherwise
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
         * view start move,not real to move，
         * 这个方法可以设置view手动的范围
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            //left = child.getLeft + dx

            //限制mMainView的范围｜limit the range of mMainView
            if (child == mMainView) {
                left = fixLeft(left);
            }
            return left;
        }


        /**
         * not real move range
         * 实际上不是范围，只是
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return (int) mRange;
        }


        /**
         * when changing the position of the view,(Update, animation, repainted)
         * 当view的位置改变的时候调用，可以进行一些更新，动画，重绘的一些工作
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            int mMainViewLeft = mMainView.getLeft();
            int newLeft = mMainViewLeft + dx;
            newLeft = fixLeft(newLeft);

            if (changedView == mLeftView) {
                mLeftView.layout(0, 0, mWidth, mHeight);
                mMainView.layout(newLeft, 0, newLeft + mWidth, mHeight);
            }

            dragEventAnimation(newLeft);

            // Compatible low version
            // ViewDragHelper using offsetLeftAndRight (int offset) to drag,In high version,which contains refresh method,
            // In the low version,without having refresh method,so we need to write invalidate() to compatible low version
            invalidate();
        }

        /**
         * the view was released
         * 当view释放的时候调用
         * @param releasedChild
         * @param xvel          horizontal speed + right,- left
         * @param yvel          vertical speed + down,-up
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel == 0 && mMainView.getLeft() > mRange / 2.0f) {
                open();
            } else if (xvel > 0) {
                open();
            } else {
                close();
            }
        }
    }


    /**
     * some animation while dragger view
     * view的一些动画，包括绽放，平衡，alpha
     * @param newLeft
     */
    private void dragEventAnimation(int newLeft) {
        float percent = (float) (newLeft * 1.0f / mRange);
        Log.d("DrawerLayout", "percent:" + percent);
        if (mOnDraggedStatusListener != null) {
            mOnDraggedStatusListener.drawerDragging(percent);
        }
        int lastStatus = mStatus;
        Log.d("DrawerLayout", "lastStatus:" + lastStatus);
        mStatus = getDragStatus(newLeft);
        Log.d("DrawerLayout", "mStatus:" + mStatus);
        Log.d("DrawerLayout", "\tnewLeft\t" + newLeft + "\tmRange\t" + mRange + "newLeft == mRange:" + (newLeft == mRange));
        if (lastStatus != mStatus) {
            if (mOnDraggedStatusListener != null) {
                if (mStatus == DrawerLayout.CLOSE) {
                    mOnDraggedStatusListener.drawerClose();
                } else if (mStatus == DrawerLayout.OPEN) {
                    mOnDraggedStatusListener.drawerOpen();
                }
            }
        }
        ViewAnimations(percent);
    }

    /**
     * get current drawerlayout status
     * 获取当前drawerlayout的状态
     * @param newLeft
     * @return
     */
    private int getDragStatus(int newLeft) {
        if (newLeft == 0) {
            return DrawerLayout.CLOSE;
        } else if (newLeft == mRange) {
            return DrawerLayout.OPEN;
        } else {
            return DrawerLayout.DRAGGING;
        }
    }


    /**
     * view animation ,user ViewHelper in order to  Compatible low version
     * 使用ViewHelper主要是为了兼容低的版本（3.0以下）
     * @param percent Percentage from mMainView.getLeft to target position
     */
    private void ViewAnimations(float percent) {
        //set mLeftView scale
        ViewHelper.setScaleX(mLeftView, evaluate(percent, 0.8, 1.0f));
        ViewHelper.setScaleY(mLeftView, evaluate(percent, 0.8, 1.0f));

        //set mLeftView translation
        ViewHelper.setTranslationX(mLeftView, evaluate(percent, -mWidth / 2, 0));

        //set mLeftView alpha
        ViewHelper.setAlpha(mLeftView, evaluate(percent, 0.5f, 1f));

        //set mMainView scale
        ViewHelper.setScaleX(mMainView, evaluate(percent, 1.0f, 0.8f));
        ViewHelper.setScaleY(mMainView, evaluate(percent, 1.0f, 0.8f));

        //set background color gradiant
        getBackground().setColorFilter((int) evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    /**
     * set value gradiant from start to end by fraction
     * 设置数值渐变
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    private Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * set color gradient
     * 设置颜色渐变
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    private Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }


    /**
     * set smooth move default if this method called
     * 关闭左边的view
     */
    public void close() {
        close(true);
    }

    /**
     * close leftView
     *  关闭左边的view
     * @param isSmooth ture表示平没移动｜true make view move smoothly,false otherwise
     */
    private void close(boolean isSmooth) {
        if (isSmooth) {
            boolean smoothSlideViewTo = mHelper.smoothSlideViewTo(mMainView, 0, 0);//true has not yet finished,false otherwhise
            if (smoothSlideViewTo) {
                //need to refreshe UI
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainView.layout(0, 0, mWidth, mHeight);
        }
    }

    /**
     * set smooth move default if this method called
     */
    public void open() {
        open(true);
    }

    /**
     * open leftView
     * 打开左边的视图
     * @param isSmooth true make view move smoothly,false otherwise
     */
    public void open(boolean isSmooth) {
        if (isSmooth) {
            boolean smoothSlideViewTo = mHelper.smoothSlideViewTo(mMainView, (int) mRange, 0);//true has not yet finished,false otherwhise
            if (smoothSlideViewTo) {
                //need to refresh UI
                ViewCompat.postInvalidateOnAnimation(this);//must be a ViewGroup

            }
        } else {
            mMainView.layout((int) mRange, 0, (int) (mRange + mWidth), mHeight);
        }
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mHelper.continueSettling(true)) {
            //返回true表示视图还没有移动到一个指定的位置
            //return true mean we need refresh ui
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    /**
     * Limit the movement range of views
     * 修改移动的位置，不让view超出边界
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
