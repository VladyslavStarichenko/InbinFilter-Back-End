package nure.com.InbinFilter.controller.cleaner;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.flat.FlatGetDto;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.repository.cleaner.CleanerRepository;
import nure.com.InbinFilter.repository.flat.FlatRepository;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import nure.com.InbinFilter.service.cleaner.CleanerServiceImpl;
import nure.com.InbinFilter.service.flat.FlatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cleaners/")
@Api(value = "Operations with bins")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class CleanerController {

    private final CleanerRepository cleanerRepository;
    private final CleanerServiceImpl cleanerService;
    private final FlatServiceImpl flatService;
    private final UserServiceSCRT userServiceSCRT;
    private final FlatRepository flatRepository;

    @Autowired
    public CleanerController(CleanerRepository cleanerRepository, CleanerServiceImpl cleanerService, FlatServiceImpl flatService, UserServiceSCRT userServiceSCRT, FlatRepository flatRepository) {
        this.cleanerRepository = cleanerRepository;
        this.cleanerService = cleanerService;
        this.flatService = flatService;

        this.userServiceSCRT = userServiceSCRT;
        this.flatRepository = flatRepository;
    }

    @ApiOperation(value = "Create bin")
    @PreAuthorize("hasRole('ROLE_CLEANER')")
    @PostMapping("clean/{binId}")
    public ResponseEntity<String> clean(
            @ApiParam(value = "Bin id") @PathVariable Long binId) {

        cleanerService.clean(binId);
        return new ResponseEntity<>("Bin with id: " + binId + " was successfully cleaned", HttpStatus.OK);
    }


    @ApiOperation(value = "Get flats")
    @PreAuthorize("hasRole('ROLE_CLEANER')")
    @GetMapping("cleanersBin")
    public ResponseEntity<List<FlatGetDto>> getAllBins() {
        User user = userServiceSCRT.getCurrentLoggedInUser();
        List<Flat> flats = flatRepository.findAllFlatsBins(user.getUserName());
        List<FlatGetDto> resultList = flats.stream()
                .map(flatService::fromFlat)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }


}