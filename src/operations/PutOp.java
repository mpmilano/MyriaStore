package operations;

import java.io.Serializable;
import remote.*;

public class PutOp<T extends Serializable, C extends consistency.Top > extends Operation<Void,C> {

	private Handle<T,C,? extends access.Write,?,?> h;
	private T t;
	
	public PutOp(Handle<T,C,? extends access.Write,?,?> h, T t){
		this.h = h;
		this.t = t;
	}

	@Override
	public Void execute() {
		h.ro.put(t);
		return null;
	}
	
	
}
