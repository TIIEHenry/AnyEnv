package tiiehenry.io;

public class FileExistsException extends Exception
{
  public FileExistsException(){
	super("FileExists");
  }
  
  public FileExistsException(String path){
	super("FileExists Path:"+path);
  }
}
