package com.djondb;

public class DjondbException extends Exception {
	private int _code;
	private String _message;
	private Exception _parent;

	public DjondbException(int code, String message) {
		this._code = code;
		this._message = message;
	}

	public DjondbException(String message) {
		this._message = message;
	}

	public DjondbException(Exception e) {
		super(e);
		_parent = e;
	}

	public String toString() {
		if (_parent != null) {
			return _parent.toString();
		} else {
			if (_code > 0) {
				return "Error code: " + _code + ". Message: " + _message;
			} else {
				return "Message: " + _message;
			}
		}
	}
}
