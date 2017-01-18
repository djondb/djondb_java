package com.djondb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DjondbConnectionTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DjondbConnectionTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DjondbConnectionTest.class );
    }


	 private void printLog(String message) {
		 System.out.println(message);
	 }

    public void testShowDbs()
    {
		 printLog("testShowDbs");
		 try {
			 DjondbConnection con = new DjondbConnection("localhost");
			 assertTrue(con.open());

			 String[] dbs = con.showDbs();

			 assertTrue(dbs.length > 0);
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testShowNamespaces()
	 {
		 printLog("testShowNamespaces");
		 try {
			 DjondbConnection con = new DjondbConnection("localhost");
			 assertTrue(con.open());

			 String[] nss = con.showNamespaces("db");

			 assertTrue(nss.length > 0);
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testDropNamespace()
	 {
		 printLog("testDropNamespace");
		 try {
			 DjondbConnection con = new DjondbConnection("localhost");
			 assertTrue(con.open());
			 assertTrue(con.dropNamespace("dbjavacon", "nsx"));
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testInsertFindRemove()
	 {
		 printLog("testInsertFindRemove");
		 try {
			 DjondbConnection con = new DjondbConnection("localhost");
			 assertTrue(con.open());
			 con.dropNamespace("dbjavacon", "insertfindremove");

			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, con.insert("dbjavacon", "insertfindremove", o));

			 DjondbCursor cur = con.find("dbjavacon", "insertfindremove", "*", "");
			 assertTrue(cur.next());
			 Bson res = cur.current();

			 con.remove("dbjavacon", "insertfindremove", res.getString("_id"), res.getString("_revision"));
			 cur = con.find("dbjavacon", "insertfindremove", "*", "");
			 assertFalse(cur.next());
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testInsert()
	 {
		 printLog("testInsert");
		 try {
			 DjondbConnection con = new DjondbConnection("localhost");
			 assertTrue(con.open());
			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, con.insert("dbjavacon", "insert", o));
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testInsertFind()
	 {
		 printLog("testInsertFind");
		 try {
			 DjondbConnection con = new DjondbConnection("localhost");
			 assertTrue(con.open());
			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, con.insert("dbjavacon", "find", o));

			 DjondbCursor cur = con.find("dbjavacon", "find", "*", "");
			 assertTrue(cur.next());
			 Bson res = cur.current();

			 res.remove("_id");
			 res.remove("_revision");
			 assertEquals(o, res);
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testUpdate()
	 {
		 printLog("testUpdate");
		 try {
			 DjondbConnection con = new DjondbConnection("localhost");
			 assertTrue(con.open());
			 con.dropNamespace("dbjavacon", "update");

			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, con.insert("dbjavacon", "update", o));

			 DjondbCursor cur = con.find("dbjavacon", "update", "*", "");
			 assertTrue(cur.next());
			 Bson toUpdate = cur.current();

			 toUpdate.add("other", 1);

			 con.update("dbjavacon", "update", toUpdate);
			 cur = con.find("dbjavacon", "update", "*", "");
			 assertTrue(cur.next());
			 Bson updatedRes = cur.current();

			 toUpdate.remove("_id");
			 toUpdate.remove("_revision");

			 updatedRes.remove("_id");
			 updatedRes.remove("_revision");
			 updatedRes.remove("__revisionOld");

			 assertEquals(toUpdate, updatedRes); 
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testTX()
	 {
		 printLog("testTX");
		 try {
			 DjondbConnection con = new DjondbConnection("localhost");
			 assertTrue(con.open());
			 con.dropNamespace("dbjavacon", "testtx");

			 con.beginTransaction();

			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, con.insert("dbjavacon", "testtx", o));

			 DjondbCursor cur = con.find("dbjavacon", "testtx", "*", "");
			 assertTrue(cur.next());
			 Bson res = cur.current();

			 res.remove("_id");
			 res.remove("_revision");
			 assertEquals(o, res);
			 con.commitTransaction();

		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }
}

