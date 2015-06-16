package util;

import java.io.*;

public class FileOps{

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T readFromFS(File where) throws ClassNotFoundException, FileNotFoundException, IOException{
		return (T) (new ObjectInputStream(new FileInputStream(where))).readObject();
	}

	public static void writeToFS(Serializable s, File where) {
		try {
			(new ObjectOutputStream(new FileOutputStream(where))).writeObject(s);
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	public static boolean exists(String where){
		try{
			readFromFS(new File(where));
			return true;
		}
		catch (FileNotFoundException e){
			return false;
		}
		catch (ClassNotFoundException e){
			return true;
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}
}
