Description
===========

This is the native Java driver for djondb, this does not require any external dependencies and works in any platform supported by java.

Build
=====

To build the project you can use maven command:

mvn package

This will compile, pack and execute the tests, so be sure to have a djondb server instance running locally.

Usage
=====

Add the jar file as dependency of your project and start using it as explained in the documentation http://djondb.com

Example:

	try {
		DjondbConnection con = new DjondbConnection("localhost");
		if (con.open()) {
			Bson o = new Bson();
			o.add("name", "John");
			o.add("lastName", "Smith");
			o.add("age", 10);
			con.insert("djondbjava", "customers", o);

			DjondbCursor cur = con.find("djondbjava", "customers");
			cur.next();
			Bson res = cur.current();

			System.out.println(res.toString());
		}
	} catch (DjondbException e) {
		e.printStackTrace();
	}

