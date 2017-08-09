package tiiehenry.app;

import android.app.IntentService;
import android.content.Intent;

public class ActionService extends IntentService {

  public static abstract class Actionable {
	public abstract void doInThread(Intent intent)
	public void callback(){}
  }

  public static Actionable action;
  public ActionService(){
	super("action_service");
  }
  public ActionService(String s){
	super(s);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
	action.doInThread(intent);
  }

  @Override
  public void onDestroy() {
	super.onDestroy();
	action.callback();
  }

}
