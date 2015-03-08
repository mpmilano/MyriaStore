package transactions;

import remote.*;
import operations.*;

public class Gets<T extends java.io.Serializable, Cgive extends consistency.Top, Crecv extends Cgive> extends Transaction{

	Handle<T,Crecv, ? extends access.Write, ?, ? > recv;
	Operation<T,Cgive> give;
	
	public Gets(Handle<T,Crecv, ? extends access.Write, ?, ? > recv, Operation<T,Cgive> give){
		this.recv = recv;
		this.give = give;
	}

	@Override
	public void run(){
		recv.ro.getStore().beginTransaction();
		recv.ro.put(give.execute());
		recv.ro.getStore().endTransaction();
	}
	

}
