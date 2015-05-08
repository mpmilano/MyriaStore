package util;

public final class Timestamp{
	private final String n;
	private final long time;
	private final long epsilon;
	
	public Timestamp(final long time, final long epsilon){
		this.n = NonceGenerator.get();
		this.time = time;
		this.epsilon = epsilon;
	}
	
	private boolean prec(Timestamp t){
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
	
	private Timestamp mergeOf(Timestamp update){
		return new Timestamp((time + update.time) / 2, epsilon);
	}
	
	public static Timestamp update(Timestamp old, Timestamp update){
		if (prec(old,update)) return update;
		else if (prec(update,old)) return old;
		else return old.mergeOf(update);
	}
}
