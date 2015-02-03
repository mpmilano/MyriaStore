#include "../remote/BackingStore.h"
package operations;


public interface Swap<FSO> {

	
	public void swapObj(FSO o1, FSO o2) throws java.io.IOException;

}
