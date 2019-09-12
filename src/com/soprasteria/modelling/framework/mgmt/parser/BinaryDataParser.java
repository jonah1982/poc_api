package com.soprasteria.modelling.framework.mgmt.parser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class BinaryDataParser {
	private byte[] data;
	
	public BinaryDataParser(Object data) throws Exception {
		super();
		if(data instanceof byte[]) {
			this.data = (byte[])data;
		} else throw new Exception("the data "+data+" should be in byte[] type");
	}

	public void parse(DataParsingHandler handler) {
		ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);
		ShortBuffer sb = bb.asShortBuffer();
		int count = 0;
		while(sb.hasRemaining()){
			int value = swap(sb.get());
			handler.handle(count++, value);
		}
		bb.clear();
		sb.clear();
	}

	private short swap(short value) {
		int b1 = value & 0xff;
		int b2 = (value >> 8) & 0xff;
		return (short) (b1 << 8 | b2 << 0);
	}
}
