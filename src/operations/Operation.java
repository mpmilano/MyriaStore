package operations;

import java.util.concurrent.Callable;

public interface Operation<T, Consistency> extends Callable<T> {
	
}
