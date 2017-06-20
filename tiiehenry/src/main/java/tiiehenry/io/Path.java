package tiiehenry.io;
import java.io.*;
import java.net.URI;
import android.support.annotation.NonNull;

//getter Path return String
/**
 * File的扩展类
 * Directory 全部简写为Dir
 * absolute 和 parent 的 get 省略
 */
public class Path extends File {
  
  public static String DEF_ENCODING="GBK";
  public static int DEF_BYTESIZE=4096;

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

  public Path(File f) {
	super(f.toString());
  }
  
  public Path parent() {
	return new Path(getParent());
  }
  
  public String parentPath() {
	return getParent();
  }

  public Path absolute() {
	return new Path(getAbsolutePath());
  }
  
  public String absolutePath() {
	return getAbsolutePath();
  }


  public FileInputStream getInputStream() throws FileNotFoundException {
	return new FileInputStream(this);
  }
  public FileOutputStream getOutputStream() throws FileNotFoundException {
	return new FileOutputStream(this);
  }

  public boolean copyTo(File path)throws FileExistsException, IOException {
	if (path.exists()) {
	  throw new FileExistsException(path.toString());
	}
	if (!(exists() && canRead() && path.canWrite()))
	  return false;
	copyStream(getInputStream(), new FileOutputStream(path));
	return true;
  }

  public boolean writeString(String s){
	try {
	  FileOutputStream fos = new FileOutputStream(this);
	  fos.write(s.getBytes());
	  fos.close();
	  return true;
	} catch (IOException|FileNotFoundException e) {
	  return false;
	}  
  }

  public String readString() throws IOException {
	return readString(DEF_ENCODING);
  }

  public String readString(String encoding) throws IOException {
	return readStreamString(getInputStream(),encoding);
  }

  @NonNull
  public static void copyStream(InputStream i, OutputStream o) throws IOException {
	copyStream(i, o, DEF_BYTESIZE);
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
  public static String readStreamString(InputStream i) throws UnsupportedEncodingException, IOException {
	return readStreamString(i, DEF_ENCODING);
  }

  @NonNull
  public static String readStreamString(InputStream i, String encoding) throws UnsupportedEncodingException, IOException {
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
