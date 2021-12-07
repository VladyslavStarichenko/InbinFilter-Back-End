package nure.com.InbinFilter.controller.complex;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.complex.ComplexCreateDto;
import nure.com.InbinFilter.dto.complex.HouseComplexGetDto;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.repository.complex.ComplexRepository;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import nure.com.InbinFilter.service.complex.ComplexServiceImpl;
import nure.com.InbinFilter.service.flat.FlatServiceImpl;
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
    private final ComplexRepository complexRepository;
    private final FlatServiceImpl flatService;

    @Autowired
    public HouseComplexController(ComplexServiceImpl complexServiceImpl, UserServiceSCRT userServiceSCRT, ComplexRepository complexRepository, FlatServiceImpl flatService) {
        this.complexServiceImpl = complexServiceImpl;
        this.userServiceSCRT = userServiceSCRT;
        this.complexRepository = complexRepository;
        this.flatService = flatService;
    }



    @ApiOperation(value = "Get House Complex")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<HouseComplexGetDto> getComplex() {
        HouseComplex complex = complexServiceImpl.getComplex();
        HouseComplexGetDto houseComplexGetDto = flatService.fromHouseComplex(complex);
        return new ResponseEntity<>(houseComplexGetDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Register complex")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<HouseComplexGetDto> createComplex(@ApiParam(value = "Complex object to create") @RequestBody ComplexCreateDto complexCreateDto) {
        if(complexServiceImpl.getComplex(userServiceSCRT.getCurrentLoggedInUser()) != null){
            throw new CustomException("You're already have house complex",HttpStatus.BAD_REQUEST);
        }
        else if(complexRepository.existsHouseComplexByName(complexCreateDto.getName())){
            throw new CustomException("House Complex with same name is already exists",HttpStatus.IM_USED);
        }
        HouseComplex houseComplexToSave = ComplexCreateDto.fromDto(complexCreateDto,userServiceSCRT.getCurrentLoggedInUser());
        HouseComplex houseComplex = complexRepository.save(houseComplexToSave);
        HouseComplexGetDto getDto = flatService.fromHouseComplex(houseComplex);
        return new ResponseEntity<>(getDto, HttpStatus.CREATED);

    }
}
