#include "../remote/BackingStore.h"

package operations;
import remote.*;

public interface ForEach<Consistency_(C), FSDir>{
	
	public <R_(T), Access_(A)>
		void foreach(OperationFactory<T,C, RemHandle<T,C,A,C> > of, FSDir fs);

}
