package tiiehenry.widget;
import android.content.Context;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.io.File;
import android.content.ComponentName;
import java.io.FileInputStream;
import android.os.Handler;
import java.io.FileOutputStream;
import android.content.ContentResolver;
import android.content.BroadcastReceiver;
import java.io.InputStream;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Looper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.view.Display;
import android.os.Bundle;
import android.content.res.Resources.Theme;
import android.view.View;

public class test {
  public void cois() {
	RecyclerAdapter a=new RecyclerAdapter<View>(null, new ArrayList()){

	  @Override
	  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO: Implement this method
		return new ViewHolder(new View(null)){

		  @Override
		  void onItemClick(View v, int pos) {
			// TODO: Implement this method
		  }

		  @Override
		  boolean onItemLongClick(View parent, View v, int pos) {
			// TODO: Implement this method
			return false;
		  }
		};
	  }

	  @Override
	  void bindData(ViewHolder holder, View data, int pos) {
		// TODO: Implement this method
	  }
	};
  }
}
