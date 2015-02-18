package transactions;

import remote.*;
import operations.*;

public class Gets<T extends java.io.Serializable, Cgive extends consistency.Top, Crecv extends Cgive> {

	Handle<T,Crecv, ? extends access.Write, ?, ? > recv;
	Operation<T,Cgive> give;
	
	public Gets(Handle<T,Crecv, ? extends access.Write, ?, ? > recv, Operation<T,Cgive> give){
		this.recv = recv;
		this.give = give;
	}

	
	void execute(){
		recv.ro.getStore().beginTransaction();
		recv.ro.put(give.execute());
		recv.ro.getStore().endTransaction();
	}
	

}
