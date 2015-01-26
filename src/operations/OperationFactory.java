#include "../consistency/Consistency.h"
#include "../remote/BackingStore.h"
package operations;
import remote.*;

public interface OperationFactory<BackingStore_(BS)> {
	public <R_(T), Consistency_C(C,BSCons)> Operation<R_g(T),BSref_(BS),Cref_(C)> build(Handle... hs);
}
