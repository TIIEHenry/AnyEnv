package tiiehenry.view;
import android.graphics.*;
import android.util.*;
import android.view.*;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

public class Display {

  private DisplayMetrics dm;
  private WindowManager wm;
  private Context c;
  public Display(Context c) {
	wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
	dm = c.getResources().getDisplayMetrics();
	this.c = c;
  }
  public int getWidth() {
	return wm.getDefaultDisplay().getWidth();
  }
  public int getHeight() {
	return wm.getDefaultDisplay().getHeight();
  }

  public float dp2px(float dpVal) {
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, dm);
  }

  public float sp2px(float spVal) {
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, dm);
  }
  public float px2dp(float pxVal) {
	final float scale = dm.density;
	return (pxVal / scale);
  }
  public float px2sp(float pxVal) {
	return (pxVal / dm.scaledDensity);
  }
  /**
   * 获得屏幕宽度
   * 
   */
  public int getScreenWidth() {
	DisplayMetrics outMetrics = new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(outMetrics);
	int width = outMetrics.widthPixels;
	return width;
  }

  /**
   * 获得屏幕高度
   */
  public int getScreenHeight() {
	DisplayMetrics outMetrics = new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(outMetrics);
	int height = outMetrics.heightPixels;
	return height;
  }

  /**
   * 获得状态栏的高度
   */
  public int getStatusBarHeight() {
	int statusHeight = -1;
	try {
	  Class<?> clazz = Class.forName("com.android.internal.R$dimen");
	  Object object = clazz.newInstance();
	  int height = Integer.parseInt(clazz.getField("status_bar_height")
									.get(object).toString());
	  statusHeight = c.getResources().getDimensionPixelSize(height);
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return statusHeight;
  }

  /**
   * 精确获取屏幕尺寸（例如：3.5、4.0、5.0寸屏幕）(貌似不太准)
   * 
   * @param ctx
   * @return
   */
  public double getScreenPhysicalSize() {
	DisplayMetrics dm = new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(dm);
	double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2)
									  + Math.pow(dm.heightPixels, 2));
	return diagonalPixels / (160 * dm.density);
  }

  /**
   * 一般是7寸以上是平板 ,判断是否是平板（官方用法）
   * 
   * @param context
   * @return
   */
  public boolean isTablet() {
	return (c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
  }


  /**
   * 获取屏幕分辨率
   *
   * @return
   */
  public String getScreenRatio() {
	DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
	return displayMetrics.widthPixels + "X" + displayMetrics.heightPixels;
  }

  /**
   * 获取屏幕密度
   *
   * @return
   */
  public String getScreenDensity() {
	DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
	return displayMetrics.densityDpi + "DPI";
  }
  
  /**
   * 获取当前屏幕截图
   */
  public static Bitmap snapShot(Activity activity, boolean withStatusBar) {
	View view = activity.getWindow().getDecorView();
	view.setDrawingCacheEnabled(true);
	view.buildDrawingCache();
	Bitmap bmp = view.getDrawingCache();

	Display display=new Display(activity);
	int width = display.getScreenWidth();
	int height = display.getScreenHeight();
	Bitmap bp = null;
	if (withStatusBar) {
	  Rect frame = new Rect();
	  activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	  height -= frame.top;
	}
	bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
	view.destroyDrawingCache();
	return bp;
  }

}
