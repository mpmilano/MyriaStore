package util;

public final class Timestamp implements java.io.Serializable, RCloneable<Timestamp>, Mergable<Timestamp>{
	private final String n;
	private final long time;
	private final long epsilon;
	
	public Timestamp(final long time, final long epsilon){
		this.n = NonceGenerator.get();
		this.time = time;
		this.epsilon = epsilon;
	}

	@Override
	public boolean equals(Object t){
		return (t instanceof Timestamp) && (((Timestamp)t).n == n);
	}
	
	private boolean prec(Timestamp t){
		assert(t != null);
		if (epsilon != t.epsilon)
			throw new RuntimeException("incomparable timestamps");
		if (n == t.n) return true;
		if (Math.abs(time - t.time) < epsilon) return false;
		return (time < t.time);
	}
	public static boolean prec(Timestamp before, Timestamp after){
		if (before == null && after == null) return true;
		else if (after == null) return false;
		else if (before == null) return true;
		else return before.prec(after);
	}
	
	public Timestamp merge(Timestamp update){
		assert(update != null);
		//TODO - if this is public, we should make less draconian assumptions.
		assert(!(prec(update,this) || prec(this,update)));
		return new Timestamp((time + update.time) / 2, epsilon);
	}

	@Override
	public Object clone(){
		return this;
	}

	@Override
	public Timestamp rclone(){
		return this;
	}
	
	public static Timestamp update(Timestamp old, Timestamp update){
		if (prec(old,update)) return update;
		else if (prec(update,old)) return old;
		else {
			assert(old != null && update != null);
			return old.merge(update);
		}
	}
}
