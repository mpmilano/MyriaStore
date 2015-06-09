package util;

public class TypeUtils {

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T t){
		return (Class<T>) t.getClass();
	}

	@SuppressWarnings("unchecked")
	public static <General, Specific extends HasGenericForm<General> > General forget(Specific sp){
		return (General) sp;
	}

	//the idea is that Specific1 and Specific2 have to be the same.
	@SuppressWarnings("unchecked")
	public static <T, General,
		Specific1 extends ParameterizedOn<T> & HasGenericForm<General>,
						  Specific2 extends ParameterizedOn<T> & HasGenericForm<General>>
		Specific2 remember(Specific1 sp){
		return (Specific2) sp;
	}
}
