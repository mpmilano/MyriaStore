#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;

public class GetOp<Handle_PAC_(H,access.Read, Get<HBSObj>)> extends Operation<R_g(HT)> {

//public class GetOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private Handle<Handle_P_g(H)> h;
	
	public GetOp(Handle<Handle_P_g(H)> h){
		this.h = h;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HT execute(){
		HBSObj tmp = h.obj;
		return (HT) h.obj.getStore().getObj(tmp);
	}

}
