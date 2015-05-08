package remote;

public interface AccessReplica<Cons extends consistency.Top, RObj extends RemoteObject, SType, RType, Store_p> {
	public Store<Cons, RObj, SType, RType, Store_p> access_replica(RType rt);
}
