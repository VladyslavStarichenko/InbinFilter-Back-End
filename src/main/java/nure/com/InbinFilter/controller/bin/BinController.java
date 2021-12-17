package nure.com.InbinFilter.controller.bin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.bin.BinCreateDto;
import nure.com.InbinFilter.dto.bin.BinGetDto;
import nure.com.InbinFilter.exeption.CustomException;
import nure.com.InbinFilter.models.Bin;
import nure.com.InbinFilter.models.LitterType;
import nure.com.InbinFilter.repository.bin.BinRepository;
import nure.com.InbinFilter.service.bin.BinServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bin/")
@Api(value = "Operations with bins")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class BinController {

    private final BinRepository binRepository;
    private final BinServiceImpl binServiceImpl;

    public BinController(BinRepository binRepository, BinServiceImpl binServiceImpl) {
        this.binRepository = binRepository;
        this.binServiceImpl = binServiceImpl;
    }

    @ApiOperation(value = "Create bin")
    @PreAuthorize("hasRole('ROLE_COMPLEX_ADMIN')")
    @PostMapping("create/{flatId}")
    public ResponseEntity<BinGetDto> createBin(
            @ApiParam(value = "Complex object to create") @RequestBody BinCreateDto binCreateDto,
            @ApiParam(value = "Flat id") @PathVariable Long flatId) {

        return new ResponseEntity<>(binServiceImpl.
                fromBin(binServiceImpl.createBin(binCreateDto, flatId)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get bin by id")
    @PreAuthorize("hasRole('ROLE_COMPLEX_ADMIN') or hasRole('ROLE_CLEANER') ")
    @GetMapping("id/{id}")
    public ResponseEntity<BinGetDto> getResidentById(@ApiParam(value = "Bin id to search") @PathVariable Long id) {
        Optional<Bin> getBinById = binRepository.findById(id);
        if (getBinById.isPresent()) {
            return new ResponseEntity<>(binServiceImpl.fromBin(getBinById.get()), HttpStatus.OK);
        }
        throw new CustomException("There is no bin with specified Id", HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Get all bins in flat")
    @GetMapping("/flatId={id}")
    public ResponseEntity<List<BinGetDto>> getAllResidents(
            @ApiParam(value = "Flat id") @PathVariable(required = false) Long id
    ) {
        List<Bin> allBins = binServiceImpl.getAllBins(id);
        List<BinGetDto> resultSet = allBins.stream()
                .map(binServiceImpl::fromBin)
                .collect(Collectors.toList());

        return new ResponseEntity<>(resultSet, HttpStatus.OK);
    }

    @ApiOperation(value = "Update Bin")
    @PreAuthorize("hasRole('ROLE_COMPLEX_ADMIN')")
    @PutMapping("update/{binId}/capacity{capacity}/{litterType}/flatId{flatId}")
    ResponseEntity<BinGetDto> updateBin(
            @ApiParam(value = "Bin id to update") @PathVariable Long binId,
            @ApiParam(value = "Capacity") @PathVariable Double capacity,
            @ApiParam(value = "New LitterType") @PathVariable LitterType litterType,
            @ApiParam(value = "flatId") @PathVariable Long flatId
    ) {
        Bin bin = binServiceImpl.updateBin(binId, capacity, litterType, flatId);
        return new ResponseEntity<>(binServiceImpl.fromBin(bin), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_COMPLEX_ADMIN')")
    @ApiOperation(value = "Delete bin by id")
    @DeleteMapping("/{binId}")
    ResponseEntity<String> deleteBin(
            @ApiParam(value = "Bin id to delete") @PathVariable Long binId
    ) {
        binServiceImpl.deleteBinById(binId);
        return new ResponseEntity<>("Bin with id : " + binId  + "was successfully deleted", HttpStatus.OK);
    }

    @ApiOperation(value = "Make report")
    @PreAuthorize("hasRole('ROLE_COMPLEX_ADMIN')")
    @PutMapping("report/{flatId}")
    ResponseEntity<String> updateBin(
            @ApiParam(value = "Flat id to make bin report") @PathVariable Long flatId
    ) {
        List<Bin> bins = binServiceImpl.getAllBins(flatId);
        bins
                .forEach(binServiceImpl::makeReport);
        return new ResponseEntity<>("Report was successfully created",HttpStatus.CREATED);
    }


}
