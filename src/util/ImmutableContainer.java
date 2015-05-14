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
		return t;
	}



}
