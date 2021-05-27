package com.example.demo.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Customer Response")
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CustomerResponse {

	private UUID customerId;
	private String firstName;
	private String lastName;
	private boolean active;
	private long version;
}
