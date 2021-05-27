package com.example.demo.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.UpdateOrderRequest;
import com.example.demo.projection.OrderView;
import com.example.demo.service.OrderService;

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
@RequestMapping("/Order")
@Api(tags = { "Order Demo System" })
@SwaggerDefinition(tags = { @Tag(name = "Order Demo System", description = "Order Demo System") })
public class OrderController {

	private final OrderService orderService;
	
	@ApiOperation(value = "Create Order") 
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created new Order"),
            @ApiResponse(code = 400, message = "Update to update Order, Unique Constraint Exception"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
	public ResponseEntity<OrderResponse> create(
			@Valid @RequestBody @NotNull CreateOrderRequest request) {
		
		Set<ConstraintViolation<CreateOrderRequest>> violations = request.readyForSubmissionViolations();
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Invalid Create Order Request", violations);
		}
    	
	    return ResponseEntity
	    		.status(201)
	    		.body(orderService.create(request));
	}

	@ApiOperation(value = "Delete Order")
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully Deleted Order"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<Void> delete(@RequestParam @NotNull UUID id) {
		
		orderService.delete(id);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}
	
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read Order", response = OrderResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found the order with the given Id"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not found any order with the given Id")
    })
	public ResponseEntity<OrderView> get(@PathVariable UUID id) {
    	 return ResponseEntity
    			 .status(HttpStatus.OK)
    			 .body(orderService.get(id));
	}
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read Order List", response = Object.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found Orders"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<OrderResponse>> get() {

        return ResponseEntity
        		.status(HttpStatus.OK)
        		.body(orderService.get());
    }
    
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the Order"),
            @ApiResponse(code = 400, message = "Update to update Order, Unique Constraint Exception"),
            @ApiResponse(code = 400, message = "Unable to update Order, Optimistic Lock Exception"),
            @ApiResponse(code = 404, message = "Not found any Order to update with the given Order Id"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<OrderResponse> update(
            @Valid @RequestBody UpdateOrderRequest request) {
        Set<ConstraintViolation<UpdateOrderRequest>> violations = request.readyForSubmissionViolations();
        if (!violations.isEmpty()){
            throw new ConstraintViolationException("Invalid Update Order Request", violations);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.update(request));
    }
}
