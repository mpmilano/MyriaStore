
package operations;
import remote.*;
public interface OperationFactory<T, C extends consistency.Top, H extends GetRemoteObj<?,?> > {
	public Operation<T,C> build(H h);
}
