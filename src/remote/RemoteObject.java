package remote;

import java.io.Serializable;

public interface RemoteObject<T extends Serializable> {

	public T get();

	public void put(T t);
	
}
