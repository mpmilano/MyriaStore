package operations;

import java.util.concurrent.Callable;
public abstract class Operation<T, C extends consistency.Top > implements Callable<T> {
	abstract public T execute();
	@Override
	public T call() throws Exception{
		return execute();
	}
}
	
