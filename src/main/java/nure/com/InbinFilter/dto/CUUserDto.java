package nure.com.InbinFilter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CUUserDto {

    private String username;
    private String password;

//
//    public User toUser(){
//        User user = new User();
//        user.setUserName(username);
//        user.setEmail(email);
//        user.setAge(age);
//        user.setGoal(goal);
//        user.setPassword(password);
//        user.setWeight(weight);
//        return user;
//    }


}
