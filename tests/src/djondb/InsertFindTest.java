package djondb;

import djondb.*;
import java.util.ArrayList;

public class InsertFindTest {

    public static void main(String[] args) {
        InsertFindTest test = new InsertFindTest();
        test.test();
    }

    public void test() {
        try {
            Loader.initialize();

            DjondbConnection con = DjondbConnectionManager.getConnection("localhost");

            if (!con.open()) {
                System.out.println("Not connected");
                System.exit(1);
            }

            con.insert("dbjava", "nsjava", "{ 'name': 'John', 'lastName': 'Smith' }");

            DjondbCursor cursor = con.find("dbjava", "nsjava", "$'lastName' == 'Smith'");

				while (cursor.next()) {
					BSONObj bson = cursor.current();

					System.out.println("Hello: " + bson.getString("name"));
				}

				DjondbConnectionManager.releaseConnection(con);
		  } catch (LibraryException e) {
			  e.printStackTrace();
		  }
	 }
}
