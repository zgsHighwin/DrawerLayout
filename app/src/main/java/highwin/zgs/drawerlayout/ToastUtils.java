package highwin.zgs.drawerlayout;

import android.content.Context;
import android.widget.Toast;

/**
 * User: zgsHighwin
 * Email: 799174081@qq.com Or 799174081@gmail.com
 * Description: 让toast不会待一个一个弹出,单例模式的toast｜to make toast not to wait
 * Create-Time: 2016/8/23 10:50
 */
public class ToastUtils {

    public static Toast mToast;

    public static void showToast(Context mContext, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
}