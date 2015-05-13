package util;

public class SafeInteger implements consistency.CausalSafe<SafeInteger>, Comparable<SafeInteger> {

	final public int i;
	
	public SafeInteger(int i){
		this.i = i;
	}

	@Override
	public Object clone(){return this;}

	@Override
	public SafeInteger rclone(){return this;}

	@Override
	public SafeInteger merge(SafeInteger i){
		assert(this.i == i.i);
		return this;
	}

	@Override
	public int compareTo(SafeInteger i){
		Integer j = this.i;
		return j.compareTo(i.i);
	}

	public static SafeInteger wrap(Integer i){
		return new SafeInteger(i);
	}
}
