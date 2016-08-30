package highwin.zgs.drawerlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.nineoldandroids.view.ViewHelper;

import highwin.zgs.drawerlayout.utils.ToastUtils;
import highwin.zgs.drawerlayout.view.DrawerLayout;
import highwin.zgs.drawerlayout.view.InterceptRelativeLayout;

public class MainActivity extends AppCompatActivity {

    private int mLastMode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取到控件|get view
        final ImageView mIv = (ImageView) findViewById(getResources().getIdentifier("iv", "id", getPackageName()));
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(getResources().getIdentifier("drawer_layout", "id", getPackageName()));
        InterceptRelativeLayout interceptRelativeLayout = (InterceptRelativeLayout) findViewById(getResources().getIdentifier("main_layout", "id", getPackageName()));
        //设置监听|set listener
        drawerLayout.setOnDraggedStatusListener(new DrawerLayout.OnDraggedStatusListener() {
            @Override
            public void drawerClose() {
                ToastUtils.showToast(getApplicationContext(), "drawerClose");
            }

            @Override
            public void drawerOpen() {
                ToastUtils.showToast(getApplicationContext(), "drawerOpen");
            }

            @Override
            public void drawerDragging(float percent) {
                ViewHelper.setAlpha(mIv, 1 - percent);
                //    ToastUtils.showToast(getApplicationContext(), "drawerDragging");
            }
        });
        //设置是否拦截|set view intercept
        interceptRelativeLayout.setDrawerLayout(drawerLayout, true);


/*
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLastMode == -1) {
                    drawerLayout.setMode(DrawerLayout.DrawerLayoutStatus.MODE_TRANSLATE);
                    mLastMode = 0;
                } else if (mLastMode == 0) {
                    drawerLayout.setMode(DrawerLayout.DrawerLayoutStatus.MODE_SCALE);
                    mLastMode = 1;
                } else {
                    drawerLayout.setMode(DrawerLayout.DrawerLayoutStatus.MODE_TRANSLATE);
                    mLastMode = 0;
                }
            }
        });*/

        ViewGroup leftView = drawerLayout.getLeftView();
        if (leftView != null) {
            if (leftView instanceof ScrollView) {
                leftView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
            }
        }
    }
}