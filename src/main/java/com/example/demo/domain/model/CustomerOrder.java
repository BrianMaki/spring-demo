package com.example.demo.domain.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "customer_order", uniqueConstraints = @UniqueConstraint(columnNames = { "customer_id", "order_id" }))
public class CustomerOrder {
	
	private static final String PG_UUID = "pg-uuid";
	private static final String UUID2 = "uuid2";
	
	@Id
	@Type(type = PG_UUID)
	@GeneratedValue(generator = UUID2)
	@GenericGenerator(name = UUID2, strategy = UUID2)
	@Column(name = "customer_order_id")
	private UUID customerOrderId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

}
