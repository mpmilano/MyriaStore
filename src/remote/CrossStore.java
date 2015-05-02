package remote;
import consistency.*;
import util.*;
import java.io.*;
import java.util.*;

#define CausalStore Store<Causal, CausalObj, CausalType, CausalP>
#define LinStore Store<Lin, LinObj, LinType, LinP>
	
public class CrossStore<CausalObj extends RemoteObject, CausalType, CausalP, LinObj extends RemoteObject, LinType, LinP>{
	//private CausalStore causal;
	//private LinStore lin;

	private class ReadSetPair implements Serializable{
		{
			#define ReplicaID int 
			#define Nonce String
		}
		public final ReplicaID replica;
		public final Nonce operation_nonce;
		public ReadSetPair(final ReplicaID r, final Nonce s){
			replica = r;
			operation_nonce = s;
		}
	}

	private class MetaData implements Serializable{
		Set<ReadSetPair> readfrom;
		Nonce n;
		ReplicaID natural_replica;
		public final Set<?> ends;
		public MetaData(Set<ReadSetPair> readfrom, Nonce n, ReplicaID natural_replica){
			this.readfrom = readfrom; this.n = n; this.natural_replica = natural_replica;
		}
	}

	private class Tombstone implements Serializable {
		public final Nonce n;
		public final CausalType name;

		public Tombstone(Nonce n){
			this.n = n;
			this.name = c.concat(c.ofString("tombstone-"), c.ofString(n));
		}

	}
	
	public CrossStore(final CausalStore c, final LinStore l){
		//causal = c;
		//lin = l;

		final Set<?/*timestamps? */> ends = null;
		final Set<ReadSetPair> readset = new TreeSet<>();
		final ReplicaID natural_replica = 0; //TODO: ? 
		final LinType metadata_suffix = l.ofString("metadata");

		l.registerOnWrite(new Function<LinType,Void>(){
				@Override
				public Void apply(LinType name){
					Nonce n = NonceGenerator.get();
					{
						LinType meta_name =
							l.concat(name,metadata_suffix);
						TreeSet<ReadSetPair> rscopy = new TreeSet<>();
						rscopy.addAll(readset);
						rscopy.add(new ReadSetPair(natural_replica, n));
						//annotate with metadata
						l.newObject(new MetaData(rscopy, n, natural_replica, ends),
									meta_name, l);
					}
					//write tombstone
					{
						Tombstone t = new Tombstone(n);
						c.newObject(t, t.name);
					}
					
					return null;
				}
			});
		
		l.registerOnRead(new Function<LinType,Void>(){
				@Override
				public Void apply(LinType name){
					LinType meta_name =
						l.concat(name,metadata_suffix);
					RemoteObject<MetaData> rmeta =
						(RemoteObject<MetaData>) l.existingObject(meta_name);
					MetaData meta = rmeta.get();
					if (!ends_prec(meta.ends, ends)) {
						...
					}
					return null;
				}
			});


		c.registerOnWrite(new Function<CausalType,Void>(){
				@Override
				public Void apply(CausalType name){
					
					return null;
				}
			});
		c.registerOnRead(new Function<CausalType,Void>(){
				@Override
				public Void apply(CausalType name){

					return null;
				}
			});
		c.registerOnTick(new Runnable(){
				@Override
				public void run(){
					
				}
			});
	}
}
