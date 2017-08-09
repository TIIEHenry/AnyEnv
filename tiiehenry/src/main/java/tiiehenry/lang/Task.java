package tiiehenry.lang;
import android.os.Looper;

public class Task extends Thread {
  
  public static abstract class Actionable {
	public abstract void doInThread()
	public void callback(){}
  }

  public static Thread start(final Actionable actionable) {
	Thread t=new Thread(new Runnable(){
		public void run() {
		  actionable.doInThread();
		  Looper.prepare();
		  actionable.callback();
		  Looper.loop();
		}
	  });
	t.start();
	return t;
  }
}
