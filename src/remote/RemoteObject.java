package remote;

import java.io.Serializable;

public interface RemoteObject<T extends Serializable> extends Serializable{

	public T get();

	public void put(T t);

	public Store getStore();
	
}
