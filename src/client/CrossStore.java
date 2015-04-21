package client;
import remote.*;
import consistency.*;
import util.*;
import java.io.*;
import java.util.*;

#define CausalStore Store<Causal, CausalObj, CausalType, CausalP>
#define LinStore Store<Lin, LinObj, LinType, LinP>
	
public class CrossStore<CausalObj extends RemoteObject, CausalType, CausalP, LinObj extends RemoteObject, LinType, LinP>{
	private CausalStore causal;
	private LinStore lin;

	public CrossStore(CausalStore c, LinStore l){
		causal = c;
		lin = l;

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
