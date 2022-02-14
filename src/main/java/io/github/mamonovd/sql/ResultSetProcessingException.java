package io.github.mamonovd.sql;

/**
 * Exception thrown when there is a result set processing exception occurred
 * 
 * @author d_mamonov
 *
 */
public class ResultSetProcessingException extends Exception {
	private static final long serialVersionUID = -4238302764012163504L;

	public ResultSetProcessingException() {
	}

	public ResultSetProcessingException(String var1) {
		super(var1);
	}

	public ResultSetProcessingException(String var1, Throwable var2) {
		super(var1, var2);
	}

	public ResultSetProcessingException(Throwable var1) {
		super(var1);
	}

}
