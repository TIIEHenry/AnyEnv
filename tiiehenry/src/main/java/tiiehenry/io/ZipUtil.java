package tiiehenry.io;


import java.io.*;
import java.util.*;
import java.util.zip.*;
import tiiehenry.io.*;

import android.util.ArrayMap;

public class ZipUtil {

  File zipFile;
  //4M
  byte[] buffer = new byte[4096 * 1024]; 
  int level=Deflater.DEFAULT_COMPRESSION;
  int method=ZipOutputStream.DEFLATED;
  String comment=""; 
  Checksum checksum=new Adler32();

  private ZipUtil.OnUpdateListener onUpdateListener;

  public ZipUtil(File zipFile) {
	this.zipFile = zipFile;
  }

  public ZipUtil setByteSize(int byteSize) {
	buffer = new byte[byteSize];
	return this;
  }
  public ZipUtil setComment(String comment) {
	this.comment = comment;
	return this;
  }
  public ZipUtil setMethod(int method) {
	this.method = method;
	return this;
  }
  public ZipUtil setLevel(int level) {
	this.level = level;
	return this;
  }
  public ZipUtil setChecksum(Checksum checksum) {
	this.checksum = checksum;
	return this;
  }
  public ZipUtil setOnUpdateListener(OnUpdateListener listener) {
	onUpdateListener = listener;
	return this;
  }

  public void zipDir(File dir, File zipFile) throws FileNotFoundException,FileExistsException, IOException {
	zip(new Path(dir).getDirMap(), zipFile);
  }

  public void zip(ArrayMap<String,File> files, File zipFile) throws FileNotFoundException, IOException, FileExistsException {
	if (zipFile.exists())
	  throw new FileExistsException(zipFile);
	if (!zipFile.exists() && !zipFile.getParentFile().mkdirs())
	  throw new IOException("can't mkdirs:" + zipFile.getParent());


	ZipOutputStream out =null;
	try {
	  FileOutputStream dest = new FileOutputStream(zipFile);
	  CheckedOutputStream cos = new CheckedOutputStream(dest, checksum);
	  out = new ZipOutputStream(new BufferedOutputStream(cos));
	  out.setMethod(method);
	  out.setLevel(level);

	  Iterator iter = files.entrySet().iterator();
	  while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
		String name = (String) entry.getKey();
		File file = (File) entry.getValue();
		addEntry(out, name, file);
	  }
	} finally {
	  if (out != null) {
		try {
		  out.closeEntry();
		} catch (IOException e) {
		  throw e;
		}
		try {
		  out.close();
		} catch (IOException e) {
		  throw e;
		}
	  }
	}
  }
  private void addEntry(ZipOutputStream out, String name, File file) throws IOException {
	FileInputStream fi= null;
	BufferedInputStream origin=null;
	try {
	  fi = new FileInputStream(file);
	  origin = new BufferedInputStream(fi, buffer.length);
	  if (file.isFile()) {
		out.putNextEntry(new ZipEntry(name));
		if (onUpdateListener != null)
		  onUpdateListener.onUpdate(name, file);

		int count;
		while ((count = origin.read(buffer, 0, buffer.length)) != -1) {
		  out.write(buffer, 0, count);
		}
	  } else if (file.isDirectory()) {
		name = name.endsWith("/") ? name: name + "/";
		out.putNextEntry(new ZipEntry(name));
	  }
	} finally {
	  if (origin != null) {
		try {
		  origin.close();
		} catch (IOException e) {
		  throw e;
		}
	  }
	}
  }

  //获取zip中单个文件的流
  public InputStream getInputStream(String name) throws IOException {
	ZipFile zf = new ZipFile(zipFile);  
	FileInputStream fis= new FileInputStream(zipFile);
	CheckedInputStream cos = new CheckedInputStream(fis, checksum);
	ZipInputStream zis = new ZipInputStream(new BufferedInputStream(cos));

	ZipEntry entry;
	while ((entry = zis.getNextEntry()) != null) {
	  String entryName=entry.getName();
	  if (entryName.equals(name)) {
		return zf.getInputStream(entry);
	  }
	}
	return null;
  }
  public ArrayList<String> getFileNames() throws FileNotFoundException, IOException {
	FileInputStream fis= new FileInputStream(zipFile);
	CheckedInputStream cos = new CheckedInputStream(fis, checksum);
	ZipInputStream zis = new ZipInputStream(new BufferedInputStream(cos));

	ArrayList<String> files=new ArrayList<>();
	ZipEntry entry;
	while ((entry = zis.getNextEntry()) != null) {
	  files.add(entry.getName());
	}
	zis.close();
	return files;
  }
  
  public void unZip(String name, File outFile) throws IOException {
	ArrayMap<String,File> files=new ArrayMap<>();
	files.put(name, outFile);
	unZip(files);
  }

  public void unZipAll(File outPath) throws FileNotFoundException, IOException {
	FileInputStream fis= new FileInputStream(zipFile);
	CheckedInputStream cos = new CheckedInputStream(fis, checksum);
	ZipInputStream zis = new ZipInputStream(new BufferedInputStream(cos));

	ArrayMap<String,File> files=new ArrayMap<>();
	ZipEntry entry;
	while ((entry = zis.getNextEntry()) != null) {
	  String entryName=entry.getName();
	  files.put(entryName, new File(outPath, entryName));
	}
	zis.close();
	unZip(files);
  }

  public void unZip(ArrayMap<String,File> files) throws IOException, FileExistsException {
	if (!zipFile.exists())
	  throw new FileNotFoundException(zipFile.toString());

	FileInputStream fis= new FileInputStream(zipFile);
	CheckedInputStream cos = new CheckedInputStream(fis, checksum);
	ZipInputStream zis = new ZipInputStream(new BufferedInputStream(cos));

	try {
	  unZip(zis, files);
	} finally {
	  if (zis != null) {
		try {
		  zis.close();
		} catch (IOException e) {
		  throw e;
		}
	  }
	}
  }

  private void unZip(ZipInputStream zis, ArrayMap<String,File> files) throws FileExistsException, IOException {
	ZipEntry entry;
	while ((entry = zis.getNextEntry()) != null) {
	  String entryName=entry.getName();
	  File outFile=files.get(entryName);
	  if (entry.isDirectory()) {
		outFile.mkdirs();
	  } else {
		if (outFile != null) {
		  if (outFile.exists())
			throw new FileExistsException(outFile);
		  if (!outFile.getParentFile().exists() && !outFile.getParentFile().mkdirs())
			throw new IOException("can't mkdirs:" + outFile.getParent());
		  if (onUpdateListener != null)
			onUpdateListener.onUpdate(entryName, outFile);

		  BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(outFile), buffer.length);
		  int count;
		  while ((count = zis.read(buffer, 0, buffer.length)) != -1) {
			dest.write(buffer, 0, count);
		  }
		  dest.flush();
		  dest.close();
		}
	  }
	}
  }

  public void append(String appendFilePath) throws Exception { 
	ZipFile war = new ZipFile(zipFile);
	ZipOutputStream append = new ZipOutputStream(new FileOutputStream(zipFile));

	Enumeration<? extends ZipEntry> entries = war.entries(); 
	while (entries.hasMoreElements()) {  
	  ZipEntry e = entries.nextElement(); 
	  append.putNextEntry(e);  
	  if (!e.isDirectory()) { 
		Path.copyStream(war.getInputStream(e), append, buffer); 
	  }  
	  append.closeEntry(); 
	}  
	// now append some extra content  
	ZipEntry e = new ZipEntry(appendFilePath);  
	append.putNextEntry(e);  
	Path.copyStream(new FileInputStream(new File(appendFilePath)), append, buffer);
	append.closeEntry();  
	// close  
	war.close();
	append.close(); 
  }  

  public static interface OnUpdateListener {
	public void onUpdate(String name, File out)
  }
}

