package djondb;

import djondb.*;
import java.util.ArrayList;

public class HelloWorld {

	public static void main(String[] args) {
		HelloWorld test = new HelloWorld();
		test.helloWorld();
	}

	public void helloWorld() {
		try {
			Loader.initialize();
		} catch (LibraryException e) {
			e.printStackTrace();
         return;
		}
      try {

			BSONObj o = BSONParser.parse("a");
         throw new java.lang.Error("An exception should be thrown here");
		} catch (djondb.ParseException e) {
         System.out.println("Exception: " + e.getMessage2());
		}
		try {
			DjondbConnection con = DjondbConnectionManager.getConnection("localhost");

			if (!con.open()) {
				System.out.println("Not connected");
				System.exit(0);
			}

			con.insert("dbjava", "nsjava", "{ 'name': 'John', 'lastName': 'Smith' }");

			DjondbCursor cursor = con.executeQuery("select name, lastName from dbjava:nsjava where lastName == 'Smith'");
			while (cursor.next()) {
				BSONObj bson = cursor.current();

				System.out.println("Hello: " + bson.getString("name"));

			}
			DjondbConnectionManager.releaseConnection(con);
		} catch (DjondbException e) {
			e.printStackTrace();
		}
	}
}
