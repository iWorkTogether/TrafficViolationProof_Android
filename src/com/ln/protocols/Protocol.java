package com.ln.protocols;

public interface Protocol {
	
	public byte[] pack();
	
	public Object unpack(byte[] data);
}
