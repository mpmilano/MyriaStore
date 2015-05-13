package consistency;
import java.io.*;
import util.*;

public interface CausalSafe<T> extends Serializable, Mergable<T>, RCloneable<T> {
	
}
