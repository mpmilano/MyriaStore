
package operations;
import remote.*;
import java.io.*;
import java.util.*;

public interface ForEach<C extends consistency.Top, FSDir extends RemoteObject<? extends Collection<?>, ? >, Store >{
	public <Out, T extends Serializable, A extends access.Unknown>
		void foreach(OperationFactory<Out,C, Handle<T,C,A,C, Store> > of, FSDir fs);
}
