package com.example.demo.Controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Model.QuestionWithOptions;
import com.example.demo.Model.User;
import com.example.demo.Service.AiResponse;
import com.example.demo.Service.HomeService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/User")
public class UserController {

	@Autowired
	private HomeService homeService;
	@Autowired
	private AiResponse aiResponse;
	
	@RequestMapping({"/index","/"})
	public ModelAndView  index() {
		return new ModelAndView("/User/index");
	}
	
	@RequestMapping("/profile")
	public ModelAndView profile(HttpSession session) {
		ModelAndView mv= new ModelAndView("/User/Profile");
		Integer id= (Integer) session.getAttribute("id");
		User us=homeService.findUserDetailById(id);
		mv.addObject("User",us);
		return mv;
	}
	
	@RequestMapping("/submitDetail")
	public ModelAndView SubmitDetail(String companyname,String branchname ,String contact,String address, MultipartFile imageFile,HttpSession session) throws IOException {
		Integer id = (Integer) session.getAttribute("id");
		homeService.updateDetail(companyname,branchname,address,contact,imageFile,id);
		return new ModelAndView("redirect:/User/profile");
	}
	@RequestMapping("/getImage/{fileName}")
	public ResponseEntity<Resource> getFile(@PathVariable("fileName") String filename) {
        Path file = homeService.loadImage(filename);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (IOException e) {
            throw new RuntimeException("Could not read the file!", e);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	@RequestMapping("/creatform")
	public ModelAndView creatform(){
     return new ModelAndView("User/creatform");
	}
	@GetMapping("/ai")
	public ModelAndView aiForm() {
		return new ModelAndView("User/ai");
	}
	@PostMapping("/ai")
	public ModelAndView formCreated(String query) {
		ModelAndView mv= new ModelAndView("User/ai");
		//String query = "create five question on food and option";
		try {
			//List<QuestionWithOptions>  message = aiResponse.getResponseFromGemini(query);
			//  System.out.println(message);
			List<String> message = aiResponse.getResponseFromGemini(query);
			mv.addObject("Message" , message);
		}catch (Exception e){
		 e.printStackTrace();
		 //e.mess
			mv.addObject("Message", e.getMessage());
		}
		return mv;
	}
	@GetMapping("/listForm")
	public ModelAndView listForm() {
		return new ModelAndView("User/listForm");
	}
	@GetMapping("/msfa")
	public void ind(){
		System.out.prinlt("hel");
}
