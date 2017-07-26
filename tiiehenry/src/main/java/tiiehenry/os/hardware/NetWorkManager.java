package tiiehenry.os.hardware;

import android.content.*;
import android.net.*;

/**
 * 网络 工具类<Br>
 * 需要android.permission.ACCESS_NETWORK_STATE 权限
 */

public class NetWorkManager {

  private Context context;

  public NetWorkManager(Context c) {
	context = c;
  }
  /**"
   * 判断网络是否连接
   */
  public boolean isConnected() {
	boolean bisConnFlag = false;
	ConnectivityManager connectivityManager = (ConnectivityManager) context
	  .getSystemService(Context.CONNECTIVITY_SERVICE);

	NetworkInfo network = connectivityManager.getActiveNetworkInfo();
	if (network != null) {
	  bisConnFlag = connectivityManager.getActiveNetworkInfo().isAvailable();
	}
	return bisConnFlag;
  }

  /**
   * 判断是否是wifi连接
   */
  public boolean isWifi() {
	ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	if (!isConnected()) {
	  return false;
	}
	boolean isWifi = connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	return isWifi;
  }

  /**
   * 打开网络设置界面
   */
  public Intent getSettingActivityIntent() {
	return new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
  }


}
