package tiiehenry.io;
import java.io.*;
import java.util.*;

import android.support.annotation.NonNull;
import android.util.ArrayMap;
import java.net.URI;
import java.text.DecimalFormat;

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
	return parent(1);
  }
  public Path parent(int level) {
	File f=this;
	for (int i=0;i < level;i++) {
	  f = this.getParentFile();
	}
	return new Path(f);
  }

  public String parentPath() {
	return getParent();
  }
  public String parentPath(int level) {
	return parent(level).toString();
  }

  public Path child(String name) {
	return new Path(this, name);
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

  //like 2.1M
  public String getFormatedSize() {
	return getFormatedSize(length());
  }

  public boolean delete() {
	if (isDirectory()) {
	  deleteDir(this);
	} else
	if (isFile()) {
	  return super.delete();
	}
	return !exists();
  }

  public boolean copyTo(String path, boolean recover)throws FileExistsException, IOException {
	return copyTo(new File(path), recover);
  }
  public boolean copyTo(File path, boolean recover)throws FileExistsException, IOException {
	if (isFile()) {
	  if (path.exists()) 
		throw new FileExistsException(path.toString());
	  if (!canRead())
		throw new IOException("Couldn't read file:" + path.getParent());
	  path.getParentFile().mkdirs();
	  if ((!path.exists()) || recover) {
		InputStream in=getInputStream();
		OutputStream out=new FileOutputStream(path);
		copyStream(in, out);
		in.close();
		out.close();
	  }
	} else if (isDirectory()) {
	  path.mkdirs();
	  ArrayMap<String, File> map=getDirMap();
	  Iterator iter = map.entrySet().iterator();
	  while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
		String name = (String) entry.getKey();
		File sourceFile=(File) entry.getValue();
		new Path(sourceFile).copyTo(path.getPath() + separator + name, recover);
	  }
	}
	return true;
  }

  public boolean writeString(String s) throws IOException {
	return writeString(s, false);
  }
  public boolean writeString(String s, boolean append) throws IOException {
	BufferedWriter bw=null;
	try {
	  bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this, append)));
	  bw.write(s);
	} finally {
	  if (bw != null)
		bw.close();
	}
	return true;
  }

  public String readString() throws IOException {
	return readString(DEF_ENCODING);
  }

  public String readString(String encoding) throws IOException {
	return readStreamString(getInputStream(), encoding);
  }

  private void addFileToMap(ArrayMap<String,File> size, String namePerfix, File dir) {
	for (File f:dir.listFiles()) {
	  String fname=f.getName();
	  if (f.isFile()) {
		size.put(namePerfix + fname, f);
	  } else if (f.isDirectory()) {
		size.put(namePerfix + fname + "/", f);
		addFileToMap(size, namePerfix + fname + "/", f);
	  }
	}
  }

  //获取文件树 ("music/a.mp3",File)
  //("music/",File)
  public ArrayMap<String,File> getDirMap() {
	if (!isDirectory())
	  return null;
	ArrayMap<String,File> size=new ArrayMap<>();
	addFileToMap(size, "", this);
	return size;
  }
  public long getSize() {
	long size=0;
	if (isDirectory()) {
	  for (File f:listFiles()) {
		if (f.isDirectory()) {
		  size += new Path(f).getSize();
		} else if (f.isFile()) {
		  size += f.length();
		}
	  }
	  return size;
	} else if (isFile()) {
	  return length();
	}
	return 0;
  }

  public static void deleteDir(File dir) {
	for (File file : dir.listFiles()) {
	  if (file.isFile())
		file.delete();
	  // 删除所有文件 
	  else if (file.isDirectory()) 
		deleteDir(file);
	  // 递规的方式删除文件夹 
	} 
	dir.delete();
	// 删除目录本身 
  }
  @NonNull
  public static void copyStream(InputStream i, OutputStream o) throws IOException {
	copyStream(i, o, DEF_BYTESIZE);
  }

  @NonNull
  public static void copyStream(InputStream i, OutputStream o, int byteSize) throws IOException {
	byte[] buffer=new byte[byteSize];
	copyStream(i, o, buffer);
  }

  @NonNull
  public static void copyStream(InputStream i, OutputStream o, byte[] buffer) throws IOException {
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
  public static String readStreamString(InputStream i, String encoding) throws IOException {
	//考虑到编码格式
	BufferedReader bufferedReader=null;
	StringBuffer sb=sb = new StringBuffer();
	try {
	  bufferedReader = new BufferedReader(new InputStreamReader(i, encoding));
	  String lineTxt = null;
	  while ((lineTxt = bufferedReader.readLine()) != null) {
		sb.append(lineTxt);
	  }
	} finally {
	  if (bufferedReader != null)
		bufferedReader.close();
	}
	return sb.toString();
  }

  public static byte[] readStreamBytes(FileInputStream in) throws IOException {
	byte[] buffer = null;
	try {
	  int length = in.available();
	  buffer = new byte[length];
	  in.read(buffer);
	} finally {
	  if (in != null)
		in.close();
	}
	return buffer;
  }

  public static String getFormatedSize(long size) {
	DecimalFormat df = new DecimalFormat("#.00");
	String sizeizeString = "0K";
	if (size < 1000) {
	  sizeizeString = df.format((double) size) + "B";
	} else if (size < 1024000) {
	  sizeizeString = df.format((double) size / 1024) + "K";
	} else if (size < 1048576000) {
	  sizeizeString = df.format((double) size / 1048576) + "M";
	} else {
	  sizeizeString = df.format((double) size / 1073741824) + "G";
	}
	return sizeizeString;
  }
}
