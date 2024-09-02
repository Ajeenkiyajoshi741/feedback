package com.example.demo.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Model.User;
import com.example.demo.Repo.HomeRepo;


@Service
public class HomeService {
	
	@Autowired
	private HomeRepo homeRepo;
	
	//file path
    @Value("${file.upload-dir}")
    private String uploadDir;
	
	public boolean CheckEmail(String email) {
		List<String> EmailList= homeRepo.findAllEmailId();
		for(var e:EmailList) {
			if(e.equals(email)) {
				return false;
			}
		}
		return true;
	}
	public void Register(String name,String email,String pass) {
		String companyname="";
		String address="";
		String branchname="";
		String contact="";
		String photopath="default.jpeg";
		
		User u= new User(name,  email,  pass, companyname,  branchname,  address,contact,  photopath);
		homeRepo.save(u);
	}
	public User VerifyUser(String email, String password) {
		User u= homeRepo.findByEmailAndPassword(email,password);
		return u;
	}
	
	public User findUserDetailById(Integer id) {
		User u= homeRepo.findById(id).get();
		return u;
	}
    public void updateDetail(String companyname, String branchname,String address, String contact, MultipartFile imageFile,Integer id) throws IOException {
		File f= new File(uploadDir);

        // creating file path
        if(!f.exists()){
            f.mkdirs();
        }
		
        // coping the image
        String name=imageFile.getOriginalFilename();
        String fileName1= UUID.randomUUID().toString() +name.substring(name.lastIndexOf("."));
        Path path1=Paths.get(uploadDir +File.separator+fileName1);
        Files.copy(imageFile.getInputStream(),path1, StandardCopyOption.REPLACE_EXISTING);

		User user = homeRepo.findById(id).get();
		user.setCompanyname(companyname);
		user.setBranchname(branchname);
		user.setAddress(address);
		user.setContact(contact);
		user.setPhotopath(fileName1);

		homeRepo.save(user);
    }
	public Path loadImage(String filename) {
		return Paths.get(uploadDir).resolve(filename);
	}
    public boolean checkPassword(String pass) {
			boolean uppercase =false;
			boolean digit = false;
			boolean special = false;
			   if( pass.length() >= 8) {
				   for (int i = 0; i < pass.length(); i++) {
						if(Character.isUpperCase(pass.charAt(i))){
							 uppercase = true;
						}
						if( Character.isDigit(pass.charAt(i))){
							 digit = true;
						}
					   if(! Character.isDigit(pass.charAt(i)) &&  !Character.isUpperCase(pass.charAt(i)) && ! Character.isLowerCase(pass.charAt(i))){
							special = true;
					   }
	   
				   }
			   }
		return (uppercase && digit && special);
    }
	
	

}
