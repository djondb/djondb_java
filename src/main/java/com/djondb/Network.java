package com.djondb;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.net.*;
import java.nio.channels.*;

public class Network {

	private ByteBuffer _stream;
	private int _bufferLen;
	private int _bufferPos;
	private SocketChannel _client;
	private boolean _connected;
	private Selector _selector;

	public Network(byte[] data) {
		this._stream = ByteBuffer.wrap(data);
		this._stream.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		this._bufferLen = data.length;
		this._bufferPos = 0; 
	}

	public Network() {
		this._stream = ByteBuffer.allocate(2048);
		this._stream.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		this._stream.clear();
		this._bufferLen = 0;
		this._bufferPos = 0;
	}

	public boolean connect(String host, int port) {
		try {
			if (_client != null) {
				_client.close();
			}
			this._client = SocketChannel.open();
			this._client.configureBlocking(false);
			this._client.connect(new java.net.InetSocketAddress(host, port));

			this._selector = Selector.open();

			// Record to selector (OP_CONNECT type)
			//SelectionKey clientKey = this._client.register(this._selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			SelectionKey clientKey = this._client.register(this._selector, SelectionKey.OP_CONNECT);

			this._connected = false;
			// Waiting for the connection
			if (this._selector.select(500)> 0) {

				// Get keys
				Set keys = this._selector.selectedKeys();
				Iterator i = keys.iterator();

				// For each key...
				while (i.hasNext()) {
					SelectionKey key = (SelectionKey)i.next();

					// Remove the current key
					i.remove();

					// Get the socket channel held by the key
					SocketChannel channel = (SocketChannel)key.channel();

					// Attempt a connection
					if (key.isConnectable()) {

						// Close pendent connections
						if (channel.isConnectionPending())
							channel.finishConnect();

						this._connected = true;
					}
				}
			}
			return this._connected;
		} catch (java.io.IOException e) {
			return false;
		}
	}

	private void checkBuffer(int size) throws Exception {
		int pos = this.currentPosition();
		if (this.size() < (pos + size)) {
			this.waitAvailable();
		}
		if (this.size() < (this.currentPosition() + size)) {
			throw new Exception("Not enough data");
		}
	}

	private int currentPosition() {
		return this._bufferPos;
	}

	public void seek(int position) {
		if (position > this._bufferLen) {
			position = this._bufferLen;
		}
		this._stream.position(position);
		this._bufferPos = position;
	}

	private int size() {
		return this._bufferLen;
	}

	public void reset() {
		this._stream.clear();
		this._bufferLen = 0;
		this._bufferPos = 0;
	}

	private boolean isReadyRead() throws java.io.IOException {
		SelectionKey clientKey = this._client.register(this._selector, SelectionKey.OP_READ);
		// Waiting for the connection
		while (this._selector.select(500)> 0) {

			// Get keys
			Set keys = this._selector.selectedKeys();
			Iterator i = keys.iterator();

			// For each key...
			while (i.hasNext()) {
				SelectionKey key = (SelectionKey)i.next();

				// Remove the current key
				i.remove();

				// Get the socket channel held by the key
				SocketChannel channel = (SocketChannel)key.channel();

				// Attempt a connection
				if (key.isReadable()) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isReadyWrite() throws java.io.IOException {
		SelectionKey clientKey = this._client.register(this._selector, SelectionKey.OP_WRITE);
		// Waiting for the connection
		while (this._selector.select(500)> 0) {

			// Get keys
			Set keys = this._selector.selectedKeys();
			Iterator i = keys.iterator();

			// For each key...
			while (i.hasNext()) {
				SelectionKey key = (SelectionKey)i.next();

				// Remove the current key
				i.remove();

				// Get the socket channel held by the key
				SocketChannel channel = (SocketChannel)key.channel();

				// Attempt a connection
				if (key.isWritable()) {
					return true;
				}
			}
		}

		return false;
	}

	private void waitAvailable() throws java.io.IOException {
		if (isReadyRead()) {
			int posBeforeRead = this.currentPosition();
			//System.out.println("posBeforeRead: " + posBeforeRead);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			this._client.read(buffer);
			byte[] d = buffer.array();

			byte[] remaining = new byte[this._bufferLen - posBeforeRead];
			this._stream.get(remaining);
			//System.out.println("Remaining: " + remaining.length);
			this._stream.clear();
			this._bufferLen = remaining.length + d.length;
			//System.out.println("length: " + this._bufferLen);
			this._stream.put(remaining, 0, remaining.length);
			this._stream.put(d, 0, d.length);
			this._stream.rewind(); // prepare for read
			this._bufferPos = 0;
			//this._stream.position(posBeforeRead);
		}
	}

	public void flush() throws java.io.IOException {
		if (isReadyWrite()) {
			this._stream.flip();
			this._client.write(this._stream);
			this.reset();
		}
	}

	public int readInt() throws Exception {
		this.checkBuffer(4);
		int i = this._stream.getInt();
		//System.out.println("i: " + i);
		this._bufferPos += 4;

		return i;
	}

	public void writeInt(int i) {
		this._stream.putInt(i);
		this._bufferPos += 4;
		this._bufferLen += 4;
	}

	public long readLong() throws Exception {
		this.checkBuffer(8);
		long l = this._stream.getLong();

		this._bufferPos += 8;
		return l;
	}

	public void writeLong(long l) {
		this._stream.putLong(l);
		this._bufferPos += 8;
		this._bufferLen += 8;
	}

	public String readString() throws Exception {
		int len = readInt();

		this.checkBuffer(len);
		byte[] b = new byte[len];
		this._stream.get(b, 0, len);

		String result = new String(b);

		this._bufferPos += len;

		return result;
	}

	public void writeString(String s) {
		writeInt(s.length());

		byte[] b = s.getBytes();
		this._stream.put(b);
		this._bufferPos += b.length;
		this._bufferLen += b.length;
	}

	public boolean readBoolean() throws Exception {
		checkBuffer(1);
		char c = this._stream.getChar();

		this._bufferPos += 1;
		return c == 1;
	}

	public void writeBoolean(boolean b) {
		this._stream.putChar(b ? (char)1: (char)0);
		this._bufferPos += 1;
		this._bufferLen += 1;
	}

	public double readDouble() throws Exception {
		checkBuffer(8);

		double d = this._stream.getDouble();

		this._bufferPos += 8;
		return d;
	}

	public void writeDouble(double d) {
		this._stream.putDouble(d);
		this._bufferPos += 8;
		this._bufferLen += 8;
	}

	public Bson readBSON() throws Exception {
		long elements = this.readLong();
		Bson res = new Bson();
		for (int x = 0; x < elements; x++) {
			String key = this.readString();
			long datatype = this.readLong();

			switch ((int)datatype) {
				case 0: 
					int i = this.readInt();
					res.add(key, i);
					break;
				case 1:
					double d = this.readDouble();
					res.add(key, d);
					break;
				case 2:
					long l = this.readLong();
					res.add(key, l);
					break;
				case 4:
					String s = this.readString();
					res.add(key, s);
					break;
				case 5:
					Bson bson = this.readBSON();
					res.add(key, bson);
					break;
				case 6:
					List<Bson> bsonArray = this.readBSONArray();
					res.add(key, bsonArray);
					break;
				case 10:
					boolean b = this.readBoolean();
					res.add(key, b);
					break;
				default:
					throw new DjondbException(601, "Unsupported datatype " + datatype);
			};
		};
		return res;
	}

	public void writeBSON(Bson bson) throws DjondbException {
		int elements = bson.size();
		this.writeLong(elements);
		for (String key : bson.keys()) {
			this.writeString(key);

			Object value = bson.get(key);
			if (value instanceof Integer) {
				this.writeLong(0);
				this.writeInt(((Integer)value).intValue());
			} else if (value instanceof Double) {
				this.writeLong(1);
				this.writeDouble(((Double)value).doubleValue());
			} else if (value instanceof Long) {
				this.writeLong(2);
				this.writeLong(((Long)value).longValue());
			} else if (value instanceof String) {
				this.writeLong(4);
				this.writeString((String)value);
			} else if (value instanceof Bson) {
				this.writeLong(5);
				this.writeBSON((Bson)value);
			} else if (value instanceof List) {
				this.writeLong(6);
				this.writeBSONArray((List<Bson>)value);
			} else if (value instanceof Boolean) {
				this.writeLong(10);
				this.writeBoolean(((Boolean)value).booleanValue());
			} else {
				throw new DjondbException(601, "Unsupported datatype " + value.getClass().toString());
			}
		};
	}

	public void writeBSONArray(List<Bson> arr) throws DjondbException {
		long size = arr.size();
		this.writeLong(size);
		for (Bson b : arr) {
			this.writeBSON(b);
		}
	}

	public List<Bson> readBSONArray() throws Exception {
		long size = this.readLong();
		List<Bson> res = new ArrayList<Bson>();
		for (int x = 0; x < size; x++) { 
			Bson b = this.readBSON();
			res.add(b);
		}
		return res;
	}

}

