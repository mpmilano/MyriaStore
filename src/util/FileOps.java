package util;

import java.io.*;

public class FileOps{

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T readFromFS(File where) throws ClassNotFoundException, FileNotFoundException, IOException{
		return (T) (new ObjectInputStream(new FileInputStream(where))).readObject();
	}

}
