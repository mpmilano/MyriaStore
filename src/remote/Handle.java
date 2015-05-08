package remote;

import java.io.Serializable;

public final class Handle<T extends Serializable, Cons extends consistency.Top, Access extends access.Unknown, OriginalCons extends consistency.Top, Store>
	implements HasConsistency<Cons>, HasAccess<Access>, PointsTo<T>, StoreCons<OriginalCons>, GetRemoteObj<T>, Serializable
{

	//note - if you want to actually refer to things by these predicates,
	//you can just implement an "isHandle" interface and then make static functions which convert
	//types like Foo & isHandle into Handle<Foo ... >.  
	
	//TODO - want a friend designator. 
	public RemoteObject<T> ro;

	Handle(RemoteObject<T> ro){
		this.ro = ro;
	}

	public RemoteObject<T> getRemoteObj(){ return ro; }

	@SuppressWarnings("unchecked")
	public static <NewT extends Serializable, T extends NewT,
					  Cons extends consistency.Top,
								   Access extends access.Unknown,
												  OriginalCons extends consistency.Top, Store>
		Handle<NewT, Cons, Access, OriginalCons, Store> relaxT(Handle<T,Cons,Access,OriginalCons, Store> h){
		return (Handle<NewT, Cons, Access, OriginalCons, Store>) h;
	}


	@SuppressWarnings("unchecked")
	public static <T extends Serializable,
					  Cons extends consistency.Top,
								   OriginalCons extends consistency.Top, Store>
		Handle<T, Cons, access.Write, OriginalCons, Store> changeUp(Handle<T,? extends Cons,? extends access.ReadWrite,OriginalCons, Store> h){
		return (Handle<T, Cons, access.Write, OriginalCons, Store>) ((Handle) h); 
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable,
					  Cons extends consistency.Top,
								   OriginalCons extends consistency.Top, Store>
		Handle<T, Cons, access.Read, OriginalCons, Store> changeDown(Handle<T,? super Cons,? extends access.ReadWrite,OriginalCons, Store> h){
		return (Handle<T, Cons, access.Read, OriginalCons, Store>) ((Handle) h); 
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable,
					  NewAccess extends access.Unknown, OldAccess extends NewAccess,
					  Cons extends consistency.Top,
								   OriginalCons extends consistency.Top, Store>
		Handle<T, Cons, NewAccess, OriginalCons, Store> restrict(Handle<T,Cons,OldAccess,OriginalCons, Store> h){
		return (Handle<T, Cons, NewAccess, OriginalCons, Store>) ((Handle) h);
	}


}
