package com.example.demo.dto;

import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.demo.component.SpringApplicationContext;
import com.example.demo.domain.enums.OrderType;
import com.example.demo.domain.model.validation.ReadyForSubmissionCheck;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ApiModel("Create Customer Order Request")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateCustomerOrderRequest {

	@NotNull(message = "Customer Id cannot be null.", groups = ReadyForSubmissionCheck.class)
	private UUID customerId;
	
	@NotNull(message = "Order Number must not be null.", groups = ReadyForSubmissionCheck.class)
	@Size(message = "Order Number max length is 32.", max = 32, groups = ReadyForSubmissionCheck.class)
	private String orderNumber;
	
	@NotNull(message = "Order Type must not be null.", groups = ReadyForSubmissionCheck.class)
	private OrderType type;
	
	public Set<ConstraintViolation<CreateCustomerOrderRequest>> readyForSubmissionViolations() {
		return SpringApplicationContext.getBean(Validator.class).validate(this, ReadyForSubmissionCheck.class);
	}
}