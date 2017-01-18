package com.djondb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class CommandTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CommandTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CommandTest.class );
    }


	 private void printLog(String message) {
		 System.out.println(message);
	 }

    public void testShowDbs()
    {
		 printLog("testShowDbs");
		 try {
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 String[] dbs = cmd.showDbs();

			 assertTrue(dbs.length > 0);
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testShowNamespaces()
	 {
		 printLog("testShowNamespaces");
		 try {
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 String[] nss = cmd.showNamespaces("db");

			 assertTrue(nss.length > 0);
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testDropNamespace()
	 {
		 printLog("testDropNamespace");
		 try {
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 assertTrue(cmd.dropNamespace("dbjava", "nsx"));
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testInsert()
	 {
		 printLog("testInsert");
		 try {
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, cmd.insert("dbjava", "insert", o));
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testFind()
	 {
		 printLog("testInsertFind");
		 try {
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, cmd.insert("dbjava", "insert", o));

			 DjondbCursor cur = cmd.find("dbjava", "insert", "*", "");
			 assertTrue(cur.next());
			 Bson res = cur.current();

			 res.remove("_id");
			 res.remove("_revision");
			 assertEquals(o, res);
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testInsertFind()
	 {
		 printLog("testInsertFind");
		 try {
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, cmd.insert("dbjava", "insertfind", o));

			 DjondbCursor cur = cmd.find("dbjava", "insertfind", "*", "");
			 assertTrue(cur.next());
			 Bson res = cur.current();

			 for (String key : res.keys()) {
				 System.out.println("Key: " + key + ", value: " + res.get(key));
			 }

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
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 cmd.dropNamespace("dbjava", "update");

			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, cmd.insert("dbjava", "update", o));

			 DjondbCursor cur = cmd.find("dbjava", "update", "*", "");
			 assertTrue(cur.next());
			 Bson toUpdate = cur.current();

			 toUpdate.add("other", 1);

			 cmd.update("dbjava", "update", toUpdate);
			 cur = cmd.find("dbjava", "update", "*", "");
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

	 public void testInsertFindRemove()
	 {
		 printLog("testInsertFindRemove");
		 try {
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 cmd.dropNamespace("dbjava", "insertfindremove");

			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, cmd.insert("dbjava", "insertfindremove", o));

			 DjondbCursor cur = cmd.find("dbjava", "insertfindremove", "*", "");
			 assertTrue(cur.next());
			 Bson res = cur.current();

			 cmd.remove("dbjava", "insertfindremove", res.getString("_id"), res.getString("_revision"));
			 cur = cmd.find("dbjava", "insertfindremove", "*", "");
			 assertFalse(cur.next());
		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

	 public void testTX()
	 {
		 printLog("testTX");
		 try {
			 Network net = new Network();
			 assertTrue(net.connect("localhost", 1243));

			 Command cmd = new Command(net);
			 cmd.dropNamespace("dbjava", "testtx");

			 cmd.beginTransaction();

			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 assertEquals(1, cmd.insert("dbjava", "testtx", o));

			 DjondbCursor cur = cmd.find("dbjava", "testtx", "*", "");
			 assertTrue(cur.next());
			 Bson res = cur.current();

			 res.remove("_id");
			 res.remove("_revision");
			 assertEquals(o, res);

			 cmd.commitTransaction();

		 } catch (DjondbException e) {
			 e.printStackTrace();
		 }
	 }

}

