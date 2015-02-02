#include "../remote/BackingStore.h"
package operations;


public interface Put<FSO> {

	public <R_(T)> void putObj(FSO o, T t);

}
