package springsmart.controller;

import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import springsmart.dao.UserRepository;
import springsmart.entity.Contact;
import springsmart.entity.User;
import springsmart.helper.Message;
import springsmart.service.EmailService;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	@Autowired
private UserRepository ur;
	@Autowired
	private EmailService es;
	//home handler
	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	//about handler
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","About-Smart Contact Manager");
		return "about";
	}
	
	//signup handler
		@RequestMapping("/signup")
		public String signUp(Model m) {
			m.addAttribute("title","Register-Smart Contact Manager");
			m.addAttribute("user", new User());
			return "signup";
		}
		
		//register handler
		@PostMapping("/do_register")
		public String register(@Valid @ModelAttribute("user") User user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
				BindingResult result,   Model model, HttpSession session) {
session.removeAttribute("message");
try {
		    if (!agreement) {
		        model.addAttribute("user", user);
		        throw new Exception("you have not agreed terms and condition");
		      
		    }

		    if (result.hasErrors()) {
		        model.addAttribute("user", user);
		        return "signup";
		    }

		 
		        user.setRole("ROLE_USER");
		        user.setEnabled(true);
		        user.setImageUrl("default.png");

		        user.setPassword(passEncoder.encode(user.getPassword()));
		        User savedUser = this.ur.save(user);

		        model.addAttribute("user", new User());
		        session.setAttribute("message", new Message("Successfully registered.", "alert-success"));

		        return "signup"; // Redirect to the signup page
		    } catch (Exception e) {
		        e.printStackTrace();
		        model.addAttribute("user", user);
		        session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
		        return "signup";
		    }
		}
		@RequestMapping("/signin")
			//handler for custom login
			public String customLogin(Model m) {
	m.addAttribute("title","Login Page");
	return "login";

		}
		//forget password
		@RequestMapping("/forget")
		public String forgetPassword(Model m) {
			m.addAttribute("title","Forget Password");
			return "forget";
		}
		
		//send otp
		@PostMapping("/send-otp")
		public String sendOtp(@RequestParam("email")String email,  Model m,HttpSession session) {
			System.out.println(email);
			//generating otp of 4 digit
			Random random = new Random(1000);
			int otp = random.nextInt(999999);
			System.out.println(otp);
			
			m.addAttribute("title","Send OTP");
			String subject="OTP form SCM";
			String msg="<h1> OTP="+otp+"</h1>";
			String to=email;
			boolean flag=this.es.sendEmail(msg,subject,to);
			
			if(flag) {
				session.setAttribute("myotp", otp);
				session.setAttribute("email", email);
				return "verify-otp";
			}else {
				session.setAttribute("message", "check your email ");
				return "send-otp";
			}
			
			
		}
		//verify otp
		@PostMapping("/verify-otp")
		public String verifyOtp( @RequestParam("otp")Integer otp,HttpSession session) {
			
			int myOtp=(int)session.getAttribute("myotp");
			String email=(String)session.getAttribute("email");
			User user=this.ur.getUserByUserName(email);
			if(myOtp==otp) {
				if(user==null) {
						
					return "forget";
				}
			else {
				return "change_password";
			}
			}	
			else {
				
				return "verify-otp";
			}
			
		}
		
		//change password
		@PostMapping("/change-password")
		public String changePassword(@RequestParam("password")String password ,HttpSession session) {
			String email=(String)session.getAttribute("email");
			User user=this.ur.getUserByUserName(email);
			user.setPassword(this.passEncoder.encode(password));
			this.ur.save(user);
			return "login";
			
		}

	}
		


