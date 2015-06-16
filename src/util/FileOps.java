package util;

import java.io.*;

public class FileOps{

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T readFromFS(File where) throws ClassNotFoundException, FileNotFoundException, IOException{
		FileInputStream fis = (new FileInputStream(where));
		ObjectInputStream ois = (new ObjectInputStream(fis));
		try{
			return (T) ois.readObject();
		}
		finally {
			ois.close();
			fis.close();
		}
	}

	public static void writeToFS(Serializable s, File where) {
		try {
			FileOutputStream fos = (new FileOutputStream(where));
			ObjectOutputStream oos = (new ObjectOutputStream(fos));
			oos.writeObject(s);
			oos.close();
			fos.close();
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
