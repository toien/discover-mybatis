package user.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import user.po.User;
import util.DB;

public class UserDao {

	public User selectOne(long id) {
		SqlSession session = DB.getSession();
		User u = null;
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			u = mapper.selectOne(1330262438979277996L);
		} finally {
			session.close();
		}
		return u;
	}

	public List<User> selectMany() {
		SqlSession session = DB.getSession();
		List<User> result = null;
		try {
			UserMapper mapper = session.getMapper(UserMapper.class);
			result = mapper.selectMany(new Object());

		} finally {
			session.close();
		}
		return result;
	}

	public void insertOne(User u) {
		SqlSession session = DB.getSession();
		try {

			int rt = session.insert("user.dao.UserMapper.insertOne", u);

			// RoleMapper mapper = session.getMapper(RoleMapper.class);
			// u = mapper.selectOne(1330262438979277996L);

			session.commit(); //

			System.out.println(rt);
		} finally {
			session.close();
		}
	}

	public User selectSpecialOne(String username, String password) {
		SqlSession session = DB.getSession();
		User u = null;
		User param = new User();
		param.setUsername(username);
		param.setPassword(password);
		try {

			// u = (User) session.selectOne("po.UserMapper.selectSpecialOne",
			// param);

			UserMapper mapper = session.getMapper(UserMapper.class);
			u = mapper.selectSpecialOne(username, password);

		} finally {
			session.close();
		}
		return u;
	}

	public User selectByDate(Date date) {
		SqlSession session = DB.getSession();
		User u = null;
		try {

			UserMapper mapper = session.getMapper(UserMapper.class);
			u = mapper.selectByDate(date);

		} finally {
			session.close();
		}
		return u;
	}

}
