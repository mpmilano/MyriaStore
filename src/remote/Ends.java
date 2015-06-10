#define cassert(x,s) assert((new Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

package remote;
import consistency.*;
import util.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;


class Ends<CReplicaID extends CausalSafe<CReplicaID> & Comparable<CReplicaID>> extends ConcurrentSkipListMap<CReplicaID, Timestamp>
	implements CausalSafe<Ends<CReplicaID>>,
			   IsSerializable<Timestamp>,
			   IsAlsoSerializable<CReplicaID>,
			   ISRCloneable<CReplicaID>,
			   ISAlsoRCloneable<Timestamp>
{
	
	public Ends(){
		//TODO: I think empty is correct here, but I'm not sure.
	}
	
	public synchronized boolean prec(Ends<CReplicaID> e){
		
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
	
	public synchronized Ends<CReplicaID> fast_forward(final Ends<CReplicaID> future){
		cassert(future != null, "future is null!");
		Ends<CReplicaID> stable = future.clone();
		for (CReplicaID cr : stable.keySet()){
			put(cr,Timestamp.update(get(cr), stable.get(cr)));
		}
		return this;
	}
	
	@Override
	public synchronized Ends<CReplicaID> merge(Ends<CReplicaID> e){
		if (e == null) return this;
		else return this.fast_forward(e);
	}
	
	@Override
	public synchronized Ends<CReplicaID> rclone(){
		Ends<CReplicaID> ret = new Ends<>();
		for (Map.Entry<CReplicaID, Timestamp> e : entrySet()){
			ret.put(e.getKey().rclone(), e.getValue().rclone());
		}
		return ret;
	}
	
	@Override
	public synchronized Ends<CReplicaID> clone(){
		return rclone();
	}
}
