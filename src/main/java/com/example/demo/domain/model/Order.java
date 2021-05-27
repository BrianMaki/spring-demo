package com.example.demo.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.example.demo.domain.enums.OrderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "orders", uniqueConstraints = @UniqueConstraint(columnNames = { "order_number" }))                                                                                                              
public class Order {
	
	private static final String ORG_HIBERNATE_TYPE_NUMERIC_BOOLEAN_TYPE = "org.hibernate.type.NumericBooleanType";
	private static final String SMALLINT = "smallint";
	private static final String PG_UUID = "pg-uuid";
	private static final String UUID2 = "uuid2";
	
	@Id
	@Type(type = PG_UUID)
	@GeneratedValue(generator = UUID2)
	@GenericGenerator(name = UUID2, strategy = UUID2)
	@Column(name = "order_id")
	private UUID orderId;
	
	@Column(name = "order_number", length = 32, nullable = false)
	private String orderNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderType type;
	
	@Builder.Default
	@Type(type = ORG_HIBERNATE_TYPE_NUMERIC_BOOLEAN_TYPE)
	@Column(columnDefinition = SMALLINT, nullable = false)
	private boolean active = true;

	@Version
	private long version;
	
	@Builder.Default
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<CustomerOrder> customerOrders = new HashSet<>();
}
