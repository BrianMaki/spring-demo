package com.example.demo.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateCustomerOrderRequest;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.dto.CustomerOrderResponse;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.dto.UpdateCustomerRequest;
import com.example.demo.projection.CustomerView;
import com.example.demo.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/Customer")
@Api(tags = { "Customer Demo System" })
@SwaggerDefinition(tags = { @Tag(name = "Customer Demo System", description = "Customer Demo System") })
public class CustomerController {

	private final CustomerService customerService;
	
	@RolesAllowed("spring-demo-api-admin")
	@ApiOperation(value = "Create Customer Order") 
	@PostMapping(path = "/Order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created new Customer Order"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
	public ResponseEntity<CustomerOrderResponse> addOrder(
			@Valid @RequestBody @NotNull CreateCustomerOrderRequest request) {
    	
		Set<ConstraintViolation<CreateCustomerOrderRequest>> violations = request.readyForSubmissionViolations();
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Invalid Create Customer Request", violations);
		}
		
	    return ResponseEntity
	    		.status(201)
	    		.body(customerService.addOrder(request));
	}
	
	@RolesAllowed("spring-demo-api-admin")
	@ApiOperation(value = "Create Customer") 
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created new Customer"),
            @ApiResponse(code = 400, message = "Update to create Customer, Unique Constraint Exception"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
	public ResponseEntity<CustomerResponse> create(
			@Valid @RequestBody @NotNull CreateCustomerRequest request) {
    	
		Set<ConstraintViolation<CreateCustomerRequest>> violations = request.readyForSubmissionViolations();
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Invalid Create Customer Request", violations);
		}
		
	    return ResponseEntity
	    		.status(201)
	    		.body(customerService.create(request));
	}

	@RolesAllowed("spring-demo-api-admin")
	@ApiOperation(value = "Delete Customer")
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Successfully Deleted Customer"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<HttpStatus> delete(@RequestParam @NotNull UUID customerId) {
		
		customerService.delete(customerId);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}
	
	@RolesAllowed("spring-demo-api-client")
    @GetMapping(path = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read Customer", response = CustomerView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found the customerwith the given Id"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not found any customer with the given Id")
    })
	public ResponseEntity<CustomerView> get(@PathVariable UUID customerId) {
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth.getAuthorities());
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(customerService.get(customerId));
	}
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read Customer List", response = Object.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found Customers"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<CustomerResponse>> get() {
        return ResponseEntity
        		.status(HttpStatus.OK)
        		.body(customerService.get());
    }
    
    @RolesAllowed("spring-demo-api-admin")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the Customer"),
            @ApiResponse(code = 400, message = "Update to update Customer, Unique Constraint Exception"),
            @ApiResponse(code = 400, message = "Unable to update Customer, Optimistic Lock Exception"),
            @ApiResponse(code = 404, message = "Not found any Customer to update with the given Customer Id"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<CustomerResponse> update(
            @Valid @RequestBody UpdateCustomerRequest request) {
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = request.readyForSubmissionViolations();
        if (!violations.isEmpty()){
            throw new ConstraintViolationException("Invalid Update Customer Request", violations);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.update(request));
    }
}
