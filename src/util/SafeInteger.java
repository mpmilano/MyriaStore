package util;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
#define cassert(x,s) assert((new Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

public class SafeInteger implements consistency.CausalSafe<SafeInteger>, Comparable<SafeInteger> {

	private static final ReentrantLock lock = new ReentrantLock();
	private static boolean from_wrap = false;

	#define Type String
	
	final public Type i;
	
	private SafeInteger(final Type i){
		lock.lock();
		assert(from_wrap);
		this.i = i;
		from_wrap = false;
		lock.unlock();
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
		Type j = this.i;
		return j.compareTo(i.i);
	}

	private static ConcurrentHashMap<Integer, SafeInteger> reuse = new ConcurrentHashMap<>();
	public static SafeInteger wrap(final Integer i){
		return wrap(i + "");
	}
	
	public static SafeInteger wrap(final Type i){
		try{
			lock.lock();
			from_wrap = true;
			if (reuse.containsKey(i)) return reuse.get(i);
			else return new SafeInteger(i);
		}
		finally{
			lock.unlock();
			from_wrap = false;
		}
	}

	public String toString(){
		if (pretty.containsKey(i)) return pretty.get(i);
		else return i + "";
	}

	public int toInt(){
		return Integer.parseInt(i);
	}

	public SafeInteger concat(SafeInteger b){
		return wrap(i + b.i);
	}

	public boolean taggedWith(SafeInteger b){
		return i.endsWith(b.i);
	}



	private static ConcurrentHashMap<Integer, String> pretty = new ConcurrentHashMap<>();
	public static SafeInteger ofString(String s){
		/*
		int hc = s.hashCode();
		if (!pretty.containsKey(hc)) pretty.put(hc,s);
		return wrap(hc);
		*/
		return wrap(s);
	}
}

