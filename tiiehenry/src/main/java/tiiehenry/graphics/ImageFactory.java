package tiiehenry.graphics;
import java.io.*;

import android.graphics.Bitmap;
import tiiehenry.io.Path;

public class ImageFactory {

  private Bitmap bitmap;

  public ImageFactory(Bitmap bitmap) {
	this.bitmap = bitmap;
  }
  
  public void saveAs(Bitmap.CompressFormat type, Path file)throws IOException {
	saveAs(type, file, 100);
  }
  public void saveAs(Bitmap.CompressFormat type, Path file, int quality)throws IOException {
	FileOutputStream fos = null;
	try {
	  if (!file.parent().exists()) {
		file.getParentFile().mkdirs();
	  }
	  fos = new FileOutputStream(file);
	  bitmap.compress(type, quality, fos);
	  fos.flush();
	} finally {
	  if (fos != null) {
		fos.close();
	  }
	}
  }
  
}
