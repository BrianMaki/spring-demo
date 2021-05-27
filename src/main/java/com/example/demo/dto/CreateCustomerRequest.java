package com.example.demo.dto;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.demo.component.SpringApplicationContext;
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
@ApiModel("Create Customer Request")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateCustomerRequest {

	@NotNull(message = "First Name cannot be null.", groups = ReadyForSubmissionCheck.class)
	@Size(message = "First Name max length is 128.", max = 128, groups = ReadyForSubmissionCheck.class)
	private String firstName;
	
	@NotNull(message = "Last Name cannot be null.", groups = ReadyForSubmissionCheck.class)
	@Size(message = "Last Name max length is 128.", max = 128, groups = ReadyForSubmissionCheck.class)
	private String lastName;
	
	public Set<ConstraintViolation<CreateCustomerRequest>> readyForSubmissionViolations() {
		return SpringApplicationContext.getBean(Validator.class).validate(this, ReadyForSubmissionCheck.class);
	}
}
