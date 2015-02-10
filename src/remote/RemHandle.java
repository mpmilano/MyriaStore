#include "../remote/BackingStore.h"
package remote;

public interface RemHandle<R_(HT), Consistency_(HC), Access_(HA), Consistency_(HBSCons)>
	extends access.HasAccess<HA>, consistency.HasConsistency<HC>, consistency.OriginalConsistency<HBSCons>, PointsTo<HT>, GetRemoteObj {}
