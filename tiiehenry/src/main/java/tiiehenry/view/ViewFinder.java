package tiiehenry.view;

import android.app.*;
import android.view.*;

public class ViewFinder {
  
  private Activity activity=null;
  private ViewGroup viewGroup=null;
  private Dialog dialog=null;
  
  private int mode=1234567890;
  
  private static final int TYPE_ACTIVITY=0;
  private static final int TYPE_VIEWGROUP=1;
  private static final int TYPE_DIALOG=-1;

  public ViewFinder(Activity activity) {
	checkNull(activity);
	this.activity = activity;
	mode = TYPE_ACTIVITY;
  }
  public ViewFinder(ViewGroup viewGroup) {
	checkNull(viewGroup);
	this.viewGroup = viewGroup;
	mode = TYPE_VIEWGROUP;
  }
  public ViewFinder(Dialog dialog) {
	checkNull(dialog);
	this.dialog = dialog;
	mode = TYPE_DIALOG;
  }

  private void checkNull(Object o) {
	if (o == null) {
	  throw new RuntimeException("new ViewFinder(null)");
	}
  }

  public <T extends View> T get(int id) {
	switch (mode) {
	  case TYPE_ACTIVITY:
		return (T) activity.findViewById(id);
	  case TYPE_VIEWGROUP:
		return (T) viewGroup.findViewById(id);
	  case TYPE_DIALOG:
		return (T) dialog.findViewById(id);
	}
	return null;
  }
  
  public static <T extends View> T get(Activity activity, int id) {
	return (T) activity.findViewById(id);
  }
  public static <T extends View> T get(ViewGroup viewaGroup, int id) {
	return (T) viewaGroup.findViewById(id);
  }
  public static <T extends View> T get(Dialog dialog, int id) {
	return (T) dialog.findViewById(id);
  }
}
