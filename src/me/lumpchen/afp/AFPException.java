package me.lumpchen.afp;

public class AFPException extends RuntimeException {

	private static final long serialVersionUID = -7716424414699960238L;

	public AFPException() {
		super();
	}
	
	public AFPException(String msg) {
		super(msg);
	}
	
	public AFPException(String msg, Throwable t) {
		super(msg, t);
	}
}
