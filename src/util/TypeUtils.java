package util;

public class TypeUtils {

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T t){
		return (Class<T>) t.getClass();
	}
}
