package util;
import java.io.Serializable;
import consistency.CausalSafe;


public class CausalSafeStringPair<V extends Serializable & Comparable<V> & Mergable<V> & RCloneable<V> >
	implements Comparable<CausalSafeStringPair<V>>, Serializable, CausalSafe<CausalSafeStringPair<V>> {
	public final String first;
	public final V second;
	public CausalSafeStringPair(final String t, final V v){
		first = t; second = v;
	}

	@Override
	public int compareTo(CausalSafeStringPair<V> t){
		int r1 = first.compareTo(t.first);
		if (r1 == 0) return second.compareTo(t.second);
		else return r1;
	}

	@Override
	public String toString(){
		return "< " + first.getClass().toString() + " : " + first.toString() + " | " + second.getClass().toString() + " : " + second.toString() + " >";
	}

	@Override
	public CausalSafeStringPair<V> merge(CausalSafeStringPair<V> o){
		if (o == null) return this;
		assert(first.equals(o.first));
		return new CausalSafeStringPair<>(first,second.merge(o.second));
	}

	@Override
	public CausalSafeStringPair<V> clone(){
		return this;
	}

	@Override
	public CausalSafeStringPair<V> rclone(){
		return this;
	}
}
