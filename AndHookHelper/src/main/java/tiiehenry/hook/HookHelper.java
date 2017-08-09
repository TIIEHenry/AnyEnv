package tiiehenry.hook;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;
import tiiehenry.hook.Hook;
import apk.andhook.AndHook;

public class HookHelper {
  public void applyHooks(Class<?> holdClass) {
	for (Method hookMethod : holdClass.getDeclaredMethods()) {
	  Hook hook = hookMethod.getAnnotation(Hook.class);
	  if (hook != null) {
		Method origin=null;
		try {
		  origin = hook.clazz().getDeclaredMethod(hook.method(), (Class[])hook.param());
		} catch (SecurityException|NoSuchMethodException e) {
		}
		if (origin == null)
		  return;
		AndHook.HookHelper.hook(origin, hookMethod);
	  }
	}
  }

}
