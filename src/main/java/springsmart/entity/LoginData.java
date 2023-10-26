package springsmart.entity;

import jakarta.validation.constraints.AssertTrue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginData {
@NotBlank(message="user name cannot be empty")
@Size(min=3,max=12,message="username must in between 3 to 12 characters..!")
	private String username;
	
	private String email;
	
	@AssertTrue
	private boolean agreed;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isAgreed() {
		return agreed;
	}
	public void setAgreed(boolean agreed) {
		this.agreed = agreed;
	}
	@Override
	public String toString() {
		return "LoginData [username=" + username + ", email=" + email + ", agreed=" + agreed + "]";
	}
	
	
	
	
}
