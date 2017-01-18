package com.djondb;

import java.util.List;
import java.util.ArrayList;

public class DjondbCursor {
	private enum CursorStatus {
		CS_LOADING,
		CS_RECORDS_LOADED,
		CS_CLOSED
	}

	private Network _net;
	private String _cursorId;
	private List<Bson> _rows;
	private int _position;
	private Bson _current;
	private int _count;
	private CursorStatus _status;

	public DjondbCursor(Network net, String cursorId, List<Bson> firstPage) {
		this._net = net;
		this._cursorId = cursorId;
		this._rows = firstPage;
		this._position = 0;
		this._current = null;

		if (this._rows == null) {
			this._count = 0;
		} else {
			this._count = this._rows.size();
		}

		if (cursorId != null) {
			this._status = CursorStatus.CS_LOADING;
		} else {
			this._status = CursorStatus.CS_RECORDS_LOADED;
		}
	}

	public boolean next() throws DjondbException {
		if (this._status == CursorStatus.CS_CLOSED) {
			throw new DjondbException("Cursor is closed");
		}
		boolean result = true;
		if (this._count > this._position) {
			this._current = this._rows.get(this._position);
			this._position += 1;
		} else {
			if (this._status == CursorStatus.CS_LOADING) {
				Command cmd = new Command(this._net);
				List<Bson> page = cmd.fetchRecords(this._cursorId);
				if (page == null) {
					this._status = CursorStatus.CS_RECORDS_LOADED;
					result = false;
				} else {
					this._rows.addAll(page);
					this._count = this._rows.size();
					result = this.next();
				}
			} else {
				result = false;
			}
		}
		return result;
	}

	public boolean previous() throws DjondbException {
		if (this._status == CursorStatus.CS_CLOSED) {
			throw new DjondbException("Cursor is closed");
		}
		boolean result = true;
		if ((this._count > 0) && (this._position > 0)) {
			this._position -= 1;
			this._current = this._rows.get(this._position);
		} else {
			result = false;
		}
		return result;
	}

	public Bson current (){
		return this._current;
	}
}

