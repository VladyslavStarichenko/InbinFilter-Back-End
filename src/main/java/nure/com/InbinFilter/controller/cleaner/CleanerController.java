package nure.com.InbinFilter.controller.cleaner;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.repository.cleaner.CleanerRepository;
import nure.com.InbinFilter.service.cleaner.CleanerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bin/")
@Api(value = "Operations with bins")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class CleanerController {

    private final CleanerRepository cleanerRepository;
    private final CleanerServiceImpl cleanerService;

    @Autowired
    public CleanerController(CleanerRepository cleanerRepository, CleanerServiceImpl cleanerService) {
        this.cleanerRepository = cleanerRepository;
        this.cleanerService = cleanerService;
    }

    @ApiOperation(value = "Create bin")
    @PreAuthorize("hasRole('ROLE_CLEANER')")
    @PostMapping("clean/{binId}")
    public ResponseEntity<String> clean(
            @ApiParam(value = "Bin id") @PathVariable Long binId) {

        cleanerService.clean(binId);
        return new ResponseEntity<>("Bin with id: " + binId + " was successfully cleaned", HttpStatus.OK);
    }
}
