package gotogoal.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import java.util.Locale;


import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import gotogoal.config.LocalDateFormatter;
import gotogoal.config.PictureUploadProperties;
import gotogoal.model.User;
import gotogoal.model.UserSession;
import gotogoal.service.UserService;
import org.springframework.context.MessageSource;

@Controller
@RequestMapping("/user")
@SessionAttributes("picturePath")
public class UserController {
	
	public static final FileSystemResource PICTURES_DIR =  new FileSystemResource("./pictures");
	private final Resource picturesDir;
	private final Resource anonymousPicture;
        private final MessageSource messageSource;
        private UserSession userSession;
        private UserService userService;
	
	@Autowired
	public UserController(PictureUploadProperties uploadProperties, MessageSource messageSource, UserSession userSession, UserService userService){
		picturesDir=uploadProperties.getUploadPath();
		anonymousPicture=uploadProperties.getAnonymousPicture();
                this.messageSource = messageSource;
                this.userSession = userSession;
                this.userService = userService;
	}
	
	
	@ModelAttribute("dateFormat")
        public String localeFormat(Locale locale) {
          //  return LocalDateFormatter.getPattern(locale);
          return "yyyy-MM-dd";
        }
	
	@ModelAttribute("picturePath")
	public Resource picturePath(){
		return anonymousPicture;
	}
        
        @ModelAttribute
        public User getUser(){
            return userSession.toUser();
        }
	
	@ExceptionHandler(IOException.class)
	public ModelAndView IOExceptionHandle(Locale locale){
		ModelAndView modelAndView = new ModelAndView("user/pictureUpload");
		modelAndView.addObject("error", messageSource.getMessage("upload.io.exception", null, locale));   // można też exception.getMessage();
                modelAndView.addObject("user",userSession.toUser());
		return modelAndView;
	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String registerForm(Model model, Locale locale){
	
		model.addAttribute("user", new User());
		return "user/register";
	}
        
        
	
        @RequestMapping(value = "/profile")
	public String profile(){
		return "home";
	}
        
        
	@RequestMapping(value="/register", params = {"save"}, method = RequestMethod.POST)
	public String register(@Valid User user, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			return "user/register";
		}
                userService.save(user);
                userSession.saveUser(user);
		return "redirect:/user/profile"; //nie ma tej stronki trzeba ją dorobić xD
	}
	
	
	
	//****** PICTURE ********************/
	
	@RequestMapping("/picture")
	public String picture(){
		return "user/pictureUpload";
	}
	
	@RequestMapping(value = "/picture", params={"upload"}, method = RequestMethod.POST)
	public String picture(MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException{
		if(file.isEmpty() || !isImage(file)){
			redirectAttributes.addFlashAttribute("error","Niewlasciwy typ pliku");
			return "redirect:/user/register";
		}
		Resource picturePath = copyFileToPicture(file);		
		//model.addAttribute("picturePath",picturePath); Zamiast dodawać ścieżkę jako atrybut modelu to wrzucamy ją do obiektu usera w sessji
                userSession.setPicturePath(picturePath);
		return "user/register";
	}

//      Stara metoda nie wykorzystująca obiektu usera zapisanego w sesji 
//      @RequestMapping(value="/uploadedPicture")
//	public void getUploadedPicture(HttpServletResponse response, 
//				       @ModelAttribute("picturePath") Resource picturePath) throws IOException{
//		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
//		IOUtils.copy(picturePath.getInputStream(),response.getOutputStream());
//	}
        @RequestMapping(value="/uploadedPicture")
        public void getUploadedPicture(HttpServletResponse response) throws IOException{
            Resource picturePath = userSession.getPicturePath();
            if(picturePath == null){
                picturePath =anonymousPicture;
            }
            response.setHeader("Content-type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
            IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());
        }
	
	
	
	@RequestMapping("/uploadError")
	public String onUploadError(Locale locale){
		System.out.println("@@@@@@@@@@@@@@ WLAZLEM TU");
		ModelAndView modelAndView = new ModelAndView("user/pictureUpload");
		modelAndView.addObject("error",messageSource.getMessage("upload.file.too.big",null,locale));
                modelAndView.addObject("user",userSession.toUser());
		return "user/register";
	}
	
	
	private Resource copyFileToPicture(MultipartFile file) throws IOException{
		String fileExtension = getFileExtension(file.getOriginalFilename());
		File tempFile = File.createTempFile("pic", fileExtension,picturesDir.getFile());
		try(InputStream in = file.getInputStream();
			OutputStream out = new FileOutputStream(tempFile)){
			IOUtils.copy(in, out);
		}
		return new FileSystemResource(tempFile);
	}
	
	private boolean isImage(MultipartFile file){
		return file.getContentType().startsWith("image");
	}
	
	private static String getFileExtension(String name){
		return name.substring(name.lastIndexOf("."));
	}
}
