package tiiehenry.os;

import tiiehenry.io.Path;

import android.os.*;

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
	return getExternalStorageState().equals(
	  MEDIA_MOUNTED);

  }

  /**
   * 获取SD卡路径
   * 
   * @return
   */
  public static Path getSDCard() {
	return getExternalStorageDir().absolute();
  }

  /**
   * 获取SD卡的剩余容量 单位byte
   */
  public static long getSDCardAllSize() {
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
   * 获取指定路径所在空间的剩余可用容量，单位byte
   * 
   * @param PathPath
   * @return 容量字节 SDCard可用空间，内部存储可用空间
   */
  public static long getFreeBytes(String PathPath) {
	// 如果是sd卡的下的路径，则获取sd卡可用容量
	if (PathPath.startsWith(getSDCard().toString())) {
	  PathPath = getSDCard().toString();
	} else {
	  // 如果是内部存储的路径，则获取内存存储的可用容量
	  PathPath = getDataDir().absolutePath();
	}
	StatFs stat = new StatFs(PathPath);
	long availableBlocks = (long) stat.getAvailableBlocks() - 4;
	return stat.getBlockSize() * availableBlocks;
  }

}
