#include "BackingStore.h"
#include "Handle.h"	
package remote;


//Things we can track statically:
// Origin, Origin's consistency, Handle's consistency, 
// handle's access level, 

//of these, only access level and consistency are really "user-facing" "public" things.

public final class Handle<R_(T), BackingStore_(BS), Access_C(A,BSAccess), Consistency_C(C,BSCons)> {
	final BS_t_(BS).RemoteObject<R_g(T)> obj;

	public Handle(BS_t_(BS).RemoteObject<R_g(T)> obj){
		this.obj = obj;
	}
}
