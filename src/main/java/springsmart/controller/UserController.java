package springsmart.controller;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import springsmart.dao.ContactRepo;
import springsmart.dao.UserRepository;
import springsmart.entity.Contact;
import springsmart.entity.User;
import springsmart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
@Autowired
	private UserRepository ur;
@Autowired
private ContactRepo cr;
@Autowired
private BCryptPasswordEncoder passEncoder;


//adding common data
@ModelAttribute
public void adddComomn(Model m,Principal principal) {
	String username=principal.getName();
	System.out.println(username);
	
	//get the user using username
	User user=this.ur.getUserByUserName(username);
	System.out.println(user);
	
	m.addAttribute("user",user);
	
}

//dashboard
	@RequestMapping("/index")
	public String dash(Model m,Principal principal)
{
		m.addAttribute("title","Home Page");
		return "normal/dash";
}
	
	
	//open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title","Add Contact");
		m.addAttribute("contact",new Contact());
		return "normal/add_contact";
	}

	//processing add contact data
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,Principal principal) {
	try {
		String name=principal.getName();
	User u=this.ur.getUserByUserName(name);
	
	contact.setUser(u);
	
	/*
	 * //processing and uploading file if(file.isEmpty()) {
	 * System.out.println("File is empty"); } else { //upload the file to folder and
	 * update name to contact contact.setImage(file.getOriginalFilename()); File
	 * file1=new ClassPathResource("static/img").getFile(); Path path =
	 * Paths.get(file1.getAbsolutePath()+File.separator+file.getOriginalFilename());
	 * 
	 * Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
	 * System.out.println("Image uploaded"); }
	 */
	u.getContacts().add(contact);
	
	this.ur.save(u);
	
	System.out.println("success");
		System.out.println(contact);
	
		//message error
		//session.setAttribute("msg",new Message("Yout conatct is added..!!","success"));
		
	}
	catch(Exception e) {
		e.printStackTrace();
		//session.setAttribute("msg",new Message("something went wrong","danger"));
		
	}
	return "normal/add_contact";
	}
	
	//per page 5 contacts=5[n]
	//current page=0[page]
	//contact
	@GetMapping("/show-contacts/{page}")
	
	public String showContact(@PathVariable("page")Integer page, Model m,Principal p) {
		//sending list of contacts
		
		/*
		 * String name=p.getName();
		 * 
		 * User user=this.ur.getUserByUserName(name); user.getContacts();
		 */		
		String name=p.getName();
	User user=	this.ur.getUserByUserName(name);
	
	//pageable contains curent page and contact per page
	Pageable pageable=PageRequest.of(page,5);
	
	Page<Contact> contacts=	this.cr.findContactsByUser(user.getId(),pageable);
	
	m.addAttribute("contacts",contacts);
	m.addAttribute("currentPage",page);
	m.addAttribute("totalPages",contacts.getTotalPages());
	m.addAttribute("title","Show User Contacts");
		return "normal/show_contacts";
	}
	//specific contact deatils
	@RequestMapping("/contact/{cid}")
	public String showParticular(@PathVariable("cid") Integer cid,Model m,Principal p) {
		System.out.println(cid);
		Optional<Contact> contact = this.cr.findById(cid);
		Contact contact1=contact.get();
		
		//accessing contacts only for that particular user
		String name=p.getName();
		User user =this.ur.getUserByUserName(name);
	
		if(user.getId()==contact1.getUser().getId()) {
			m.addAttribute("contact",contact1);
		}
	
		return "normal/contact_detail";
	}
	
	//delete contact
	@RequestMapping("/delete/{cid}")
	public String delete(@PathVariable("cid") Integer cid,Principal p) {
		
		Optional<Contact> contact = this.cr.findById(cid);
		Contact con=contact.get();
		
		String name=p.getName();
		User user2=this.ur.getUserByUserName(name);
		

		
		if(user2.getId()==con.getUser().getId())
		{
			//con.setUser(null);
			//this.cr.delete(con);
			//this.ur.delete(user2);
			user2.getContacts().remove(con);
			this.ur.save(user2);
		}
		return "redirect:/user/show-contacts/"+0;
	}
	
	//update
	@PostMapping("/update-form/{cid}")
	public String update(@PathVariable("cid") Integer cid, Model m) {
		m.addAttribute("title","Update Contacts");
		Contact contact = this.cr.findById(cid).get();
		m.addAttribute("contact",contact);
		
		return "normal/update";
	}
	//process update
	@PostMapping("/process-update")
	public String processUpdate(@ModelAttribute Contact contact,Principal p) {
		
	try {
		User user=this.ur.getUserByUserName(p.getName());
		contact.setUser(user);
		this.cr.save(contact);
	
		}
	catch(Exception e){
		e.printStackTrace();
	}
	return "redirect:/user/show-contacts/"+0;
	}
	
	//your profile
	@GetMapping("/profile")
	public String profile(Model m) {
		m.addAttribute("title","Profile Page");
		
		return "normal/profile";
	}
	
	//see your settings
	
	@GetMapping("/settings")
	public String setting(@ModelAttribute User user,Model m,Principal p) {
		m.addAttribute("title","Settings Page");
//		User user1=this.ur.getUserByUserName(p.getName());
//		user1.setPassword(null);
		return "normal/settings";
	}
	
	@PostMapping("/settings-update")
	public String processUpdateSettings(@ModelAttribute("user") User user,@RequestParam("oldPassword")String oldPassword, @RequestParam("newPassword")String newPassword,Principal p,HttpSession session) {
	    try {
	        // Retrieve the currently logged-in user
	        User currentUser = ur.getUserByUserName(p.getName());

	        // Check if the old password matches before updating
	        if (this.passEncoder.matches(oldPassword, currentUser.getPassword())) {
	            // Update the password with the new one
	            currentUser.setPassword(passEncoder.encode(newPassword));
	            this.ur.save(currentUser);
	            this.ur.save(user);
	            session.setAttribute("msg", "Password successfully updated");
	        } else {
	        	session.setAttribute("msg", "please enter correct old password");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exceptions
	    }
	    return "normal/settings";
	}
	/*
	 * @PostMapping("/settings-update") public String
	 * processUpdateSettings(@ModelAttribute("user") User user) { try { // Update
	 * the password with the new one
	 * user.setPassword(passEncoder.encode(user.getPassword())); ur.save(user); }
	 * catch (Exception e) { e.printStackTrace(); // Handle exceptions } return
	 * "normal/settings"; }
	 */

}
