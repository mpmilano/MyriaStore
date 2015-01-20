package operations;

import java.util.concurrent.Callable;

import consistency.BaseModel;

public interface Operation<T, M extends BaseModel> extends Callable<T>{
	public T execute();
}
