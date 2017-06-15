package tiiehenry.io;

public class FileExistsException extends Exception
{
  public FileExistsException(){
	super();
  }
  
  public FileExistsException(String path){
	super("Path:"+path);
  }
}
