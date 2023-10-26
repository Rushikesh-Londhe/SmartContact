package springsmart.controller;

import java.security.Principal;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import springsmart.dao.ContactRepo;
import springsmart.dao.UserRepository;
import springsmart.entity.Contact;
import springsmart.entity.User;

@RestController
public class SearchController {
@Autowired
private UserRepository ur;
@Autowired
private ContactRepo cr;
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?>search(@PathVariable("query")String query, Principal p){
		
		System.out.println(query);
		User user=this.ur.getUserByUserName(p.getName());
		List<Contact> contacts=this.cr.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
}
}
