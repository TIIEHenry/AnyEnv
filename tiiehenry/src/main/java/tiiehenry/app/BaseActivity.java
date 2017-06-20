package tiiehenry.app;
import android.app.Activity;
import android.view.View.*;
import android.view.View;
import android.os.Bundle;

public abstract class BaseActivity extends Activity implements OnClickListener,OnLongClickListener
{

  @Override
  protected void onCreate(Bundle s) {
	
	super.onCreate(s);
  }

  @Override
  public abstract void onClick(View p1)

  @Override
  public abstract boolean onLongClick(View p1) 

  
}
