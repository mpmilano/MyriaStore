#include "../consistency/Consistency.h"
#include "../remote/BackingStore.h"
package operations;
import remote.*;

public class GetFactory<R_(T), OpBasics(access.HasAccess<? extends access.Read> & consistency.HasConsistency<C>)>  implements OperationFactory<T,OpBasics_g> {
	
	public GetFactory(H h){
		//just for inference purposes.
	}

	public GetFactory(){}
	
	public GetOp<T,OpBasics_g> build(H hs){
		return new GetOp<>(hs);
	}
	
}

