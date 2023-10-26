package springsmart.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import springsmart.dao.UserRepository;
import springsmart.entity.User;


public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository ur;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		//fetch user from DB
		User user=ur.getUserByUserName(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("Colud not found user");
		}
		CustomUserDetails cu=new CustomUserDetails(user);
		return cu;
	}

}
