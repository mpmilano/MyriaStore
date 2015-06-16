#define cassert(x,s) assert((new util.Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

package util;

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

public class SimpleCounter implements CausalSafe<SimpleCounter>,
										   Incrementable
	{
		private TreeSet<TwoTuple<String, Boolean>> i = new TreeSet<>();

		@Override
		public void incr() {
			i.add(new TwoTuple<>(NonceGenerator.get(),true));
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
		public boolean equals(Object o){
			if (o instanceof SimpleCounter){
				return toString() == o.toString();
			}
			else return false;
		}

		@Override
		public SimpleCounter merge(final SimpleCounter c){
			final int curr = get();
			if (c != null) i.addAll(c.i);
			cassert(c == SimpleCounter.this ? curr == get() : true, "Merge is not idempotent!");
			cassert(c == null || (get() >= c.get()), "Merge failed to actually incorporate c");
			cassert(c == null || (get() == c.get()) || ((c.merge(SimpleCounter.this)).get() == get()),"Merge fails to make counts equal");
			return this;
		}

		@Override
		public SimpleCounter rclone(){
			final SimpleCounter sc = new SimpleCounter();
			sc.i.addAll(i);
			cassert(get() == sc.get(), "Clone failed to create a clone!");
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
