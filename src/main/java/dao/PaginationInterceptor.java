package dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }))
public class PaginationInterceptor implements Interceptor {

	private static final String FIELD_DELEGATE = "delegate";
	private static final String FIELD_SQL = "sql";
	private static final String FIELD_MAPPEDSTATEMENT = "mappedStatement";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Connection connection = (Connection) invocation.getArgs()[0];

		RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation
				.getTarget();

		StatementHandler handler = (StatementHandler) readField(statementHandler,
				FIELD_DELEGATE);
		MappedStatement mappedStatement = (MappedStatement) readField(handler,
				FIELD_MAPPEDSTATEMENT);
		Page<?> page = new Page<Object>();
		
		page.setPageNo(1);
		page.setPageSize(1);

		BoundSql boundSql = handler.getBoundSql();

		String targetSql = boundSql.getSql();
		// paging
		getTotalCountAndSetInPage(targetSql, boundSql, page, mappedStatement, connection);
		
		String pagingSql = getSelectPageSql(targetSql, page);
		
		writeDeclaredField(boundSql, FIELD_SQL, pagingSql);

		return invocation.proceed();
	}

	private <T> void getTotalCountAndSetInPage(String targetSql, BoundSql boundSql,
			Page<T> page, MappedStatement mappedStatement, Connection connection)
			throws SQLException {
		String totalSql = getTotalCountSql(targetSql);
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Object parameterObject = boundSql.getParameterObject();
		BoundSql totalBoundSql = new BoundSql(mappedStatement.getConfiguration(),
				totalSql, parameterMappings, parameterObject);
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
				parameterObject, totalBoundSql);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(totalSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int totalRecord = rs.getInt(1);
				page.setTotalCount(totalRecord);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}
	}

	private String getTotalCountSql(String originalSql) {
		StringBuilder stringBuilder = new StringBuilder(originalSql.trim());
		
		return "SELECT COUNT(1) FROM t_user";
	}

	private String getSelectPageSql(String originalSql, Page page) {
		return originalSql.concat(" LIMIT 1, 1");
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}

	private Object readField(Object target, String fieldName)
			throws IllegalAccessException {
		if (target == null) {
			throw new IllegalArgumentException("target object must not be null");
		}
		Class<?> cls = target.getClass();
		Field field = getField(cls, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Cannot locate field " + fieldName
					+ " on " + cls);
		}
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		return field.get(target);
	}

	private static Field getField(final Class<?> cls, String fieldName) {
		for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
			try {
				Field field = acls.getDeclaredField(fieldName);
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
					return field;
				}
			} catch (NoSuchFieldException ex) {
				// ignore
			}
		}
		return null;
	}

	private void writeDeclaredField(Object target, String fieldName, Object value)
			throws IllegalAccessException {
		if (target == null) {
			throw new IllegalArgumentException("target object must not be null");
		}
		Class<?> cls = target.getClass();
		Field field = getField(cls, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Cannot locate declared field "
					+ cls.getName() + "." + fieldName);
		}
		field.set(target, value);
	}

}
