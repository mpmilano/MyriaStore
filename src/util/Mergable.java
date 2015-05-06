package util;

import java.io.Serializable;

public interface Mergable<T> extends Serializable {

	public T merge(Mergable<T> m);
}
