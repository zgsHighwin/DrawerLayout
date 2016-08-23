package highwin.zgs.drawerlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取到控件|get view
        final ImageView mIv = (ImageView) findViewById(getResources().getIdentifier("iv", "id", getPackageName()));
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(getResources().getIdentifier("drawer_layout", "id", getPackageName()));
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
    }
}
