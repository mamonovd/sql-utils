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
	 * Execute SQL Select using helper
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
	 * Execute SQL Select using helper
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
	 * Execute DML using helper
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
	 * Execute DML using helper
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
	 * Execute stored procedure using helper
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
			cs.executeUpdate();
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
	 * Execute stored procedure using helper
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
