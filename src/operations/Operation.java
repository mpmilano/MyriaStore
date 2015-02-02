#include "../consistency/Consistency.h"
#include "../remote/BackingStore.h"
package operations;

import java.util.concurrent.Callable;

public abstract class Operation<T /*, BackingStore_(BS), Consistency_C(C,BSCons) */> implements Callable<T> {

	abstract public T execute();

	@Override
	public T call() throws Exception{
		return execute();
	}
	
}
