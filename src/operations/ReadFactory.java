#include "../consistency/Consistency.h"
#include "../remote/BackingStore.h"
package operations;
import remote.*;
import access.*;

public interface ReadFactory<S extends Get<HBSObj>, HBSObj> {
	public <R_(T), H extends GetUnderlyingObj<HBSObj> & GetStore<S> & HasAccess<? extends Read> & PointsTo<T> > Operation<T> buildOp(H h);

}
