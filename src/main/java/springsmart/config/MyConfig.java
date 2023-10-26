package springsmart.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class MyConfig  {
	
	
	
	  @Bean public UserDetailsService uds() {
	  
	  return new UserDetailsServiceImpl(); }
	  
	  @Bean public BCryptPasswordEncoder passEncoder() {
	  
	  return new BCryptPasswordEncoder(); }
	  
	  @Bean public DaoAuthenticationProvider dap() { DaoAuthenticationProvider
	  dao=new DaoAuthenticationProvider(); dao.setUserDetailsService(this.uds());
	  dao.setPasswordEncoder(passEncoder());
	  
	  return dao; 
	  }
	  
	 
	//configure method
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(dap());
		 http
        // .addFilter(dap(), UsernamePasswordAuthenticationFilter.class)
         .authorizeRequests(authorizeRequests ->
             {
				try {
					authorizeRequests
					     .requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**").hasRole("USER").requestMatchers("/**")
					     .permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and().csrf().disable();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        
       );
       
		DefaultSecurityFilterChain build1=http.build();
		return build1;
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		
		return configuration.getAuthenticationManager();
		
	}
	
	
}
