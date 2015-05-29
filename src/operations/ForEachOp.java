package operations;
import remote.*;
import access.*;
import consistency.*;
import java.io.Serializable;
import java.util.Collection;

public class ForEachOp<C extends consistency.Top, T extends Serializable, A extends access.Unknown> 
	extends Operation<Void, C> {

	private OperationFactory<?,C,Handle<? extends T,C,A,?,?>> of;
	private Handle<? extends Collection<?>,C,? extends access.Read, ?, ? > h;
	
	public ForEachOp(OperationFactory<?,C,Handle<? extends T,C,A,?,?>> of, Handle<? extends Collection<?>,C,? extends access.Read, ?, ? > h) {
		this.of = of;
		this.h = h;
	}
	@Override
	@SuppressWarnings("unchecked")
		public Void execute(){
		try {
			((ForEach) h.ro.getStore()).foreach(of, h.ro);
			return null;
		}
		catch (ClassCastException e){
			//make a temporary store and such in-line, build handles, pass newly-minted handles to factory.
			return null;
		}
	}
}
