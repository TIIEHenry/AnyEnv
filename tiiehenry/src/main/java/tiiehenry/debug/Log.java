package tiiehenry.debug;
import java.io.File;
import tiiehenry.io.Path;
import java.io.IOException;

public class Log {
  private static Path file;

  public static void w(String tAG, String p1) {
	e(tAG+p1);
  }

  public static void i(String tAG, String p1, String packageName, String p3, long b) {
	e(tAG+p1);
  }
  public static void init(File f) {
	file = new Path(f, "debug.txt");
	file.delete();
	try {
	  file.createNewFile();
	} catch (IOException e) {}
  }
  public static void e(String s, String s2, Object s3,String s4) {
	e(s2 +s4+ s3.toString());
  }
  public static void e(String s, String s2, Object s3) {
	e(s2 + s3.toString());
  }
  public static void e(String s) {
	p(s);
  }
  public static void i(String s) {
	e(s);
  }
  public static void w(String s){
	e(s);
  }
  public static void w(String s, Object s2, Object s3,Object s4) {
	e(s2.toString() +s4.toString()+ s3.toString());
  }
  public static void w(String s, Object s2, Object s3) {
	e(s2.toString()+ s3.toString());
  }
  public static void p(final String s){
	new Thread(){
	  public void run(){
		try {
		  file.writeString(s+"\n", true);
		} catch (IOException e) {}
	  }
	}.start();
  }
}
