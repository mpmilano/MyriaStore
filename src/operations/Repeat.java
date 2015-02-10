#include "../remote/BackingStore.h"
package operations;

public class Repeat<R_(T), OpBasics(Object), Factory extends OperationFactory<T,C,H>> extends Operation<T,C> {
	private H h;
	private Factory f;
	private int times;
	public Repeat(Factory f, int times, H h){
		this.h = h;
		this.f = f;
		this.times = times;
	}

	@Override
	public T execute(){
		for (int i = 0; i < (times -1); ++i) f.build(h).execute();
		return f.build(h).execute();
	}
}
