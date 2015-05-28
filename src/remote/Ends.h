private class Ends extends HashMap<CReplicaID, Timestamp>
	implements CausalSafe<Ends>,
			   IsSerializable<Timestamp>,
			   IsAlsoSerializable<CReplicaID>,
			   ISRCloneable<CReplicaID>,
			   ISAlsoRCloneable<Timestamp>
{
	
	public Ends(){
		//TODO: I think empty is correct here, but I'm not sure.
	}
	
	public boolean prec(Ends e){
		
		Set<CReplicaID> crs = new TreeSet<>();
		crs.addAll(keySet());
		crs.addAll(e.keySet());
		
		for (CReplicaID cr : crs){
			Timestamp this_ts = get(cr);
			Timestamp e_ts = e.get(cr);
			if (!Timestamp.prec(this_ts,e_ts)) return false;
		}
		return true;
	}
	
	public Ends fast_forward(Ends future){
		if (future != null)
			for (CReplicaID cr : future.keySet()){
				put(cr,Timestamp.update(get(cr), future.get(cr)));
			}
		return this;
	}
	
	@Override
	public Ends merge(Ends e){
		return this.fast_forward(e);
	}
	
	@Override
	public Ends rclone(){
		Ends ret = new Ends();
		for (Map.Entry<CReplicaID, Timestamp> e : entrySet()){
			ret.put(e.getKey().rclone(), e.getValue().rclone());
		}
		return ret;
	}
	
	@Override
	public Object clone(){
		return rclone();
	}
}
