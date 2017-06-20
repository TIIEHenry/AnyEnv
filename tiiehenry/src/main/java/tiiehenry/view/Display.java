package tiiehenry.view;
import android.content.Context;
import android.util.TypedValue;
import android.util.DisplayMetrics;

public class Display{

  private DisplayMetrics dm;
  
  public Display(Context c) {
	dm = c.getResources().getDisplayMetrics();
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
