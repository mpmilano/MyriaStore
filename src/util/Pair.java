package util;
import java.io.Serializable;

public class Pair<T extends Serializable & Comparable<T>,
							V extends Serializable & Comparable<V> >
	implements Comparable<Pair<T,V>>, Serializable {
	public final T first;
	public final V second;
	public Pair(final T t, final V v){
		first = t; second = v;
	}

	@Override
	public int compareTo(Pair<T,V> t){
		int r1 = first.compareTo(t.first);
		if (r1 == 0) return second.compareTo(t.second);
		else return r1;
	}
}
