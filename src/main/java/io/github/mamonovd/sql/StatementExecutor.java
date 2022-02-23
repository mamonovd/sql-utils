package io.github.mamonovd.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Statement executor. Use it to select / DML / call stored procedures
 * 
 * @author d_mamonov
 *
 */
public class StatementExecutor {
	/**
	 * Execute SQL Select using helper. StatementExecutorHelper#bind(PreparedStatement ps) must be implemented.
	 * <p>Example usage:<p>
	 * <pre>
	 * StatementExecutor.select(conn, "SELECT col FROM table WHERE id = ?", new StatementExecutorHelperBase() {
	 *   &#64;Override
	 *   public void bind(PreparedStatement ps) throws SQLException {
	 *     ps.setInt(1, 1);
	 *   }
	 *   
	 *   &#64;Override
	 *   public void result(ResultSet rs) throws ResultSetProcessingException {
	 *     result.set(rs.getString(1));
	 *   }
	 * });</pre>
	 * 
	 * @param conn     SQL connection to use to run the query
	 * @param sql      SQL text
	 * @param helper   Instance of helper
	 * @throws Exception Database access error
	 */
	public static void select(Connection conn, String sql, StatementExecutorHelper helper) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			helper.before(conn);
			ps = conn.prepareStatement(sql);
			helper.bind(ps);
			rs = ps.executeQuery();
			helper.result(rs);
			helper.after(conn);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * Execute SQL Select using helper. StatementExecutorHelper#bind(PreparedStatement ps) must be implemented.
	 * <p>Example usage:<p>
	 * <pre>
	 * StatementExecutor.select("SELECT col FROM table WHERE id = ?", new StatementExecutorHelperBase() {
	 *   &#64;Override
	 *   public void bind(PreparedStatement ps) throws SQLException {
	 *     ps.setInt(1, 1);
	 *   }
	 *   
	 *   &#64;Override
	 *   public void result(ResultSet rs) throws ResultSetProcessingException {
	 *     result.set(rs.getString(1));
	 *   }
	 * });</pre>
	 * 
	 * @param sql      SQL text
	 * @param helper   Instance of helper
	 * @throws Exception Database access error
	 */
	public static void select(String sql, StatementExecutorHelper helper) throws Exception {
		Connection conn = null;
		try {
			conn = helper.getConnection();
			select(conn, sql, helper);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * Execute DML using helper. StatementExecutorHelper#bind(PreparedStatement ps) must be implemented.
	 * <p>Example usage:<p>
	 * <pre>
	 * StatementExecutor.update(conn, "UPDATE table SET col = ? WHERE id = ?", new StatementExecutorHelperBase() {
	 *   &#64;Override
	 *   public void bind(PreparedStatement ps) throws SQLException {
	 *     int i = 0;
	 *     ps.setString(++i, "value");
	 *     ps.setInt(++i, 1);
	 *   }
	 * });</pre>
	 * 
	 * @param conn   SQL connection to use to run the query
	 * @param sql    DML text
	 * @param helper Instance of helper
	 * @throws SQLException Database access error
	 */
	public static void update(Connection conn, String sql, StatementExecutorHelper helper) throws SQLException {
		PreparedStatement ps = null;
		try {
			helper.before(conn);
			ps = conn.prepareStatement(sql);
			helper.bind(ps);
			ps.executeUpdate();
			helper.after(conn);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * Execute DML using helper. StatementExecutorHelper#bind(PreparedStatement ps) must be implemented.
	 * <p>Example usage:<p>
	 * <pre>
	 * StatementExecutor.update("UPDATE table SET col = ? WHERE id = ?", new StatementExecutorHelperBase() {
	 *   &#64;Override
	 *   public void bind(PreparedStatement ps) throws SQLException {
	 *     int i = 0;
	 *     ps.setString(++i, "value");
	 *     ps.setInt(++i, 1);
	 *   }
	 * });</pre>
	 * 
	 * @param sql    DML text
	 * @param helper Instance of helper
	 * @throws SQLException Database access error
	 */
	public static void update(String sql, StatementExecutorHelper helper) throws SQLException {
		Connection conn = null;
		try {
			conn = helper.getConnection();
			update(conn, sql, helper);
			conn.commit();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * Execute stored procedure using helper. StatementExecutorHelper#bind(CallableStatement cs) must be implemented.
	 * <p>Example usage:<p>
	 * <ul>
	 * <li>Get returned value:<pre>
	 * StatementExecutor.call(conn, "{? = call stored_procedure(?)}", new StatementExecutorHelperBase() {
	 *   &#64;Override
	 *   public void bind(CallableStatement cs) throws SQLException {
	 *     int i = 0;
	 *     cs.registerOutParameter(++i, java.sql.Types.NUMERIC);
	 *     cs.setInt(++i, 1);
	 *   }
     *
	 *   &#64;Override
	 *   public void result(CallableStatement cs) throws SQLException {
	 *     result.set(cs.getLong(1));
	 *   }
	 * });</pre></li>
	 * <li>Execute PL/SQL block:<pre>
	 * StatementExecutor.call(conn, "BEGIN stored_procedure(?); END;", new StatementExecutorHelperBase() {
	 *   &#64;Override
	 *   public void bind(CallableStatement cs) throws SQLException {
	 *     int i = 0;
	 *     cs.setInt(++i, 1);
	 *   }
	 * });</pre></li>
	 *</ul>
	 * 
	 * @param conn   SQL connection to use to run the query
	 * @param sql    Call text
	 * @param helper Instance of helper
	 * @throws SQLException Database access error
	 */
	public static void call(Connection conn, String sql, StatementExecutorHelper helper) throws SQLException {
		CallableStatement cs = null;
		try {
			helper.before(conn);
			cs = conn.prepareCall(sql);
			helper.bind(cs);
			cs.execute();
			helper.result(cs);
			helper.after(conn);
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * Execute stored procedure using helper. StatementExecutorHelper#bind(CallableStatement cs) must be implemented.
	 * <p>Example usage:<p>
	 * <ul>
	 * <li>Get returned value:<pre>
	 * StatementExecutor.call("{? = call stored_procedure(?)}", new StatementExecutorHelperBase() {
	 *   &#64;Override
	 *   public void bind(CallableStatement cs) throws SQLException {
	 *     int i = 0;
	 *     cs.registerOutParameter(++i, java.sql.Types.NUMERIC);
	 *     cs.setInt(++i, 1);
	 *   }
     *
	 *   &#64;Override
	 *   public void result(CallableStatement cs) throws SQLException {
	 *     result.set(cs.getLong(1));
	 *   }
	 * });</pre></li>
	 * <li>Execute PL/SQL block:<pre>
	 * StatementExecutor.call("BEGIN stored_procedure(?); END;", new StatementExecutorHelperBase() {
	 *   &#64;Override
	 *   public void bind(CallableStatement cs) throws SQLException {
	 *     int i = 0;
	 *     cs.setInt(++i, 1);
	 *   }
	 * });</pre></li>
	 *</ul>
	 * 
	 * @param sql    call text
	 * @param helper Instance of helper
	 * @throws SQLException Database access error
	 */
	public static void call(String sql, StatementExecutorHelper helper) throws SQLException {
		Connection conn = null;
		try {
			conn = helper.getConnection();
			call(conn, sql, helper);
			conn.commit();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}
