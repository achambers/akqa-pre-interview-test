package com.akqa.atm.exception;

import com.akqa.atm.model.ErrorCode;

public class AtmServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final ErrorCode errorCode;

	public AtmServiceException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public AtmServiceException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public AtmServiceException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public AtmServiceException(Throwable cause, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
