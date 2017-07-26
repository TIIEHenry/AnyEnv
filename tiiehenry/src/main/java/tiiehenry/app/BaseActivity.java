package tiiehenry.app;

import android.app.*;
import android.os.*;
import android.view.*;
import tiiehenry.view.*;

import android.annotation.TargetApi;
import android.widget.FrameLayout;
import tiiehenry.view.Display;
import android.widget.Toast;

//这是个模板
public abstract class BaseActivity extends Activity implements View.OnClickListener, Handler.Callback {

  public Handler handler;
  public Application app;
  public ViewFinder viewFinder;
  public Display display;
  
  @Override
  protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	handler = new Handler(this);
	app = getApplication();
	viewFinder = new ViewFinder(this);
	display = new Display(this);
	
	/*init();
	 initData();
	 initView();
	 initListener();*/
  }

  protected abstract void init()
  protected abstract void initData();
  protected abstract void initView()
  protected abstract void initListener();
  public abstract void onClick(View v) 

  public <T extends View> T getView(int id) {
	return (T) findViewById(id);
  }
  public FrameLayout getRootView() {
	return (FrameLayout) getWindow().getDecorView();
  }
  public View getContentView() {
	return findViewById(android.R.id.content);
  }
  public int getWidth() {
	return display.getWidth();
  }
  public int getHeight() {
	return display.getHeight();
  }
  
  @TargetApi(19)
  public void setTranslucentStatus(boolean on) {
	Window win = getWindow();
	WindowManager.LayoutParams winParams = win.getAttributes();
	final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
	if (on) {
	  winParams.flags |= bits;
	} else {
	  winParams.flags &= ~bits;
	}
	win.setAttributes(winParams);
  }
  //获取标题栏高度
  public int getTitleBarHeight() {
	int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	int titleBarHeight = contentTop - getStatusBarHeight();
	return titleBarHeight;
  }
  public int getStatusBarHeight() {
	return display.getStatusBarHeight();
  }

  public boolean handleMessage(Message msg) {
	return false;
  }
  public void toast(Object o) {
	if (o == null) {
	  toast("null");
	} else {
	  Toast.makeText(this, o.toString(), 0).show();
	}
  }
  public void toastLong(Object o) {
	if (o == null) {
	  toast("null");
	} else {
	  Toast.makeText(this, o.toString(), 1).show();
	}
  }

}
