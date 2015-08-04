package user.po;

import java.util.Date;

public class User {
	private Long id;
	private String username;
	private String password;

	private Date signUpTime;

	public Date getSignUpTime() {
		return signUpTime;
	}

	public void setSignUpTime(Date signUpTime) {
	}

	public String getLastSignInIp() {
		return lastSignInIp;
	}

	public void setLastSignInIp(String lastSignInIp) {
		this.lastSignInIp = lastSignInIp;
	}

	public Date getLastSignInTime() {
		return lastSignInTime;
	}

	public void setLastSignInTime(Date lastSignInTime) {
		this.lastSignInTime = lastSignInTime;
	}

	private String lastSignInIp;

	private Date lastSignInTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
