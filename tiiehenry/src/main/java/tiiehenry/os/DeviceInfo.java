package tiiehenry.os;

import android.net.wifi.*;
import android.os.*;
import java.io.*;
import java.net.*;
import java.util.*;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
/**
 * Helper class for accessing hardware specifications,
 * including the number of CPU cores, CPU clock speed
 * and phone infos
 * and total available RAM.
 */
public class DeviceInfo {

  /**
   * The default return value of any method in this class when an
   * error occurs or when processing fails (Currently set to -1). Use this to check if
   * the information about the device in question was successfully obtained.
   */
  public static final int DEVICEINFO_UNKNOWN = -1;

  /**
   * Reads the number of CPU cores from the first available information from
   * {@code /sys/devices/system/cpu/possible}, {@code /sys/devices/system/cpu/present},
   * then {@code /sys/devices/system/cpu/}.
   *
   * @return Number of CPU cores in the phone, or DEVICEINFO_UKNOWN = -1 in the event of an error.
   */
  public static int getCPUCoresNumber() {
	int cores;
    try {
      cores = getCoresFromFileInfo("/sys/devices/system/cpu/possible");
      if (cores == DEVICEINFO_UNKNOWN) {
        cores = getCoresFromFileInfo("/sys/devices/system/cpu/present");
      }
      if (cores == DEVICEINFO_UNKNOWN) {
        cores = getCoresFromCPUFileList();
      }
    } catch (SecurityException e) {
      cores = DEVICEINFO_UNKNOWN;
    } catch (NullPointerException e) {
      cores = DEVICEINFO_UNKNOWN;
    }
    return cores;
  }

  /**
   * Tries to read file contents from the file location to determine the number of cores on device.
   * @param fileLocation The location of the file with CPU information
   * @return Number of CPU cores in the phone, or DEVICEINFO_UKNOWN = -1 in the event of an error.
   */
  private static int getCoresFromFileInfo(String fileLocation) {
    InputStream is = null;
    try {
      is = new FileInputStream(fileLocation);
      BufferedReader buf = new BufferedReader(new InputStreamReader(is));
      String fileContents = buf.readLine();
      buf.close();
      return getCoresFromFileString(fileContents);
    } catch (IOException e) {
      return DEVICEINFO_UNKNOWN;
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
		  // Do nothing.
        }
      }
    }
  }

  /**
   * Converts from a CPU core information format to number of cores.
   * @param str The CPU core information string, in the format of "0-N"
   * @return The number of cores represented by this string
   */
  static int getCoresFromFileString(String str) {
    if (str == null || !str.matches("0-[\\d]+$")) {
      return DEVICEINFO_UNKNOWN;
    }
    int cores = Integer.valueOf(str.substring(2)) + 1;
    return cores;
  }

  private static int getCoresFromCPUFileList() {
    return new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
  }

  private static final FileFilter CPU_FILTER = new FileFilter() {
    @Override
    public boolean accept(File pathname) {
      String path = pathname.getName();
      //regex is slow, so checking char by char.
      if (path.startsWith("cpu")) {
        for (int i = 3; i < path.length(); i++) {
          if (!Character.isDigit(path.charAt(i))) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
  };

  /**
   * Method for reading the clock speed of a CPU core on the device. Will read from either
   * {@code /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq} or {@code /proc/cpuinfo}.
   *
   * @return Clock speed of a core on the device, or -1 in the event of an error.
   */
  public static int getCPUMaxFreqKHz() {
    int maxFreq = DEVICEINFO_UNKNOWN;
    try {
      for (int i = 0; i < getCPUCoresNumber(); i++) {
        String filename =
		  "/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq";
        File cpuInfoMaxFreqFile = new File(filename);
        if (cpuInfoMaxFreqFile.exists() && cpuInfoMaxFreqFile.canRead()) {
          byte[] buffer = new byte[128];
          FileInputStream stream = new FileInputStream(cpuInfoMaxFreqFile);
          try {
            stream.read(buffer);
            int endIndex = 0;
            //Trim the first number out of the byte buffer.
            while (Character.isDigit(buffer[endIndex]) && endIndex < buffer.length) {
              endIndex++;
            }
            String str = new String(buffer, 0, endIndex);
            Integer freqBound = Integer.parseInt(str);
            if (freqBound > maxFreq) {
              maxFreq = freqBound;
            }
          } catch (NumberFormatException e) {
            //Fall through and use /proc/cpuinfo.
          } finally {
            stream.close();
          }
        }
      }
      if (maxFreq == DEVICEINFO_UNKNOWN) {
        FileInputStream stream = new FileInputStream("/proc/cpuinfo");
        try {
          int freqBound = parseFileForValue("cpu MHz", stream);
          freqBound *= 1000; //MHz -> kHz
          if (freqBound > maxFreq) maxFreq = freqBound;
        } finally {
          stream.close();
        }
      }
    } catch (IOException e) {
      maxFreq = DEVICEINFO_UNKNOWN; //Fall through and return unknown.
    }
    return maxFreq;
  }

  /**
   * Calculates the total RAM of the device through Android API or /proc/meminfo.
   *
   * @param c - Context object for current running activity.
   * @return Total RAM that the device has, or DEVICEINFO_UNKNOWN = -1 in the event of an error.
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static long getTotalMemory(Context c) {
    // memInfo.totalMem not supported in pre-Jelly Bean APIs.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
      ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
      am.getMemoryInfo(memInfo);
      if (memInfo != null) {
        return memInfo.totalMem;
      } else {
        return DEVICEINFO_UNKNOWN;
      }
    } else {
      long totalMem = DEVICEINFO_UNKNOWN;
      try {
        FileInputStream stream = new FileInputStream("/proc/meminfo");
        try {
          totalMem = parseFileForValue("MemTotal", stream);
          totalMem *= 1024;
        } finally {
          stream.close();
        }
      } catch (IOException e) {
      }
      return totalMem;
    }
  }

  /**
   * Helper method for reading values from system files, using a minimised buffer.
   *
   * @param textToMatch - Text in the system files to read for.
   * @param stream      - FileInputStream of the system file being read from.
   * @return A numerical value following textToMatch in specified the system file.
   * -1 in the event of a failure.
   */
  private static int parseFileForValue(String textToMatch, FileInputStream stream) {
    byte[] buffer = new byte[1024];
    try {
      int length = stream.read(buffer);
      for (int i = 0; i < length; i++) {
        if (buffer[i] == '\n' || i == 0) {
          if (buffer[i] == '\n') i++;
          for (int j = i; j < length; j++) {
            int textIndex = j - i;
            //Text doesn't match query at some point.
            if (buffer[j] != textToMatch.charAt(textIndex)) {
              break;
            }
            //Text matches query here.
            if (textIndex == textToMatch.length() - 1) {
              return extractValue(buffer, j);
            }
          }
        }
      }
    } catch (IOException e) {
      //Ignore any exceptions and fall through to return unknown value.
    } catch (NumberFormatException e) {
    }
    return DEVICEINFO_UNKNOWN;
  }

  /**
   * Helper method used by {@link #parseFileForValue(String, FileInputStream) parseFileForValue}. Parses
   * the next available number after the match in the file being read and returns it as an integer.
   * @param index - The index in the buffer array to begin looking.
   * @return The next number on that line in the buffer, returned as an int. Returns
   * DEVICEINFO_UNKNOWN = -1 in the event that no more numbers exist on the same line.
   */
  private static int extractValue(byte[] buffer, int index) {
    while (index < buffer.length && buffer[index] != '\n') {
      if (Character.isDigit(buffer[index])) {
        int start = index;
        index++;
        while (index < buffer.length && Character.isDigit(buffer[index])) {
          index++;
        }
        String str = new String(buffer, 0, start, index - start);
        return Integer.parseInt(str);
      }
      index++;
    }
    return DEVICEINFO_UNKNOWN;
  }
  

  /**
   * 获取本机语言
   *
   * @return
   */
  public static String getLanguage() {
	Locale locale = Locale.getDefault();
	String languageCode = locale.getLanguage();
	if (TextUtils.isEmpty(languageCode)) {
	  languageCode = "";
	}
	return languageCode;
  }

  /**
   * 获取国家编号
   *
   * @return
   */
  public static String getCountry() {
	Locale locale = Locale.getDefault();
	String countryCode = locale.getCountry();
	if (TextUtils.isEmpty(countryCode)) {
	  countryCode = "";
	}
	return countryCode;
  }


  /**
   * 获取手机的IMEI号
   */
  public static String getIMEI(Context context) {
	TelephonyManager telecomManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	String imei = telecomManager.getDeviceId();
	return imei;
  }

  /**
   * 获取手机IESI号
   */
  public static String getIESI(Context context) {
	TelephonyManager telecomManager = (TelephonyManager) context
	  .getSystemService(Context.TELEPHONY_SERVICE);
	String imsi = telecomManager.getSubscriberId();
	return imsi;
  }


  /**
   * 获取设备的系统版本号
   */
  public static int getSDKVersion() {
	int sdk = android.os.Build.VERSION.SDK_INT;
	return sdk;
  }

  /**
   * 获取手机品牌
   */
  public static String getBrand() {
	String brand = android.os.Build.BRAND;
	return brand;
  }

  /**
   * 获取设备的型号
   */
  public static String getDeviceName() {
	String model = android.os.Build.MODEL;
	return model;
  }


  /**
   * 获取本机IP地址
   *
   * @return
   */
  public static String getLocalIPAddress() {
	try {
	  for (Enumeration<NetworkInterface> en = NetworkInterface
			 .getNetworkInterfaces(); en.hasMoreElements(); ) {
		NetworkInterface intf = en.nextElement();
		for (Enumeration<InetAddress> enumIpAddr = intf
			   .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
		  InetAddress inetAddress = enumIpAddr.nextElement();
		  if (!inetAddress.isLoopbackAddress()) {
			return inetAddress.getHostAddress().toString();
		  }
		}
	  }
	} catch (SocketException ex) {
	  return "0.0.0.0";
	}
	return "0.0.0.0";
  }

  /**
   * 获取 MAC 地址
   * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
   */
  public static String getMacAddress(Context context) {
	//wifi mac地址
	WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	WifiInfo info = wifi.getConnectionInfo();
	String mac = info.getMacAddress();
	return mac;
  }

  /**
   * 获取 开机时间
   */
  public static String getBootTimeString() {
	long ut = SystemClock.elapsedRealtime() / 1000;
	int h = (int) ((ut / 3600));
	int m = (int) ((ut / 60) % 60);

	return h + ":" + m;
  }
}
