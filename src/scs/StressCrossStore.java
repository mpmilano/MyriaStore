package scs;

import java.util.*;

import remote.*;

import fsstore.*;
import util.*;

public class StressCrossStore {
	final static int num_clients = 2;

	final StressClient[] sc = new StressClient[num_clients];

	public StressCrossStore() {
		Handle<SimpleCounter, consistency.Lin, access.ReadWrite, ?,?> h =
			FSStore.inst.newObject(new SimpleCounter(), "/tmp/fsstore/shared-lin-obj",FSStore.inst);
		Function<Void,Void> syncAll = new Function<Void,Void>(){
				@Override
				public Void apply(Void v){
					for (StressClient stc : sc){
						stc.sync();
					}
					return v;
				}
			};
		
		for (int i = 0; i < sc.length; ++i){
			sc[i] = new StressClient(h,syncAll);
		}

		for (int i = 0; i < 100000; ++i){
			for (StressClient stc : sc){
				stc.tick();
				if ((i - stc.id) % 4 == 0) stc.sync();
			}
		}
	}
}
