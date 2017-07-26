package tiiehenry.os;

import android.os.*;
import java.io.*;

import java.lang.Process;
import java.util.ArrayList;
import tiiehenry.io.Path;

/**
 * SD卡相关的辅助类
 * Directory 全部简写为Dir
 * 
 */
public class Env extends Environment{

  public Env() {
	super();
  }

  public static Path getRootDir() {
	return new Path(getRootDirectory()).absolute();
  }

  public static Path getDataDir() {
	return new Path(getDataDirectory()).absolute();
  }

  public static Path getExternalStorageDir() {
	return new Path(getExternalStorageDirectory()).absolute();
  }

  public static Path getExternalStoragePublicDir(String type) {
	return new Path(getExternalStoragePublicDirectory(type)).absolute();
  }

  public static Path getDownloadCacheDir() {
	return new Path(getDownloadCacheDirectory()).absolute();
  }

  /**
   * 判断SDCard是否可用
   */
  public static boolean isSDCardEnable() {
	return getExternalStorageState().equals(MEDIA_MOUNTED);
  }

  /**
   * 获取SD卡路径
   * 
   * @return
   */
  public static Path getSDCard() {
	return getExternalStorageDir().absolute();
  }
  public static ArrayList<String> getSDCardPaths() {
	ArrayList<String> list = new ArrayList<String>();
	try {
	  Runtime runtime = Runtime.getRuntime();
	  Process proc = runtime.exec("mount");
	  InputStream is = proc.getInputStream();
	  InputStreamReader isr = new InputStreamReader(is);
	  String line;
	  BufferedReader br = new BufferedReader(isr);
	  while ((line = br.readLine()) != null) {
		if (line.contains("secure")) {
		  continue;
		}
		if (line.contains("asec")) {
		  continue;
		}

		if (line.contains("fat")) {
		  String columns[] = line.split(" ");
		  if (columns.length > 1) {
			list.add("*" + columns[1]);
		  }
		} else if (line.contains("fuse")) {
		  String columns[] = line.split(" ");
		  if (columns.length > 1) {
			list.add(columns[1]);
		  }
		}
	  }
	} catch (FileNotFoundException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	}
	return list;
  }
  
  /**
   * 获取SD卡的剩余容量 单位byte
   */
  public static long getSDCardTotalSize() {
	if (isSDCardEnable()) {
	  StatFs stat = new StatFs(getSDCard().toString());
	  // 获取空闲的数据块的数量
	  long availableBlocks = (long) stat.getAvailableBlocks() - 4;
	  // 获取单个数据块的大小（byte）
	  long freeBlocks = stat.getAvailableBlocks();
	  return freeBlocks * availableBlocks;
	}
	return 0;
  }
  /**
   * 获取内部存储设备的总空间大小
   */
  public static long getRomSpaceTotalSize() {
	if (isSDCardEnable()) {
	  // 得到一个内部存储设备的目录/通过getPath得到路径
	  File path = Environment.getDataDirectory();
	  // 文件系统的帮助类，传入一个路径可以得到路径的信息
	  StatFs stat = new StatFs(path.getPath());
	  // 得到该存储空间每一块存储空间的大小
	  long blockSize = stat.getBlockSize();
	  // 得到空间总个数
	  long totalBlocks = stat.getBlockCount();
	  // 得到总空间大小
	  long totalSize = totalBlocks * blockSize;

	  return totalSize;
	}
	return 0;
  }
  
  /**
   * 获取指定路径所在空间的剩余可用容量，单位byte
   * 
   * @param path
   * @return 容量字节 SDCard可用空间，内部存储可用空间
   */
  public static long getFreeBytes(String path) {
	// 如果是sd卡的下的路径，则获取sd卡可用容量
	if (path.startsWith(getSDCard().toString())) {
	  path = getSDCard().toString();
	} else {
	  // 如果是内部存储的路径，则获取内存存储的可用容量
	  path = getDataDir().absolutePath();
	}
	StatFs stat = new StatFs(path);
	long availableBlocks = (long) stat.getAvailableBlocks() - 4;
	return stat.getBlockSize() * availableBlocks;
  }

}
