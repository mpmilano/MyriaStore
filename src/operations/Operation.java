package operations;


import java.util.concurrent.Callable;
import transactions.*;

public abstract class Operation<T, C extends consistency.Top > extends Transaction implements Callable<T> {
	abstract public T execute();
	@Override
	public T call() throws Exception{
		return execute();
	}
	@Override
	public void run() {
		execute();
	}
}
	
