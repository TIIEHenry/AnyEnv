package tiiehenry.app;
import android.app.Application;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import java.util.ArrayList;

public abstract class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks{
  public ArrayList<Activity> activityList=new ArrayList<>();

  @Override
  public void onCreate() {
	registerActivityLifecycleCallbacks(this);
	super.onCreate();
  }

  public void exit() {
	preExit();
	for (Activity activity:activityList) {
	  activity.finish();
	}
  }
  public abstract void preExit();
  
  @Override
  public void onActivityCreated(Activity activity, Bundle p2) {
	activityList.add(activity);
  }
  @Override
  public void onActivityStarted(Activity p1) {
	// TODO: Implement this method
  }
  @Override
  public void onActivityResumed(Activity p1) {
	// TODO: Implement this method
  }
  @Override
  public void onActivityPaused(Activity p1) {
	// TODO: Implement this method
  }
  @Override
  public void onActivityStopped(Activity p1) {
	// TODO: Implement this method
  }
  @Override
  public void onActivitySaveInstanceState(Activity p1, Bundle p2) {
	// TODO: Implement this method
  }
  @Override
  public void onActivityDestroyed(Activity activity) {
	activity.finish();
	activityList.remove(activity);
  }
  
}
