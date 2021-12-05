package nure.com.InbinFilter.controller.resident;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.resident.ResidentGetDto;
import nure.com.InbinFilter.dto.resident.ResidentResponsePage;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import nure.com.InbinFilter.service.resident.ResidentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/house-complex/flat/residents")
@Api(value = "Operations with exercises")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class ResidentController {

    private final ResidentServiceImpl residentServiceImpl;
    private final UserServiceSCRT userServiceSCRT;

    @Autowired
    public ResidentController(ResidentServiceImpl residentServiceImpl, UserServiceSCRT userServiceSCRT) {
        this.residentServiceImpl = residentServiceImpl;
        this.userServiceSCRT = userServiceSCRT;
    }

    @ApiOperation(value = "Get resident by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("id/{id}")
    public ResponseEntity<ResidentGetDto> getResidentById(@ApiParam(value = "Resident id to search") @PathVariable Long id) {

        return new ResponseEntity<>((residentServiceImpl.getResidentById(id)), HttpStatus.OK);
    }

    @ApiOperation(value = "Get resident account")
    @GetMapping("myAccount/")
    public ResponseEntity<ResidentGetDto> getResidentByName() {
        return new ResponseEntity<>((residentServiceImpl.getResidentAccount(userServiceSCRT.getCurrentLoggedInUser().getUserName())), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all flat residents")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/pageNumber={pageNumber}/pageSize={pageSize}/flatId={id}")
    public ResponseEntity<ResidentResponsePage> getAllResidents(
            @ApiParam(value = "Page number to show") @PathVariable int pageNumber,
            @ApiParam(value = "Page size") @PathVariable int pageSize,
            @ApiParam(value = "Sort information by parameter") @PathVariable(required = false) Long id
    ) {
        Page<Resident> allResidents = residentServiceImpl.getAllResidents(pageNumber, pageSize, id);
        Page<ResidentGetDto> page = new PageImpl<>(allResidents
                .stream()
                .map(residentServiceImpl::fromResident)
                .collect(Collectors.toList()));
        List<ResidentGetDto> resultList = page.toList();
        ResidentResponsePage response = residentServiceImpl.fromPage(page, resultList);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements((int) allResidents.getTotalElements());
        response.setTotalPages(allResidents.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}