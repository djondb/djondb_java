package com.djondb;

public class DjondbConnection {
	private String _host;
	private int _port = 1243;
	private Command _cmd;
	private Network _network;
	private String _activeTransactionId;

	public DjondbConnection(String host, int port) {
		this._host = host;
		this._port = port;
	}

	public DjondbConnection(String host) {
		this._host = host;
	}

	private void checkError(Command _cmd) throws DjondbException {
		if (this._cmd.resultCode() > 0) {
			throw new DjondbException(this._cmd.resultCode(), this._cmd.resultMessage());
		}
	}

	public boolean open() {
		this._network = new Network();
		boolean res = this._network.connect(this._host, this._port);
		if (res) {
			this._cmd = new Command(this._network);
		}
		return res;
	}

	public String[] showDbs() throws DjondbException {
		String[] res= this._cmd.showDbs();
		this.checkError(this._cmd);
		return res;
	}

	public String[] showNamespaces(String db) throws DjondbException {
		String[] res = this._cmd.showNamespaces(db);
		this.checkError(this._cmd);
		return res;
	}

	public int insert(String db, String ns, Bson data) throws DjondbException {
		int res = this._cmd.insert(db, ns, data);
		this.checkError(this._cmd);
		return res;
	}

	public boolean update(String db, String ns, Bson data) throws DjondbException {
		boolean res = this._cmd.update(db, ns, data);
		this.checkError(this._cmd);
		return res;
	}

	public DjondbCursor find(String db, String ns) throws DjondbException {
		return find(db, ns, "*", "");
	}

	public DjondbCursor find(String db, String ns, String select) throws DjondbException {
		return find(db, ns, select, "");
	}

	public DjondbCursor find(String db, String ns, String select, String filter) throws DjondbException {
		DjondbCursor res =this._cmd.find(db, ns, select, filter);
		this.checkError(this._cmd);
		return res;
	}

	public boolean dropNamespace(String db, String ns) throws DjondbException {
		boolean res =this._cmd.dropNamespace(db, ns);
		this.checkError(this._cmd);
		return res;
	}

	public boolean remove(String db, String ns, String id) throws DjondbException {
		return remove(db, ns, id, null);
	}

	public boolean remove(String db, String ns, String id, String revision) throws DjondbException {
		boolean res = this._cmd.remove(db, ns, id, revision);
		this.checkError(this._cmd);
		return res;
	}

	public void beginTransaction() {
		this._activeTransactionId = this._cmd.beginTransaction();
	}
		
	public void commitTransaction() throws DjondbException {
		this._cmd.commitTransaction();
		this._activeTransactionId = null;
		this.checkError(this._cmd);
	}
		
	public void rollbackTransaction() throws DjondbException {
		this._cmd.rollbackTransaction();
		this._activeTransactionId = null;
		this.checkError(this._cmd);
	}

	public void createIndex(Bson indexDef) throws DjondbException {
		this._cmd.createIndex(indexDef);
		this.checkError(this._cmd);
	}

	public DjondbCursor executeQuery(String query) throws DjondbException {
		DjondbCursor res =this._cmd.executeQuery(query);
		this.checkError(this._cmd);
		return res;
	}

	public boolean executeUpdate(String query) throws DjondbException {
		boolean res =this._cmd.executeUpdate(query);
		this.checkError(this._cmd);
		return res;
	}
}
