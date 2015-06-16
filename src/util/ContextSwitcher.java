package util;

public class ContextSwitcher {
	
	static Object o;
	
	public static void setContext(Object t){
		o = t;
	}

	public static boolean testContext(Object t){
		return o.equals(t);
	}
}
