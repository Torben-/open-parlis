package data;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;


public class Database {
	public static Object readData() {
		Object data = null;
		return data;
	}
	
	public static void writeData(Object data) {
		String dbSaveFile = "db/parlis.db";
		ObjectContainer db1 = Db4o.openFile(dbSaveFile);

		try {
			db1.set(data);
		}
		finally {
			db1.close();
		}
	}
}
