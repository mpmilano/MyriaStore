package operations;

import remote.*;
import util.*;

public interface Increment<Obj extends RemoteObject<? extends Incrementable>> {
	
	public void incr(Obj e) throws java.io.IOException;
	public void decr(Obj e) throws java.io.IOException;
	//public IncrementFactory<?,?> ifact();
}
