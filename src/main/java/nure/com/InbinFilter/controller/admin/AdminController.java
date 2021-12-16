package nure.com.InbinFilter.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.AuthorizationDto;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.exeption.EmptyDataException;
import nure.com.InbinFilter.models.user.Status;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.repository.user.UserRepository;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@Api(value = "Admin operations")
@RequestMapping(value = "/admin/")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class AdminController {

    private final UserServiceSCRT userServiceSCRT;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserServiceSCRT userServiceSCRT, UserRepository userRepository) {
        this.userServiceSCRT = userServiceSCRT;
        this.userRepository = userRepository;
    }

    @PostMapping("registerComplexAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Register Admin")
    public ResponseEntity<Map<Object, Object>> signUp(@ApiParam(value = "User object to sign up to the system") @RequestBody AuthorizationDto user) {
        if (user == null) {
            throw new EmptyDataException("Invalid or empty input");
        }
        Map<Object, Object> response = userServiceSCRT.signUpComplexAdmin(user.toUser());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("blockComplexAdmin/{adminName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Register Admin")
    public ResponseEntity<String> block(@ApiParam(value = "User object to sign up to the system") @PathVariable String adminName) {
        Optional<User> userDb = userRepository.findUserByUserName(adminName);
        if (userDb.isPresent()) {
            User user = userDb.get();
            user.setStatus(Status.NONACTIVE);
            userRepository.save(user);
        } else {
            throw new CustomException("There is no admin with specified name", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("User with name: " + userDb.get().getUserName() + " was successfully blocked",
                HttpStatus.CREATED);
    }


}
