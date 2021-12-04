package nure.com.InbinFilter.controller.authentication;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.AuthenticationDto;
import nure.com.InbinFilter.exeption.EmptyDataException;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@Api(value = "Authentication operations (login, sign up)")
@RequestMapping(value = "/auth/")
@CrossOrigin
public class AuthenticationController {


    private final UserServiceSCRT userService;

    @Autowired
    public AuthenticationController(UserServiceSCRT userService) {
        this.userService = userService;
    }


    @PostMapping("login")
    @ApiOperation(value = "Login to the system")
    public ResponseEntity login(@ApiParam(value = "Registered User object") @RequestBody AuthenticationDto requestDto) {
        if (requestDto == null) {
            throw new EmptyDataException("Invalid or empty input");
        }
        Map<Object, Object> response = userService.signIn(requestDto);
        return ResponseEntity.ok(response);
    }


//    @PostMapping("signUp")
//    @ApiOperation(value = "Sign up to the system")
//    public ResponseEntity signUp(@ApiParam(value = "User object to sign up to the system") @RequestBody CUUserDto user) {
//        if (user == null) {
//            throw new EmptyDataException("Invalid or empty input");
//        }
//        Map<Object, Object> response = userService.createUserResident(user.toUser());
//        return ResponseEntity.ok(response);
//    }
}