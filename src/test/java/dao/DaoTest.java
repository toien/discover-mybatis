package dao;

import org.junit.Test;

import user.dao.UserDao;

public class DaoTest {

	@Test
	public void test() {
		UserDao dao = new UserDao();
		System.out.println(dao.selectMany());
	}
}
