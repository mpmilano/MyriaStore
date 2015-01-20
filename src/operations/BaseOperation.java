package operations;

import java.util.concurrent.Callable;

import consistency.BaseModel;

public interface BaseOperation<T, M extends BaseModel<handles.access.Any>> extends Callable<T>{
	public T execute();
}
