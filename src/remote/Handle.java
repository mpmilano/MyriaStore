package remote;

import java.io.Serializable;

public final class Handle<T extends Serializable, Cons extends consistency.Top, Access extends access.Unknown, OriginalCons extends consistency.Top>
	implements HasConsistency<Cons>, HasAccess<Access>, PointsTo<T>, StoreCons<OriginalCons>
{

	//TODO - want a friend designator. 
	public RemoteObject<T> ro;

	Handle(RemoteObject<T> ro){
		this.ro = ro;
	}

	@SuppressWarnings("unchecked")
	static <NewT extends Serializable, T extends NewT,
					  Cons extends consistency.Top,
								   Access extends access.Unknown,
												  OriginalCons extends consistency.Top>
		Handle<NewT, Cons, Access, OriginalCons> relaxT(Handle<T,Cons,Access,OriginalCons> h){
		return (Handle<NewT, Cons, Access, OriginalCons>) h;
	}

	@SuppressWarnings("unchecked")
	static <T extends Serializable,
					  Cons extends consistency.Top,
								   OriginalCons extends consistency.Top>
		Handle<T, Cons, access.Write, OriginalCons> changeUp(Handle<T,? extends Cons,? extends access.ReadWrite,OriginalCons> h){
		return new Handle<>(h.ro);
	}

	@SuppressWarnings("unchecked")
	static <T extends Serializable,
					  Cons extends consistency.Top,
								   OriginalCons extends consistency.Top>
		Handle<T, Cons, access.Read, OriginalCons> changeDown(Handle<T,? super Cons,? extends access.ReadWrite,OriginalCons> h){
		return new Handle<>(h.ro);
	}

}
