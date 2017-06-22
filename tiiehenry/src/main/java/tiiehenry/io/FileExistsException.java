package tiiehenry.io;
import java.io.*;

public class FileExistsException extends IOException
{
  public FileExistsException(){
	super("FileExists");
  }
  
  public FileExistsException(String path){
	super("FileExists Path:"+path);
  }
  public FileExistsException(File f){
	this(f.toString());
  }
}
