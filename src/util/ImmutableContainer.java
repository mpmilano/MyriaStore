package util;

public final class ImmutableContainer<T extends RCloneable<T> > {
	private final T t;

	public ImmutableContainer(T t){
		this.t = t.rclone();
	}

	public T get(){
		return t.rclone();
	}

	public T readOnlyIpromise(){
		return get();
		//return t;
	}

	public static <T extends RCloneable<T> > T readOnlyIfExists(ImmutableContainer<T> c){
		if (c != null) return c.readOnlyIpromise();
		else return null;
	}



}
