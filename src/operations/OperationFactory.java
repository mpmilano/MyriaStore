
package operations;
import remote.*;
public interface OperationFactory<T, T2 extends java.io.Serializable, C extends consistency.Top, H extends GetRemoteObj<T2> > {
	public Operation<T,C> build(H h);
}
