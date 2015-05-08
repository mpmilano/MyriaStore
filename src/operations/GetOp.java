package operations;

import java.io.Serializable;
import remote.*;

public class GetOp<T extends Serializable, C extends consistency.Top > extends Operation<T,C> {

	private Handle<T,C,? extends access.Read,?,?> h;
	
	public GetOp(Handle<T,C,? extends access.Read,?,?> h){
		this.h = h;
	}

	@Override
	public T execute() { return h.ro.get(); }
	
	
}
