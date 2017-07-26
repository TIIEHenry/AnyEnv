package tiiehenry.os;

import android.os.*;

public class UIHandler extends Handler {
  
  public UIHandler() {
	super(Looper.getMainLooper());
  }
  
  public void run(Runnable runnable) {
	post(runnable);
  }

  public void runDelayed(Runnable runnable, long delayMillis) {
	postDelayed(runnable, delayMillis);
  }
  
}
