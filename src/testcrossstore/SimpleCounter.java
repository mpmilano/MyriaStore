#define cassert(x,s) assert((new util.Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

package testcrossstore;

import remote.*;
import fsstore.*;
import logstore.*;
import simplecausal.*;
import operations.*;
import java.util.*;
import util.*;
import transactions.*;
import consistency.*;
import java.io.*;

class SimpleCounter implements CausalSafe<SimpleCounter>,
										   Incrementable
	{
		private TreeSet<TwoTuple<String, Boolean>> i = new TreeSet<>();

		@Override
		public void incr() {
			i.add(new TwoTuple<>(NonceGenerator.get(),true));
			synchronized(synchlock){
				for (TwoTuple<String,Boolean> tt : i){
					System.out.println("  " + tt.a + ": " + tt.b);
				}
			}
		}

		@Override
		public void decr() {
			i.add(new TwoTuple<>(NonceGenerator.get(),false));
		}
		public int get() {
			int i = 0;
			for (TwoTuple<String, Boolean> e : this.i){
				if (e.b) ++i;
				else --i;
			}
			return i;
		}

		@Override
		public SimpleCounter merge(SimpleCounter c){
			//System.out.println("merge");
			final int curr = get();
			if (c != null) i.addAll(c.i);
			cassert(c == this ? curr == get() : true, "Merge is not idempotent!");
			cassert(c == null || (this.get() >= c.get()), "Merge failed to actually incorporate c");
			cassert((get() == c.get()) || ((c.merge(this)).get() == this.get()),"Merge fails to make counts equal");
			return this;
		}

		@Override
		public SimpleCounter rclone(){
			SimpleCounter sc = new SimpleCounter();
			sc.i.addAll(i);
			cassert(this.get() == sc.get(), "Clone failed to create a clone!");
			return sc;
		}

		@Override
		public String toString(){
			String ret =  get() + "";
			for (TwoTuple<String,Boolean> tt : i){
				ret += ("  " + tt.a + ": " + tt.b);
			}
			return ret;
		}
	}
