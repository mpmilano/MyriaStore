package operations;

import remote.*;
import java.io.Serializable;

#define HandleType Handle<?,C,access.Read,?,?>

public class PrintFactory<C extends consistency.Top>
	implements OperationFactory<Void, C, HandleType> {

	public Operation<Void,C> build(HandleType h){
		final HandleType h2 = h;
		return new Operation<Void,C>() {
			@Override
			public Void execute(){
				System.out.println(h2.getRemoteObj().get().toString());
				return null;
			}
		};
	}
}
