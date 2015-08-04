package util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DB {
	public static SqlSession getSession() {
		SqlSessionFactory fac = LazyHolder.SESSION_FACTORY;
		SqlSession session = fac.openSession();
		return session;
	}

	private static class LazyHolder {
		private static SqlSessionFactory SESSION_FACTORY = initSessionFactrory();

		private static SqlSessionFactory initSessionFactrory() {
				String resource = "mybatis-config.xml";
				InputStream inputStream = null;
				try {
					inputStream = Resources.getResourceAsStream(resource);
				} catch (IOException e) {
					e.printStackTrace();
				}
				SESSION_FACTORY = new SqlSessionFactoryBuilder()
						.build(inputStream);
			return SESSION_FACTORY;
		}
	}

}
