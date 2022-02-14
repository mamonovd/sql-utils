package io.github.mamonovd.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * StatementExecutorHelper implementations helper for StatementExecutor methods.
 * Implementations can extend StatementExecutorHelperBase to protect themselves from changes to this interface.
 * 
 * @author d_mamonov
 *
 */
public interface StatementExecutorHelper {
	/**
	 * Open new database connection 
	 * 
	 * @throws SQLException Database access error
	 */
	public Connection getConnection() throws SQLException;
	/**
	 * Bind query parameters
	 * 
	 * @param ps Prepared statement
	 * @throws SQLException Database access error
	 */
	public void bind(PreparedStatement ps) throws SQLException;

	/**
	 * Handle result of calling stored procedure
	 * 
	 * @param cs Completed statement
	 * @throws SQLException Database access error
	 */
	public void result(CallableStatement cs) throws SQLException;

	/**
	 * Handle query result set
	 * 
	 * @param rs Query result set
	 * @throws SQLException                 Database access exception
	 * @throws ResultSetProcessingException Result process exception
	 */
	public void result(ResultSet rs) throws SQLException, ResultSetProcessingException;

	/**
	 * Executes before query
	 * 
	 * @param conn SQL connection
	 * @throws SQLException Database access error
	 */
	public void before(Connection conn) throws SQLException;

	/**
	 * Executes after query
	 * 
	 * @param conn SQL connection
	 * @throws SQLException Database access error
	 */
	public void after(Connection conn) throws SQLException;
}
