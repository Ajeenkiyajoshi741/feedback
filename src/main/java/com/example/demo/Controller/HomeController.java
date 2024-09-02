package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Model.User;
import com.example.demo.Service.HomeService;

import jakarta.servlet.http.HttpSession;

@RestController()
public class HomeController {
	
	@Autowired
	private HomeService homeService;
	
	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
	@RequestMapping("/login")
	public ModelAndView login(){
		return new ModelAndView("login");
	}
	@RequestMapping("/signup")
	public ModelAndView sginup() {
		return new ModelAndView("Signup");
	}
	@PostMapping("/signup")
	public ModelAndView Signup(String name,String email,String pass, String cpass) {
		ModelAndView mv= new ModelAndView("Signup");
		/* if((pass.equals(cpass)) && homeService.checkPassword(pass)) {
			if(homeService.CheckEmail(email)) {
				homeService.Register(name,email,pass);
				mv.addObject("message", "Register Successfull");
			}else {
				mv.addObject("message", "Email Already Register");
			}
		}else {
			mv.addObject("message", "Password Not Match");
		} */
		if(!pass.equals(cpass)){
			mv.addObject("message", "Password Not Match");
		}
		else if(!homeService.checkPassword(pass)){
			mv.addObject("message", "Password Much have  least 8 characters ,special character And Number");
		}else if(!homeService.CheckEmail(email)){
			mv.addObject("message", "Email Already Register");
		}else{
			homeService.Register(name, email, pass);
			mv.addObject("Message", "Register Successfull");
		}
		return mv;
	}
	@PostMapping("/login")
	public ModelAndView Login(String email , String pass,HttpSession session) {
		ModelAndView mv= new ModelAndView("login");
		User u= homeService.VerifyUser(email,pass);
		if(u != null) {
			session.setAttribute("id", u.getId());
			return new ModelAndView("redirect:/User/index");
		}
		else {
			mv.addObject("message", "Email or Password Wrong");
		}
		return mv;
	}
	@GetMapping("/logout")
	public ModelAndView logout(HttpSession session){
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
}
