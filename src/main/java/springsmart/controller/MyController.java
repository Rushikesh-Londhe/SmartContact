package springsmart.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import springsmart.entity.LoginData;
@Controller
public class MyController {
	
	@RequestMapping("/form")
	public String openForm(Model m) {
		m.addAttribute("loginData",new LoginData());
		return "form";
	}
	
	@PostMapping("/process")
	public String getdata(@Valid @ModelAttribute("logindata")LoginData ld,BindingResult result) {
		
		if(result.hasErrors()) {
			
			System.out.println(result);
			return "form";
		}
		System.out.println(ld);
		//data process
		
		
		return "success";
	}
}
