package util;

import java.io.*;
import java.util.*;

public interface SerializableCollection<T extends Serializable> extends Serializable, Collection<T> {}

