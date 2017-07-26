package tiiehenry.lang;
import android.text.TextUtils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class string
{
  public static boolean isEmpty(CharSequence s){
	return TextUtils.isEmpty(s);
  }
  
  public static String replace(String s,String pattern,Callback c,int times){
	Pattern p=Pattern.compile(pattern);
	Matcher m=p.matcher(s);
	while(m.find()){
	  m.group();
	}
	return s;
  }
  

  public static interface Callback{
	public String onMatch(String s)
  }
}
