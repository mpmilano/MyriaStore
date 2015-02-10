
package operations;
import remote.*;
import java.io.*;
import java.util.*;

public interface ForEach<C extends consistency.Top, FSDir extends RemoteObject<? extends Collection<?> > >{
	public <Out, T extends Serializable, A extends access.Unknown>
		void foreach(OperationFactory<Out,T, C, Handle<T,C,A,C> > of, FSDir fs);
}
