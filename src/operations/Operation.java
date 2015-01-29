#include "../consistency/Consistency.h"
#include "../remote/BackingStore.h"
package operations;

import java.util.concurrent.Callable;

public abstract class Operation<R_(T) /*, BackingStore_(BS), Consistency_C(C,BSCons) */> implements Callable<R_g(T)> {

	abstract public T execute();

	@Override
	public T call() throws Exception{
		return execute();
	}
	
}
