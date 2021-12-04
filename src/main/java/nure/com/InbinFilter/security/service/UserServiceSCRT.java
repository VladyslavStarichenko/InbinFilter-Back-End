package nure.com.InbinFilter.security.service;

import lombok.extern.slf4j.Slf4j;
import nure.com.InbinFilter.dto.AuthenticationDto;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.repository.role.RoleRepository;
import nure.com.InbinFilter.repository.user.UserRepository;
import nure.com.InbinFilter.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserServiceSCRT {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public UserServiceSCRT(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

    }


//    public Map<Object,Object> createUserResident(User user) {
//        if (!userRepository.existsByUserName(user.getUserName())) {
//            Role roleUser = roleRepository.findByName("RESIDENT");
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//            user.setRole(roleUser);
//            user.setStatus(Status.ACTIVE);
//            User registeredUser = userRepository.save(user);
//            log.info("IN register - user: {} successfully registered", registeredUser);
//
//            String token =  jwtTokenProvider.createToken(user.getUserName(), new ArrayList<>(Collections.singletonList(user.getRole())));
//            String userNameSignedIn =  user.getUserName();
//            Map<Object, Object> response = new HashMap<>();
//            response.put("username", userNameSignedIn);
//            response.put("token", token);
//            return response;
//        } else {
//            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//    }

    public Map<Object, Object> signIn(AuthenticationDto requestDto) throws AuthenticationException {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,requestDto.getPassword()));
            Optional<User> user = userRepository.findUserByUserName(username);

            if(!user.isPresent()){
                throw new UsernameNotFoundException("User with username: " + username + "wasn't found");
            }
            log.info("IN signIn - user: {} successfully signedIN", userRepository.findUserByUserName(username));
            String token = jwtTokenProvider.createToken(username, new ArrayList<>(Collections.singletonList(user.get().getRole())));

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return response;
        }catch (AuthenticationException exception){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public User getCurrentLoggedInUser() {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        if(userRepository.findUserByUserName(username).isPresent()){
            return userRepository.findUserByUserName(username).get();
        }
        throw new UsernameNotFoundException("User with username: " + username + "wasn't found, you should authorize firstly") {
        };
    }



}
