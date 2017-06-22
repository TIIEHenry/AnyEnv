package tiiehenry.view;
import android.content.Context;
import android.util.TypedValue;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Display {

  private DisplayMetrics dm;
  private WindowManager wm;

  public Display(Context c) {
	wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
	dm = c.getResources().getDisplayMetrics();
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

}
