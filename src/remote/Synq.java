package remote;

public interface Synq<Replica>{
	public boolean sync_req(Replica from, Replica to);
	
}
