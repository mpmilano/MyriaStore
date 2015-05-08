package operations;
import remote.*;
import access.*;
import consistency.*;
import java.io.Serializable;
import java.util.Collection;

public class ForEachOp<C extends consistency.Top, T extends Serializable, A extends access.Unknown, Sto extends ForEach<C,Obj, Sto>, Obj extends RemoteObject<? extends Collection<?> > > 
	extends Operation<Void, C> {

	private OperationFactory<?,T,C,Handle<T,C,A,C, Sto>> of;
	private Handle<? extends Collection<?>,C,? extends access.Read, ?, Sto > h;
	
	public ForEachOp(OperationFactory<?,T,C,Handle<T,C,A,C, Sto>> of, Handle<? extends Collection<?>,C,? extends access.Read, ?, Sto > h){
		this.of = of;
		this.h = h;
	}

	@SuppressWarnings("unchecked")
	@Override
		public Void execute(){
		try {
			((Sto) h.ro.getStore()).foreach(of, (Obj) h.ro);
			return null;
		}
		catch (ClassCastException e){
			//make a temporary store and such in-line, build handles, pass newly-minted handles to factory.
			return null;
		}
	}
}
