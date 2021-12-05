package nure.com.InbinFilter.controller.authentication;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.AuthenticationDto;
import nure.com.InbinFilter.dto.AuthorizationDto;
import nure.com.InbinFilter.exeption.EmptyDataException;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<Map<Object, Object>> login(@ApiParam(value = "Registered User object") @RequestBody AuthenticationDto requestDto) {
        if (requestDto == null) {
            throw new UsernameNotFoundException("Invalid or empty input");
        }
        Map<Object, Object> response = userService.signIn(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("signUp")
    @ApiOperation(value = "Sign up to the system")
    public ResponseEntity<Map<Object, Object>> signUp(@ApiParam(value = "User object to sign up to the system") @RequestBody AuthorizationDto user) {
        if (user == null) {
            throw new EmptyDataException("Invalid or empty input");
        }
        Map<Object, Object> response = userService.signUpAdmin(user.toUser());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}