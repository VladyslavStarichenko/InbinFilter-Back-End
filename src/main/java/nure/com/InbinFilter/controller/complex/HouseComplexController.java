package nure.com.InbinFilter.controller.complex;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import nure.com.InbinFilter.service.complex.ComplexServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/house-complex/")
@Api(value = "Operations with exercises")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class HouseComplexController {

    private final ComplexServiceImpl complexServiceImpl;
    private final UserServiceSCRT userServiceSCRT;

    @Autowired
    public HouseComplexController(ComplexServiceImpl complexServiceImpl, UserServiceSCRT userServiceSCRT) {
        this.complexServiceImpl = complexServiceImpl;
        this.userServiceSCRT = userServiceSCRT;
    }



    @ApiOperation(value = "Get House Complex")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<HouseComplex> getComplex() {
        return new ResponseEntity<>(complexServiceImpl.getComplex(), HttpStatus.OK);
    }
}
