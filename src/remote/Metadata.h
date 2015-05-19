	#define MetaData FourTuple<TreeSet<Pair<ReplicaID,Nonce>>, \
	Nonce, ReplicaID, Ends>
	#define MD_readfrom(x) x.a
	#define MD_n(x) x.b
	#define MD_natural_replica(x) x.c
	#define MD_ends(x) x.d
