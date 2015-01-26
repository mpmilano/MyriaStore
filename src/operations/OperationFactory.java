package operations;
import remote.*;

public interface OperationFactory {
	public <T, Consistency> Operation<T,Consistency> build(Handle... hs);
}
