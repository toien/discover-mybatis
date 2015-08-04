package user.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import user.po.User;

public interface UserMapper {
	User selectSpecialOne(@Param(value = "username") String username,
			@Param(value = "password") String password);

	User selectOne(Long id);
	
	List<User> selectMany(Object o);

	@Select(value = "select * from t_user where signUpTime <= #{date}")
	User selectByDate(@Param(value = "date") Date date);
}
