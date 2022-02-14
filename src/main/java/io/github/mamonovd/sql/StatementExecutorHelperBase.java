package io.github.mamonovd.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class StatementExecutorHelperBase implements StatementExecutorHelper {
	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mamonovd.sql.StatementExecutorHelper#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return null;
	};
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mamonovd.sql.StatementExecutorHelper#bind(PreparedStatement ps)
	 */
	@Override
	public void bind(PreparedStatement ps) throws SQLException {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mamonovd.sql.StatementExecutorHelper#bind(CallableStatement cs)
	 */
	@Override
	public void result(CallableStatement cs) throws SQLException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mamonovd.sql.StatementExecutorHelper#result(ResultSet rs)
	 */
	@Override
	public void result(ResultSet rs) throws SQLException, ResultSetProcessingException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mamonovd.sql.StatementExecutorHelper#before(Connection conn)
	 */
	@Override
	public void before(Connection conn) throws SQLException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mamonovd.sql.StatementExecutorHelper#after(Connection conn)
	 */
	@Override
	public void after(Connection conn) throws SQLException {
	}

}
