package com.tikal.codegen.jet;

public class EmitterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	final ErrorCode errorCode;
	final Object[] params;

	public EmitterException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
		this.params = null;
	}

	public EmitterException(ErrorCode errorCode, Object... params) {
		super();
		this.errorCode = errorCode;
		this.params = params;
	}

	public EmitterException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.params = null;
	}

	public EmitterException(ErrorCode errorCode, Throwable cause, Object... params) {
		super(cause);
		this.errorCode = errorCode;
		this.params = params;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public Object[] getParams() {
		return params;
	}

}
