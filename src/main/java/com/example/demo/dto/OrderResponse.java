package com.example.demo.dto;

import java.util.UUID;

import com.example.demo.domain.enums.OrderType;
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
@ApiModel("Order Reponse")
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderResponse {
	
	private UUID orderId;
	private String orderNumber;
	private OrderType type;
	private boolean active;
	private long version;
}
