#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;

//public class GetOp<Handle_PAC_(H,access.Read, Get<BackingStore<BSref_(HBS)>.RemoteObject<R_g(HT)> >)> extends Operation<R_g(HT), BSref_(HBS), HC> {

public class GetOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private Handle<R_g(HT),?,?,?,? extends Get<GT>,?,?> h;
	
	public GetOp(Handle<R_g(HT),?,?,?,? extends Get<GT>,?,?> h){
		this.h = h;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HT execute(){
		BackingStore<?,?,?,?>.RemoteObject<?> tmp0 = h.obj;
		GT tmp = (GT) tmp0;
		return h.obj.getStore().getObj((Class<HT>) tmp.getUnderlyingClass(), tmp);
	}

}
