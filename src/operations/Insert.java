package operations;

import remote.*;

public interface Insert<Set, Obj extends RemoteObject> {
	
	public void insert(Set set, Obj e) throws java.io.IOException;
	public void insert(Set set, java.io.Serializable e) throws java.io.IOException;
}
