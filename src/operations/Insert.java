package operations;

import remote.*;
import util.*;

public interface Insert<Set, Obj extends RemoteObject> {
	
	public void insert(Set set, Obj e) throws MyriaIOException;
	public void insert(Set set, java.io.Serializable e) throws MyriaIOException;
	public InsertFactory<?,?,?> ifact();
}
