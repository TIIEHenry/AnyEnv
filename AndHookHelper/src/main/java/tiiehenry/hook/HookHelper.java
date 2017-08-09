package tiiehenry.hook;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;
import tiiehenry.hook.Hook;
import apk.andhook.AndHook;

public class HookHelper extends AndHook.HookHelper {

  public void applyHooks(Class<?> holdClass) throws Exception,SecurityException, NoSuchMethodException {
	AndHook.ensureClassInitialized(holdClass);
	for (Method hookMethod : holdClass.getDeclaredMethods()) {
	  Hook hook = hookMethod.getAnnotation(Hook.class);
	  if (hook != null) {
		Method origin=null;
		AndHook.ensureClassInitialized(hook.clazz());
		origin = hook.clazz().getDeclaredMethod(hook.method(), (Class[])hook.param());
		if (origin == null)
		  return;
		hook(origin, hookMethod);
	  }
	}
  }

}
