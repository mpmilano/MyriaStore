package operations;

import remote.*;

public class PrintFactory<T2 extends java.io.Serializable, C extends consistency.Top, H extends GetRemoteObj<T2>> implements OperationFactory<Void, T2, C, H> {

	public Operation<Void,C> build(H h){
		System.out.println("building...");
		final H h2 = h;
		return new Operation<Void,C>() {
			@Override
			public Void execute(){
				System.out.println("running...");
				System.out.println(h2.getRemoteObj().get().toString());
				return null;
			}
		};
	}
}
