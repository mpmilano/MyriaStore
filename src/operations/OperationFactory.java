#include "../remote/BackingStore.h"
package operations;
import remote.*;

public interface OperationFactory<R_(T), Consistency_(C), H extends GetRemoteObj> {

	public  Operation<T,C> build(H h);
}
