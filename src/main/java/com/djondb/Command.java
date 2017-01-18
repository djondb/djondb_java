package com.djondb;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class Command {
	public enum CommandType {
		INSERT(0),
			UPDATE(1),
			FIND(2),
			CLOSECONNECTION(3),
			DROPNAMESPACE(4),
			SHUTDOWN(5),
			SHOWDBS(6),
			SHOWNAMESPACES(7),
			REMOVE(8),
			COMMIT(9),
			ROLLBACK(10),
			FETCHCURSOR(11),
			FLUSHBUFFER(12),
			CREATEINDEX(13),
			BACKUP(14),
			RCURSOR(15),
			PERSISTINDEXES(16),
			EXECUTEQUERY(17),
			EXECUTEUPDATE(18);

		private int _value;
		private static Map<Integer, CommandType> _map = new HashMap<Integer, CommandType>();

		static {
			for (CommandType type : CommandType.values()) {
				_map.put(type._value, type);
			}
		}

		private CommandType(int value) {
			this._value = value;
		}

		public int value() {
			return _value;
		}

		public static CommandType valueOf(int ctype) {
			return _map.get(ctype);
		}
	};

	private Network _net;
	private String _activeTransactionId;
	private int _resultCode;
	private String _resultMessage;;

	public Command(Network net) {
		this._net = net;
		this._activeTransactionId = null;
		this._resultCode = 0;
		this._resultMessage = null;
	}

	public int resultCode() {
		return _resultCode;
	}

	public String resultMessage() {
		return _resultMessage;
	}

	public void writeHeader() {
		String version = "3.5.60822";
		this._net.writeString(version);
	}

	public void readErrorInformation() throws Exception {
		this._resultCode = this._net.readInt();
		if (this._resultCode > 0) {
			this._resultMessage = this._net.readString();
		}
	}

	public void writeOptions() throws DjondbException {
		Bson options = new Bson();
		if (this._activeTransactionId != null) {
			options.add("_transactionId", this._activeTransactionId);
		}
		this._net.writeBSON(options);
	}

	public String[] readResultShowDbs() throws Exception {
		int results = this._net.readInt();

		java.util.List<String> dbs = new java.util.ArrayList<String>();
		for (int x = 0; x < results; x++) {
			String db = this._net.readString();
			dbs.add(db);
		}

		this.readErrorInformation();

		String[] res = new String[dbs.size()];
		return dbs.toArray(res);
	}

	public String[] showDbs() throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.SHOWDBS.value());
			this.writeOptions();
			this._net.flush();

			return readResultShowDbs();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public String[] readResultShowNamespaces() throws Exception {
		int results = this._net.readInt();

		java.util.List<String> nss = new java.util.ArrayList<String>();
		for (int x = 0; x < results; x++) {
			String ns = this._net.readString();
			nss.add(ns);
		}

		this.readErrorInformation();

		String[] res = new String[nss.size()];
		return nss.toArray(res);
	}

	public String[] showNamespaces(String db) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.SHOWNAMESPACES.value());
			this.writeOptions();
			this._net.writeString(db);
			this._net.flush();

			return readResultShowNamespaces();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public boolean readResultDropNamespace() throws Exception {
		int result = this._net.readInt();

		this.readErrorInformation();
		return true;
	}

	public boolean dropNamespace(String db, String ns) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.DROPNAMESPACE.value());
			this.writeOptions();
			this._net.writeString(db);
			this._net.writeString(ns);
			this._net.flush();

			return readResultDropNamespace();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public boolean readResultRemove() throws Exception {
		boolean result = this._net.readBoolean();

		this.readErrorInformation();
		return result;
	}

	public boolean remove(String db, String ns, String id) throws DjondbException {
		return remove(db, ns, id, "");
	}

	public boolean remove(String db, String ns, String id, String revision) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.REMOVE.value());
			this.writeOptions();
			this._net.writeString(db);
			this._net.writeString(ns);
			this._net.writeString(id);
			this._net.writeString(revision);
			this._net.flush();

			return readResultRemove();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public int readResultInsert() throws Exception {
		int result = this._net.readInt();

		this.readErrorInformation();
		return result;
	}

	public int insert(String db, String ns, Bson obj) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.INSERT.value());
			this.writeOptions();
			this._net.writeString(db);
			this._net.writeString(ns);
			this._net.writeBSON(obj);
			this._net.flush();

			return readResultInsert();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public boolean readResultUpdate() throws Exception {
		boolean result = this._net.readBoolean();

		this.readErrorInformation();
		return result;
	}

	public boolean update(String db, String ns, Bson obj) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.UPDATE.value());
			this.writeOptions();
			this._net.writeString(db);
			this._net.writeString(ns);
			this._net.writeBSON(obj);
			this._net.flush();

			return readResultUpdate();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public DjondbCursor readResultFind() throws Exception {
		String cursorId = this._net.readString();
		int flag = this._net.readInt();

		List<Bson> results = new ArrayList<Bson>();
		if (flag == 1) {
			results = this._net.readBSONArray();
		}

		DjondbCursor result = new DjondbCursor(this._net, cursorId, results);
		this.readErrorInformation();
		return result;
	}

	public DjondbCursor find(String db, String ns, String select, String filter) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.FIND.value());
			this.writeOptions();
			this._net.writeString(db);
			this._net.writeString(ns);
			this._net.writeString(filter);
			this._net.writeString(select);
			this._net.flush();

			return readResultFind();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public List<Bson> readResultFetchRecords() throws Exception {
		int flag = this._net.readInt();

		List<Bson> results = null;
		if (flag == 1) {
			results = this._net.readBSONArray();
		}

		this.readErrorInformation();
		return results;
	}

	public List<Bson> fetchRecords(String cursorId) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.FETCHCURSOR.value());
			this.writeOptions();
			this._net.writeString(cursorId);
			this._net.flush();

			return readResultFetchRecords();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public String beginTransaction() {
		UUID uid = UUID.randomUUID();
		this._activeTransactionId = uid.toString();

		return this._activeTransactionId;
	}	

	public void readResultCommitTransaction() throws Exception {
		this.readErrorInformation();
	}

	public void commitTransaction() throws DjondbException {
		try {
			if (this._activeTransactionId != null) {
				this._net.reset();
				this.writeHeader();
				this._net.writeInt(CommandType.COMMIT.value());
				this.writeOptions();
				this._net.writeString(this._activeTransactionId);
				this._net.flush();

				readResultCommitTransaction();
				this._activeTransactionId = null;
			} else {
				throw new DjondbException(10001, "Nothing to commit, you need beginTransaction before committing or rollback");
			}
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public void readResultRollbackTransaction() throws Exception {
		this.readErrorInformation();
	}

	public void rollbackTransaction() throws DjondbException {
		try {
			if (this._activeTransactionId != null) {
				this._net.reset();
				this.writeHeader();
				this._net.writeInt(CommandType.ROLLBACK.value());
				this.writeOptions();
				this._net.writeString(this._activeTransactionId);
				this._net.flush();

				readResultCommitTransaction();
				this._activeTransactionId = null;
			} else {
				throw new DjondbException(10001, "Nothing to rollback, you need beginTransaction before committing or rollback");
			}
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public void readResultCreateIndex() throws Exception {
		this.readErrorInformation();
	}

	public void createIndex(Bson indexDef) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.CREATEINDEX.value());
			this.writeOptions();
			this._net.writeBSON(indexDef);
			this._net.flush();

			readResultCreateIndex();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public int readResultBackup() throws Exception {
		int res = this._net.readInt();

		this.readErrorInformation();
		return res;
	}

	public int backup(String db, String destFile) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.BACKUP.value());
			this.writeOptions();
			this._net.writeString(db);
			this._net.writeString(destFile);
			this._net.flush();

			return readResultBackup();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	private DjondbCursor readResultExecuteQuery() throws DjondbException {
		try {
			int flag = this._net.readInt();
			DjondbCursor cursorResult = null;
			String cursorId = null;
			if (flag == 1) {
				CommandType commandType = CommandType.valueOf(this._net.readInt());
				switch (commandType) {
					case INSERT:
						this.readResultInsert();
						break;

					case UPDATE:
						this.readResultUpdate();
						break;

					case FIND:
						cursorResult = this.readResultFind();
						break;

					case DROPNAMESPACE:
						this.readResultDropNamespace();
						break;

					case SHOWDBS:
						String[] dbs = this.readResultShowDbs();
						this.readErrorInformation();
						List<Bson> arrDbs = new ArrayList<Bson>();
						for (String db : dbs) {
							Bson row = new Bson();
							row.add("db", db);
							arrDbs.add(row);
						}
						cursorResult = new DjondbCursor(this._net, cursorId, arrDbs);
						break;

					case SHOWNAMESPACES:
						String[] nss = this.readResultShowNamespaces();
						this.readErrorInformation();
						List<Bson> arrNs = new ArrayList<Bson>();
						for (String ns : nss) {
							Bson row = new Bson();
							row.add("ns", ns);
							arrNs.add(row);
						}
						cursorResult = new DjondbCursor(this._net, cursorId, arrNs);
						break;

					case REMOVE:
						this.readResultRemove();
						break;

					case COMMIT:
						this.readResultCommitTransaction();
						this._activeTransactionId = null;
						break;

					case ROLLBACK:
						this.readResultRollbackTransaction();
						this._activeTransactionId = null;
						break;

					case FETCHCURSOR:
						// Unsupported from ExecuteQuery, discard results
						this.readResultFetchRecords();
						break;

					case CREATEINDEX:
						this.readResultCreateIndex();
						break;

					case BACKUP:
						this.readResultBackup();
						break;
				}
			} else {
				this.readErrorInformation();
			}

			if (cursorResult == null) {
				List<Bson> arr = new ArrayList<Bson>();
				Bson row = new Bson();
				java.util.Date now = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				row.add("date", dateFormat.format(now));
				row.add("success", true);
				arr.add(row);
				cursorResult = new DjondbCursor(this._net, null, arr);
			}

			return cursorResult;
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public DjondbCursor executeQuery(String query) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.EXECUTEQUERY.value());
			this.writeOptions();
			this._net.writeString(query);
			this._net.flush();

			return readResultExecuteQuery();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	private boolean readResultExecuteUpdate() throws DjondbException {
		try {
			int flag = this._net.readInt();
			if (flag == 1) {
				CommandType commandType = CommandType.valueOf(this._net.readInt());
				switch (commandType) {
					case INSERT:
						this.readResultInsert();
						break;

					case UPDATE:
						this.readResultUpdate();
						break;

					case DROPNAMESPACE:
						this.readResultDropNamespace();
						break;

					case REMOVE:
						this.readResultRemove();
						break;

					case COMMIT:
						this.readResultCommitTransaction();
						this._activeTransactionId = null;
						break;

					case ROLLBACK:
						this.readResultRollbackTransaction();
						this._activeTransactionId = null;
						break;

					case CREATEINDEX:
						this.readResultCreateIndex();
						break;

					case BACKUP:
						this.readResultBackup();
						break;
				}
			} else {
				this.readErrorInformation();
			}

			return true;
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}

	public boolean executeUpdate(String query) throws DjondbException {
		try {
			this._net.reset();
			this.writeHeader();
			this._net.writeInt(CommandType.EXECUTEUPDATE.value());
			this.writeOptions();
			this._net.writeString(query);
			this._net.flush();

			return readResultExecuteUpdate();
		} catch (java.io.IOException e) {
			throw new DjondbException(e.getMessage());
		} catch (Exception e) {
			throw new DjondbException(e.getMessage());
		}
	}
}
