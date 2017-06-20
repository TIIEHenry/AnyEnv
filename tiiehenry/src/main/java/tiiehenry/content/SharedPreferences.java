package tiiehenry.content;

import java.lang.reflect.*;
import java.util.*;

import android.content.Context;
import android.support.annotation.NonNull;
import android.content.SharedPreferences.Editor;

public class SharedPreferences {

  public android.content.SharedPreferences sp;
  public Editor editor;

  @NonNull
  public SharedPreferences(Context c, String fileName) {
	sp = c.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	editor = sp.edit();
  }

  @NonNull
  public SharedPreferences(Context c) {
	sp = c.getSharedPreferences("sharedpreference_def", Context.MODE_PRIVATE);
	editor = sp.edit();
  }

  public Editor edit() {
	return editor;
  }

  public void registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener l) {
	sp.registerOnSharedPreferenceChangeListener(l);
  }


  public void unregisterOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener l) {
	sp.unregisterOnSharedPreferenceChangeListener(l);
  }

  /**
   * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
   * @param key
   * @param object
   */
  @NonNull
  public void put(String key, Object object) {
	if (object instanceof String) {
	  editor.putString(key, (String) object);
	} else if (object instanceof Integer) {
	  editor.putInt(key, (Integer) object);
	} else if (object instanceof Boolean) {
	  editor.putBoolean(key, (Boolean) object);
	} else if (object instanceof Float) {
	  editor.putFloat(key, (Float) object);
	} else if (object instanceof Long) {
	  editor.putLong(key, (Long) object);
	} else if (object instanceof Set) {
	  editor.putStringSet(key, (Set)object);
	} else {
	  editor.putString(key, object.toString());
	}
	Compat.apply(editor);
  }
  public SharedPreferences putStringSet(String key, Set<String> object) {
	editor.putStringSet(key, object);
	return this;
  }

  /**
   * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
   * @param key
   * @param defObject
   * @return
   */
  @NonNull
  public  Object get(String key, Object defObject) {
	if (defObject instanceof String) {
	  return sp.getString(key, (String) defObject);
	} else if (defObject instanceof Integer) {
	  return sp.getInt(key, (Integer) defObject);
	} else if (defObject instanceof Boolean) {
	  return sp.getBoolean(key, (Boolean) defObject);
	} else if (defObject instanceof Float) {
	  return sp.getFloat(key, (Float) defObject);
	} else if (defObject instanceof Long) {
	  return sp.getLong(key, (Long) defObject);
	}
	return null;
  }
  public Set<String> getStringSet(String key, java.util.Set<String> def) {
	return sp.getStringSet(key, def);
  }
  
  public int getInt(String key, int def){
	return sp.getInt(key,def);
  }

  public long getLong(String key, long def){
	return sp.getLong(key,def);
  }

  public float getFloat(String key, float def){
	return sp.getFloat(key,def);
  }

  public boolean getBoolean(String key, boolean def){
	return sp.getBoolean(key,def);
  }
  
  /**
   * 返回所有的键值对
   * 

   * @return
   */
  public Map<String, ?> getAll() {
	return sp.getAll();
  }

  /**
   * 移除某个key值已经对应的值
   * @param key
   */
  public void remove(String key) {
	editor.remove(key);
	Compat.apply(editor);
  }

  /**
   * 清除所有数据
   * 

   */
  public void clear() {
	editor.clear();
	Compat.apply(editor);
  }

  /**
   * 查询某个key是否已经存在
   * 

   * @param key
   * @return
   */
  public boolean contains(String key) {
	return sp.contains(key);
  }



  /**
   * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
   * 
   * @author zhy
   * 
   */
  public static class Compat {

	private static final Method sApplyMethod = findApplyMethod();
	/**
	 * 反射查找apply的方法
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Method findApplyMethod() {
	  try {
		Class clz = android.content.SharedPreferences.Editor.class;
		return clz.getMethod("apply");
	  } catch (NoSuchMethodException e) {
	  }

	  return null;
	}

	/**
	 * 如果找到则使用apply执行，否则使用commit
	 * 
	 * @param editor
	 */
	public static void apply(android.content.SharedPreferences.Editor editor) {
	  try {
		if (sApplyMethod != null) {
		  sApplyMethod.invoke(editor);
		  return;
		}
	  } catch (IllegalArgumentException e) {
	  } catch (IllegalAccessException e) {
	  } catch (InvocationTargetException e) {
	  }
	  editor.commit();
	}
  }
  public static abstract interface OnSharedPreferenceChangeListener extends android.content.SharedPreferences.OnSharedPreferenceChangeListener{
	public abstract void onSharedPreferenceChanged(android.content.SharedPreferences p1, java.lang.String p2);
  }
  
}

