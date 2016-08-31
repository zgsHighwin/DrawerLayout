package highwin.zgs.library.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * User: zgsHighwin
 * Email: 799174081@qq.com Or 799174081@gmail.com
 * Description: 去拦截事件｜in order to intercept events
 * Create-Time: 2016/8/23 11:24
 */
public class InterceptRelativeLayout extends RelativeLayout {

    private DrawerLayout mDrawerLayout;
    private boolean isIntercept;

    public InterceptRelativeLayout(Context context) {
        super(context);
    }

    public InterceptRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param drawerLayout
     * @param isIntercept  true表示拦截事件，false表示不拦截事件，true intercept,false otherwise
     */
    public void setDrawerLayout(DrawerLayout drawerLayout, boolean isIntercept) {
        this.mDrawerLayout = drawerLayout;
        this.isIntercept = isIntercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int status = mDrawerLayout.getStatus();
        if (isIntercept) {
            if (status == DrawerLayout.DrawerLayoutStatus.CLOSE) {
                return super.onInterceptTouchEvent(ev);
            } else {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int status = mDrawerLayout.getStatus();
        if (isIntercept) {
            if (status == DrawerLayout.DrawerLayoutStatus.CLOSE) {
                return super.onTouchEvent(event);
            } else {
                //Compatible low version
                int actionMasked = MotionEventCompat.getActionMasked(event);
                if (actionMasked == MotionEvent.ACTION_UP) {
                    mDrawerLayout.close();
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }
}
