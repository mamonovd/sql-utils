package io.github.mamonovd.sql;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>Maps result set onto object</p>
 * 
 * <p>Partly borrowed from
 * https://www.codeproject.com/Tips/372152/Mapping-JDBC-ResultSet-to-Object-using-Annotations</p>
 * 
 * @param <T> Class {@link io.github.mamonovd.sql.Entity} to map result set onto
 * 
 * @author d_mamonov
 *
 */
public class ResultSetMapper<T> {

	private static ObjectMapper jsonMapper = new ObjectMapper();
	private Class<T> outputClass;

	public ResultSetMapper(Class<T> outputClass) {
		this.outputClass = outputClass;
	}

	/**
	 * Get SQL query result as list of objects
	 * 
	 * @param sql    SQL text
	 * @param helper Helper
	 * @return List of objects
	 * @throws Exception Database access or mapping exception
	 */
	public List<T> getObjectResult(String sql, final StatementExecutorHelper helper) throws Exception {
		final List<T> list = new ArrayList<T>();
		StatementExecutor.select(sql, new StatementExecutorHelperBase() {
			@Override
			public void bind(PreparedStatement ps) throws SQLException {
				helper.bind(ps);
			}

			@Override
			public void result(ResultSet rs) throws ResultSetProcessingException {
				map(rs, list);
			}
		});

		return list;
	}

	/**
	 * Serialize object to JSON string
	 * 
	 * @param obj object
	 * @return JSON string
	 * @throws JsonProcessingException Error processing
	 */
	public String serialize(Object obj) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(obj);
	}

	/**
	 * Get SQL query result as JSON string
	 * 
	 * @param sql          SQL text
	 * @param helper       Helper
	 * @param singleResult Return only forst object from list
	 * @return JSON string result
	 * @throws Exception Database access or mapping exception
	 */
	public String getJsonStringResult(String sql, StatementExecutorHelper helper, boolean singleResult)
			throws Exception {
		List<T> list = getObjectResult(sql, helper);
		if (singleResult) {
			if (list.size() > 0) {
				return serialize(list.get(0));
			} else {
				return "{}";
			}
		} else {
			if (list.size() > 0) {
				return serialize(list);
			} else {
				return "[]";
			}
		}
	}

	/**
	 * Maps query result set onto list of objects
	 * 
	 * @param rs Query result set
	 * @throws ResultSetProcessingException Database access exception or problem
	 *                                      with bean creation
	 */
	public void map(ResultSet rs, final List<T> outputList) throws ResultSetProcessingException {
		// make sure resultset is not null
		if (rs != null) {
			try {
				// check if outputClass has 'Entity' annotation
				if (outputClass.isAnnotationPresent(Entity.class)) {
					// get the resultset metadata
					ResultSetMetaData rsmd = rs.getMetaData();
					// get all the attributes of outputClass
					Field[] fields = outputClass.getDeclaredFields();

					while (rs.next()) {
						T bean = (T) outputClass.newInstance();
						for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
							// getting the SQL column name
							String columnName = rsmd.getColumnName(_iterator + 1);
							// reading the value of the SQL column
							Object columnValue = rs.getObject(_iterator + 1);

							// iterating over outputClass attributes to check if any attribute has 'Column'
							// annotation with matching 'name' value
							for (Field field : fields) {
								if (field.isAnnotationPresent(Column.class)) {
									Column column = field.getAnnotation(Column.class);

									if (column.name().equalsIgnoreCase(columnName) && columnValue != null) {
										BeanUtils.setProperty(bean, field.getName(), columnValue);
										break;
									}
								}
							}
						}

						outputList.add(bean);
					}

				} else {
					throw new InstantiationException("Class doesn't have @Entity annotation");
				}
			} catch (Exception e) {
				throw new ResultSetProcessingException(e);
			}
		}
	}
}
