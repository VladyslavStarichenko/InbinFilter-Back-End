package nure.com.InbinFilter.controller.flat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nure.com.InbinFilter.dto.flat.FlatCreateDto;
import nure.com.InbinFilter.dto.flat.FlatGetDto;
import nure.com.InbinFilter.dto.flat.FlatPageResponse;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.repository.flat.FlatRepository;
import nure.com.InbinFilter.service.flat.FlatServiceImpl;
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
@RequestMapping("/house-complex/flat")
@Api(value = "Operations with exercises")
@CrossOrigin(origins = {"http://localhost:3000", "http://someserver:8000"},
        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST},
        allowCredentials = "true", maxAge = 3600, allowedHeaders = "*")
public class FlatController {

    private final FlatServiceImpl flatServiceImpl;
    private final FlatRepository flatRepository;


    @Autowired
    public FlatController(FlatServiceImpl flatServiceImpl, FlatRepository flatRepository) {
        this.flatServiceImpl = flatServiceImpl;
        this.flatRepository = flatRepository;
    }

    @ApiOperation(value = "Get flat by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("id/{id}")
    public ResponseEntity<FlatGetDto> getProgramById(@ApiParam(value = "Program id to search") @PathVariable Long id) {
        return new ResponseEntity<>(flatServiceImpl.fromFlat(flatServiceImpl.getFlatById(id)), HttpStatus.OK);
    }


    @ApiOperation(value = "Get all flats")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/pageNumber={pageNumber}/pageSize={pageSize}/sortBy={sortBy}")
    public ResponseEntity<FlatPageResponse> getAllPrograms(
            @ApiParam(value = "Page number to show") @PathVariable int pageNumber,
            @ApiParam(value = "Page size") @PathVariable int pageSize,
            @ApiParam(value = "Sort information by parameter")  @PathVariable(required = false) String sortBy
    ) {
        Page<Flat> allFlats = flatServiceImpl.getAllFlats(pageNumber, pageSize, sortBy);
        Page<FlatGetDto> page = new PageImpl<>(allFlats
                .stream()
                .map(flatServiceImpl::fromFlat)
                .collect(Collectors.toList()));
        List<FlatGetDto> resultList = page.toList();
        FlatPageResponse response = flatServiceImpl.fromPage(page, resultList, sortBy);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements((int) allFlats.getTotalElements());
        response.setTotalPages(allFlats.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Update flat address")
    @PutMapping("update/{flatId}/{newAddress}")
    ResponseEntity<FlatGetDto> updateName(
            @ApiParam(value = "Program name to update") @PathVariable Long flatId,
            @ApiParam(value = "New program name") @PathVariable String newAddress
    ) {
        Flat flat = flatServiceImpl.updateFlat(flatId, newAddress);
        return new ResponseEntity<>(flatServiceImpl.fromFlat(flat), HttpStatus.OK);
    }

    @ApiOperation(value = "Add new flat to the complex")
    @PostMapping
    public ResponseEntity<FlatGetDto> createFlat(@ApiParam(value = "Flat object to create") @RequestBody FlatCreateDto flatCreateDto) {
        Flat flat = flatServiceImpl.createFlat(flatCreateDto);
        FlatGetDto flatGetDto = flatServiceImpl.fromFlat(flat);
        return new ResponseEntity<>(flatGetDto, HttpStatus.CREATED);
    }



}
