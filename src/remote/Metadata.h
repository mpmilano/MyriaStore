private static class MetaData<CReplicaID extends CausalSafe<CReplicaID> & Comparable<CReplicaID>> extends FourTuple<TreeSet<Pair<ReplicaID,Nonce>>, Nonce, ReplicaID, Ends<CReplicaID>> {
	public MetaData(TreeSet<Pair<ReplicaID,Nonce>> a, Nonce b, ReplicaID c, Ends<CReplicaID> d){
		super(a,b,c,d);
	}
}

#define MetaData MetaData<CReplicaID>
	#define MD_readfrom(x) x.a
	#define MD_n(x) x.b
	#define MD_natural_replica(x) x.c
	#define MD_ends(x) x.d
