package util;
import java.io.Serializable;

public class Pair<T extends Serializable, V extends Serializable>
	implements Serializable {
	public final T first;
	public final V second;
	public Pair(final T t, final V v){
		first = t; second = v;
	}
}
