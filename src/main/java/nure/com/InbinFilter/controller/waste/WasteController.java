package nure.com.InbinFilter.controller.waste;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.waste.*;
import nure.com.InbinFilter.models.LitterType;
import nure.com.InbinFilter.models.Waste;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.models.user.User;
import nure.com.InbinFilter.security.service.UserServiceSCRT;
import nure.com.InbinFilter.service.resident.ResidentServiceImpl;
import nure.com.InbinFilter.service.waste.WasteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/house-complex/flat/residents/waste")
@Api(value = "Operations with exercises")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class WasteController {

    private final WasteServiceImpl wasteServiceImpl;
    private final UserServiceSCRT userServiceSCRT;
    private final ResidentServiceImpl residentService;

    @Autowired
    public WasteController(WasteServiceImpl wasteServiceImpl, UserServiceSCRT userServiceSCRT, ResidentServiceImpl residentService) {
        this.wasteServiceImpl = wasteServiceImpl;
        this.userServiceSCRT = userServiceSCRT;
        this.residentService = residentService;
    }

    @ApiOperation(value = "Get My Wastes")
    @GetMapping("myWastes/pageNumber={pageNumber}/pageSize={pageSize}")
    public ResponseEntity<WastePageResponse> getMyWasteStatistics(
            @ApiParam(value = "Page number to show") @PathVariable int pageNumber,
            @ApiParam(value = "Page size") @PathVariable int pageSize
    ) {
        User user = userServiceSCRT.getCurrentLoggedInUser();
        Page<Waste> allResidentWastes = wasteServiceImpl.getAllWastesByResident(pageNumber, pageSize,user.getResident().getId());
        Page<WasteGetDto> page = new PageImpl<>(allResidentWastes
                .stream()
                .map(wasteServiceImpl::fromWaste)
                .collect(Collectors.toList()));
        List<WasteGetDto> resultList = page.toList();
        WastePageResponse response = wasteServiceImpl.fromPage(page, resultList);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements((int) allResidentWastes.getTotalElements());
        response.setTotalPages(allResidentWastes.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Get All Wastes By Resident")
    @GetMapping("userWastes/pageNumber={pageNumber}/pageSize={pageSize}/{residentId}")
    public ResponseEntity<WastePageResponse> getCurrentUserWasteStatistics(
            @ApiParam(value = "Page number to show") @PathVariable int pageNumber,
            @ApiParam(value = "Page size") @PathVariable int pageSize,
            @ApiParam(value = "User id") @PathVariable Long residentId

    ) {
        Page<Waste> allResidentWastes = wasteServiceImpl.getAllWastesByResident(pageNumber, pageSize,residentId);
        Page<WasteGetDto> page = new PageImpl<>(allResidentWastes
                .stream()
                .map(wasteServiceImpl::fromWaste)
                .collect(Collectors.toList()));
        List<WasteGetDto> resultList = page.toList();
        WastePageResponse response = wasteServiceImpl.fromPage(page, resultList);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements((int) allResidentWastes.getTotalElements());
        response.setTotalPages(allResidentWastes.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @ApiOperation(value = "Get All Wastes By Resident ")
    @GetMapping("/litterType/statistics/resident/{residentId}")
    public ResponseEntity<WasteStatisticsDto> getUserWasteStatistics(
            @ApiParam(value = "Resident id") @PathVariable Long residentId
    ) {
        Resident resident = residentService.getResidentById(residentId);
        List<Waste> residentWastes = wasteServiceImpl.getAllWastesByResident(resident.getUser());
        WasteGenericDto plastics = wasteServiceImpl.getWastesByLitterType(residentWastes, LitterType.PLASTIC);
        WasteGenericDto other = wasteServiceImpl.getWastesByLitterType(residentWastes, LitterType.OTHERWASTE);
        WasteGenericDto paper = wasteServiceImpl.getWastesByLitterType(residentWastes, LitterType.PAPER);
        WasteGenericDto glass = wasteServiceImpl.getWastesByLitterType(residentWastes, LitterType.GLASS);
        WasteStatisticsDto statistics = new WasteStatisticsDto();
        statistics.getWastes().add(paper);
        statistics.getWastes().add(plastics);
        statistics.getWastes().add(glass);
        statistics.getWastes().add(other);
        statistics.setResidentId(residentWastes.get(0).getResident().getId());
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @ApiOperation(value = "Get All Wastes By Flat ")
    @GetMapping("/litterType/statistics/flat/{flatAddress}")
    public ResponseEntity<WasteStatisticsDto> getFlatWasteStatistics(
            @ApiParam(value = "Flat address") @PathVariable String flatAddress
    ) {
        List<Waste> residentWastes = wasteServiceImpl.getAllWastesByFlat(flatAddress);
        WasteGenericDto plastics = wasteServiceImpl.getWastesByLitterType(residentWastes, LitterType.PLASTIC);
        WasteGenericDto other = wasteServiceImpl.getWastesByLitterType(residentWastes, LitterType.OTHERWASTE);
        WasteGenericDto paper = wasteServiceImpl.getWastesByLitterType(residentWastes, LitterType.PAPER);
        WasteGenericDto glass = wasteServiceImpl.getWastesByLitterType(residentWastes, LitterType.GLASS);
        WasteStatisticsDto statistics = new WasteStatisticsDto();
        statistics.getWastes().add(paper);
        statistics.getWastes().add(plastics);
        statistics.getWastes().add(glass);
        statistics.getWastes().add(other);
        statistics.setResidentId(residentWastes.get(0).getResident().getId());
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @ApiOperation(value = "Commit the waste")
    @PostMapping
    public ResponseEntity<String> commitWaste(@ApiParam(value = "Waste object to create") @RequestBody WasteCreateDto wasteCreateDto) {
        wasteServiceImpl.commitWaste(wasteCreateDto.getLitterType(),wasteCreateDto.getAmount());
        return new ResponseEntity<>("You're successfully wasted ", HttpStatus.CREATED);
    }




}
