package com.djondb;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Bson {
	private Map<String, Object> _data;

	public Bson() {
		_data = new HashMap<String, Object>();
	}

	public Bson add(String name, int i) {
		_data.put(name, Integer.valueOf(i));
		return this;
	}

	public int getInt(String name) {
		if (_data.containsKey(name)) {
			Object o = _data.get(name);

			if (o instanceof Integer) {
				return ((Integer)o).intValue();
			} else {
				throw new IllegalArgumentException("Data type is not integer.");
			}
		} else {
			throw new IllegalArgumentException(name + " is not in the bson");
		}
	}

	public Bson add(String name, long l) {
		_data.put(name, Long.valueOf(l));
		return this;
	}

	public long getLong(String name) {
		if (_data.containsKey(name)) {
			Object o = _data.get(name);

			if (o instanceof Long) {
				return ((Long)o).longValue();
			} else {
				throw new IllegalArgumentException("Data type is not long.");
			}
		} else {
			throw new IllegalArgumentException(name + " is not in the bson");
		}
	}

	public Bson add(String name, double d) {
		_data.put(name, Double.valueOf(d));
		return this;
	}

	public double getDouble(String name) {
		if (_data.containsKey(name)) {
			Object o = _data.get(name);

			if (o instanceof Double) {
				return ((Double)o).doubleValue();
			} else {
				throw new IllegalArgumentException("Data type is not double.");
			}
		} else {
			throw new IllegalArgumentException(name + " is not in the bson");
		}
	}

	public Bson add(String name, String s) {
		_data.put(name, s);
		return this;
	}

	public String getString(String name) {
		if (_data.containsKey(name)) {
			Object o = _data.get(name);

			if (o instanceof String) {
				return (String)o;
			} else {
				throw new IllegalArgumentException("Data type is not String.");
			}
		} else {
			throw new IllegalArgumentException(name + " is not in the bson");
		}
	}

	public Bson add(String name, Bson o) {
		_data.put(name, o);
		return this;
	}

	public Bson getBson(String name) {
		if (_data.containsKey(name)) {
			Object o = _data.get(name);

			if (o instanceof Bson) {
				return (Bson)o;
			} else {
				throw new IllegalArgumentException("Data type is not Bson.");
			}
		} else {
			throw new IllegalArgumentException(name + " is not in the bson");
		}
	}

	public Bson add(String name, List<Bson> arr) {
		_data.put(name, arr);
		return this;
	}

	public List<Bson> getBsonArray(String name) {
		if (_data.containsKey(name)) {
			Object o = _data.get(name);

			if (o instanceof List) {
				return (List<Bson>)o;
			} else {
				throw new IllegalArgumentException("Data type is not List<Bson>.");
			}
		} else {
			throw new IllegalArgumentException(name + " is not in the bson list");
		}
	}

	public Bson add(String name, boolean b) {
		_data.put(name, Boolean.valueOf(b));
		return this;
	}

	public boolean getBoolean(String name) {
		if (_data.containsKey(name)) {
			Object o = _data.get(name);

			if (o instanceof Boolean) {
				return ((Boolean)o).booleanValue();
			} else {
				throw new IllegalArgumentException("Data type is not boolean.");
			}
		} else {
			throw new IllegalArgumentException(name + " is not in the bson");
		}
	}

	public Object get(String name) {
		if (_data.containsKey(name)) {
			Object o = _data.get(name);

			return o;
		} else {
			throw new IllegalArgumentException(name + " is not in the bson");
		}
	}

	public Set<String> keys() {
		return _data.keySet();
	}

	public int size() {
		return _data.size();
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof Bson) {
			Bson b = (Bson)o;
			if (b.size() != this.size()) {
				return false;
			}

			for (String key : keys()) {
				Object value1 = this.get(key);
				Object value2 = b.get(key);

				if (!value1.equals(value2)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		for (String key : keys()) {
			hash = 53 * hash + (get(key).hashCode());
		}
		return hash;
	}

	public void remove(String name) {
		if (_data.containsKey(name)) {
			_data.remove(name);
		} else {
			throw new IllegalArgumentException(name + " is not in the bson");
		}
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("{");

		for (String key : this.keys()) {
			if (buffer.length() > 1) {
				buffer.append(", ");
			}
			buffer.append("\"" + key + "\": ");
			Object value = this.get(key);

			if (value instanceof  Bson) {
				buffer.append(((Bson)value).toString());
			} else {
				buffer.append(value.toString());
			}
		}
		buffer.append("}");

		return buffer.toString();
	}

}
