package tiiehenry.io;
import java.io.*;
import java.net.URI;
import android.support.annotation.NonNull;

public class Path extends File {
  public Path(String pathname) {
	super(pathname);
  }

  public Path(String parent, String child) {
	super(parent, child);
  }

  public Path(File parent, String child) {
	super(parent, child);
  }

  public Path(URI uri) {
	super(uri);
  }

  public FileInputStream getStream() throws FileNotFoundException {
	return new FileInputStream(this);
  }

  
  public boolean copyTo(Path path) throws FileExistsException {
	if (path.exists())
	  throw new FileExistsException(path.toString());
	if (!exists()||!canRead()||!path.canWrite())
	  return false;
	return true;
  }
  public boolean writeString(String s) {  
	try {
	  FileOutputStream fos = new FileOutputStream(this);
	  fos.write(s.getBytes());  
	  fos.close();  
	  return true;
	} catch (IOException|FileNotFoundException e) {
	  return false;
	}  
  }

  @NonNull
  public static void copyStream(InputStream i, OutputStream o) throws IOException {
	copyStream(i, o, 4096);
  }

  @NonNull
  public static void copyStream(InputStream i, OutputStream o, int byteSize) throws IOException {
	byte[] buffer=new byte[byteSize];
	int s;
	while ((s = i.read(buffer)) != -1) {
	  o.write(buffer, 0, s);
	}
  }

  @NonNull
  public static String getStreamString(InputStream i) throws UnsupportedEncodingException, IOException {
	return getStreamString(i, "GBK");
  }

  @NonNull
  public static String getStreamString(InputStream i, String encoding) throws UnsupportedEncodingException, IOException {
	InputStreamReader read = new InputStreamReader(i, encoding);//考虑到编码格式
	BufferedReader bufferedReader = new BufferedReader(read);
	String lineTxt = null;
	String o=bufferedReader.readLine();
	while ((lineTxt = bufferedReader.readLine()) != null) {
	  o = o + "\n" + lineTxt;
	}
	read.close();
	return o;
  }
}
