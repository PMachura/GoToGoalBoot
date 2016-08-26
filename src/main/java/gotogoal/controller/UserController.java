package gotogoal.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Path;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import gotogoal.config.PLLocalDateFormatter;
import gotogoal.config.PictureUploadProperties;
import gotogoal.model.User;

@Controller
@RequestMapping("/user")
@SessionAttributes("picturePath")
public class UserController {
	
	public static final FileSystemResource PICTURES_DIR =  new FileSystemResource("./pictures");
	private final Resource picturesDir;
	private final Resource anonymousPicture;
	
	@Autowired
	public UserController(PictureUploadProperties uploadProperties){
		picturesDir=uploadProperties.getUploadPath();
		anonymousPicture=uploadProperties.getAnonymousPicture();
	}
	
	
	@ModelAttribute("dateFormat")
    public String localeFormat(Locale locale) {
        return PLLocalDateFormatter.getPattern(locale);
    }
	
	@ModelAttribute("picturePath")
	public Resource picturePath(){
		return anonymousPicture;
	}
	
	@ExceptionHandler(IOException.class)
	public ModelAndView IOExceptionHandle(IOException exception){
		ModelAndView modelAndView = new ModelAndView("user/pictureUpload");
		modelAndView.addObject("error", exception.getMessage());
		return modelAndView;
	}
	
//	@ExceptionHandler(MultipartException.class)
//	public ModelAndView IOExceptionHandle(MultipartException exception){
//		ModelAndView modelAndView = new ModelAndView("user/pictureUpload");
//		modelAndView.addObject("error", exception.getMessage());
//		return modelAndView;
//	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String registerForm(Model model, Locale locale){
	
		model.addAttribute("user", new User());
		return "user/register";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String register(@Valid User user, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			return "user/register";
		}
		return "redirect: profile";
	}
	
	@RequestMapping("/profile")
	public String profile(){
		return "home";
	}
	
	//****** PICTURE ********************/
	
	@RequestMapping("/picture")
	public String picture(){
		return "user/pictureUpload";
	}
	
	@RequestMapping(value = "/picture", method = RequestMethod.POST)
	public String picture(MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException{
		throw new IOException("Komunikat bledu");
//		if(file.isEmpty() || !isImage(file)){
//			redirectAttributes.addFlashAttribute("error","Niewlasciwy typ pliku");
//			return "redirect:/user/picture";
//		}
//		Resource picturePath = copyFileToPicture(file);		
//		model.addAttribute("picturePath",picturePath);
//		return "user/pictureUpload";
	}
	
	@RequestMapping(value="/uploadedPicture")
	public void getUploadedPicture(HttpServletResponse response, 
								   @ModelAttribute("picturePath") Resource picturePath) throws IOException{
		response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
		IOUtils.copy(picturePath.getInputStream(),response.getOutputStream());
	}
	
	
	
	@RequestMapping("/uploadError")
	public String onUploadError(HttpServletRequest request){
		System.out.println("@@@@@@@@@@@@@@ WLAZLEM TU");
		ModelAndView modelAndView = new ModelAndView("user/pictureUpload");
		modelAndView.addObject("error",request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE));
		return "user/pictureUpload";
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
