package nure.com.InbinFilter.security.service;

import lombok.extern.slf4j.Slf4j;
import nure.com.InbinFilter.dto.AuthenticationDto;
import nure.com.InbinFilter.dto.cleaner.CleanerGetDto;
import nure.com.InbinFilter.dto.resident.ResidentGetDto;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.*;
import nure.com.InbinFilter.repository.cleaner.CleanerRepository;
import nure.com.InbinFilter.repository.flat.FlatRepository;
import nure.com.InbinFilter.repository.resident.ResidentRepository;
import nure.com.InbinFilter.repository.role.RoleRepository;
import nure.com.InbinFilter.repository.user.UserRepository;
import nure.com.InbinFilter.security.jwt.JwtTokenProvider;
import nure.com.InbinFilter.service.cleaner.CleanerServiceImpl;
import nure.com.InbinFilter.service.complex.ComplexServiceImpl;
import nure.com.InbinFilter.service.resident.ResidentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserServiceSCRT {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;
    private final CleanerRepository cleanerRepository;
    private final ComplexServiceImpl complexServiceImpl;


    @Autowired
    public UserServiceSCRT(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, ResidentRepository residentRepository, FlatRepository flatRepository, CleanerRepository cleanerRepository, ComplexServiceImpl complexServiceImpl) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.residentRepository = residentRepository;
        this.flatRepository = flatRepository;
        this.cleanerRepository = cleanerRepository;
        this.complexServiceImpl = complexServiceImpl;
    }

    public ResidentGetDto signUpResident(User user, Long flatId) {
        Pattern passWordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,30}$");
        Matcher matcherPassword = passWordPattern.matcher(user.getPassword());
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (!matcherPassword.matches()) {
            throw new CustomException("Password should contain at least one capital letter, one lowercase letter, special character," +
                    "length should be more or equals 8", HttpStatus.BAD_REQUEST);
        } else {
            Role roleUser = roleRepository.findByName("ROLE_RESIDENT");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(roleUser);
            user.setStatus(Status.ACTIVE);
            User registeredUser = userRepository.save(user);
            log.info("IN register - user: {} successfully registered", registeredUser);

            Resident resident = new Resident();
            resident.setBill(Math.round(0*100.0)/100.0);
            resident.setNotifications(new ArrayList<>());
            Optional<Flat> flatById = flatRepository.findById(flatId);
            flatById.ifPresent(resident::setFlat);
            resident.setWastes(new ArrayList<>());
            resident.setUser(registeredUser);
            Resident residentToSave = residentRepository.save(resident);
            return ResidentServiceImpl.fromResident(residentToSave);

        }
    }


    public CleanerGetDto signUpCleaner(User user, Long complexId) {
        User userLogged = getCurrentLoggedInUser();
        Pattern passWordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,30}$");
        Matcher matcherPassword = passWordPattern.matcher(user.getPassword());
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (!matcherPassword.matches()) {
            throw new CustomException("Password should contain at least one capital letter, one lowercase letter, special character," +
                    "length should be more or equals 8", HttpStatus.BAD_REQUEST);
        } else {
            Role roleUser = roleRepository.findByName("ROLE_CLEANER");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(roleUser);
            user.setStatus(Status.ACTIVE);
            User registeredUser = userRepository.save(user);
            log.info("IN register - user: {} successfully registered", registeredUser);

            Cleaner cleaner = new Cleaner();
            cleaner.setUser(user);
            List<HouseComplex> complexes = new ArrayList<>();
            complexes.add(complexServiceImpl.getComplex(user));
            cleaner.setComplexes(complexes);
            Cleaner cleanerToSave = cleanerRepository.save(cleaner);
            return CleanerServiceImpl.fromCleaner(cleanerToSave);
        }
    }





    public Map<Object, Object> signUpComplexAdmin(User user) {
        Pattern passWordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,30}$");
        Matcher matcherPassword = passWordPattern.matcher(user.getPassword());
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (!matcherPassword.matches()) {
            throw new CustomException("Password should contain at least one capital letter, one lowercase letter, special character," +
                    "length should be more or equals 8", HttpStatus.BAD_REQUEST);
        } else {
            Role roleUser = roleRepository.findByName("ROLE_COMPLEX_ADMIN");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(roleUser);
            user.setStatus(Status.ACTIVE);
            User registeredUser = userRepository.save(user);
            log.info("IN register - user: {} successfully registered", registeredUser);
            String token = jwtTokenProvider.createToken(user.getUserName(), new ArrayList<>(Collections.singletonList(user.getRole())));
            String userNameSignedIn = user.getUserName();
            Map<Object, Object> response = new HashMap<>();
            response.put("username", userNameSignedIn);
            response.put("token", token);
            return response;
        }
    }

    public void blockComplexAdmin(String userName){

    }

    public Map<Object, Object> signIn(AuthenticationDto requestDto) throws AuthenticationException {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            Optional<User> user = userRepository.findUserByUserName(username);

            if (!user.isPresent()) {
                throw new UsernameNotFoundException("User with username: " + username + "wasn't found");
            }
//            if(user.get().getStatus() == Status.NONACTIVE){
//                throw new CustomException("You've been blocked - conctact the Admin of service", HttpStatus.BAD_REQUEST);
//            }
            log.info("IN signIn - user: {} successfully signedIN", userRepository.findUserByUserName(username));
            String token = jwtTokenProvider.createToken(username, new ArrayList<>(Collections.singletonList(user.get().getRole())));

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            response.put("role", user.get().getRole().getName());
            return response;
        } catch (AuthenticationException exception) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public User getCurrentLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.findUserByUserName(username).isPresent()) {
            return userRepository.findUserByUserName(username).get();
        }
        throw new UsernameNotFoundException("User with username: " + username + "wasn't found, you should authorize firstly") {
        };
    }


}
