package gotogoal.model.user;

//package gotogoal.model;
//
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//
//import org.springframework.web.multipart.MultipartFile;
//
//public class UserForm {
//
//	
//    
//    @NotNull
//    @Size(min=2, max=20, message ="{user.firstName.size}")
//    private String firstName;
//    
//    @NotNull
//    @Size(min=2,max=20, message ="{user.lastName.size}")
//    private String lastName;
//    
//    @NotNull
//    @Min(value = 18, message = "{user.age.min}")
//    private Integer age;
//    
//    private MultipartFile profilePicture;
//    
//    public User toUser(){
//    	return new User(firstName, lastName, age);
//    }
//
//    
//
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public Integer getAge() {
//		return age;
//	}
//
//	public void setAge(Integer age) {
//		this.age = age;
//	}
//
//	public MultipartFile getProfilePicture() {
//		return profilePicture;
//	}
//
//	public void setProfilePicture(MultipartFile profilePicture) {
//		this.profilePicture = profilePicture;
//	}
//    
//    
//    
//	
//}
