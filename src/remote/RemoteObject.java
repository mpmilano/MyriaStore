package remote;

import java.io.Serializable;

public interface RemoteObject<T extends Serializable, K> extends Serializable{

	public T get();

	public void put(T t);

	public K name();

	public Store getStore();
	
}
