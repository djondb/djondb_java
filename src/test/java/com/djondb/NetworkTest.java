package com.djondb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class NetworkTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NetworkTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( NetworkTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testStreams()
    {
		 System.out.println("testStreams()");
		 try {
			 Network n = new Network();
			 n.writeInt(1);
			 n.writeLong(10000L);
			 n.writeString("Hello World");
			 n.writeBoolean(true);
			 n.writeDouble(2.45);

			 Bson o = new Bson();
			 o.add("name", "John");
			 o.add("lastName", "Smith");
			 o.add("age", 10);
			 n.writeBSON(o);

			 n.seek(0);
			 int i = n.readInt();
			 assertEquals(1, i);
			 long l = n.readLong();
			 assertEquals(10000L, l);
			 String s = n.readString();
			 assertEquals("Hello World", s);
			 boolean b = n.readBoolean();
			 assertEquals(true, b);
			 double d = n.readDouble();
			 assertEquals(2.45, d);
			 Bson bson = n.readBSON();

			 assertEquals(bson, o);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 /*
			 $s = new \djondb_php\Network();
			 $s->writeInt(1);
			 $text = "Hello";
			 $s->writeString($text);
			 $b = True;
			 $s->writeBoolean($b);
			 $d = 2323223.23232;
			 $s->writeDouble($d);
			 $l = 10;
			 $s->writeLong($l);
			 $l2 = PHP_INT_MAX;
			 $s->writeLong($l2);

			 $obj = (object)array("name" => "John", "lastName" => "Smith", "age" => 10);
			 $s->writeBSON($obj);

			 $arr = array($obj, $obj);
			 $s->writeBSONArray($arr);

			 $s->seek(0);

			 $res = $s->readInt();
			 $this->assertEquals(1, $res);
			 $res = $s->readString();
			 $this->assertEquals($text, $res);
			 $res = $s->readBoolean();
			 $this->assertEquals($b, $res);
			 $res = $s->readDouble();
			 $this->assertEquals($d, $res);
			 $res = $s->readLong();
			 $this->assertEquals($l, $res);
			 $res = $s->readLong();
			 $this->assertEquals($l2, $res);
			 $res = $s->readBSON();
			 $this->assertEquals($obj, $res);
			 $res = $s->readBSONArray();
			 $this->assertEquals($arr, $res);
			 */
		 assertTrue( true );
	 }
}

