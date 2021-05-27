package com.example.demo.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = { "first_name", "last_name" }))
public class Customer {
	
	private static final String ORG_HIBERNATE_TYPE_NUMERIC_BOOLEAN_TYPE = "org.hibernate.type.NumericBooleanType";
	private static final String SMALLINT = "smallint";
	private static final String PG_UUID = "pg-uuid";
	private static final String UUID2 = "uuid2";
	
	@Id
	@Type(type = PG_UUID)
	@GeneratedValue(generator = UUID2)
	@GenericGenerator(name = UUID2, strategy = UUID2)
	@Column(name = "customer_id")
	private UUID customerId;
	
	@Column(name = "first_name", length = 128, nullable = false)
	private String firstName;
	
	@Column(name = "last_name", length = 128, nullable = false)
	private String lastName;
	
	@Builder.Default
	@Type(type = ORG_HIBERNATE_TYPE_NUMERIC_BOOLEAN_TYPE)
	@Column(columnDefinition = SMALLINT, nullable = false)
	private boolean active = true;

	@Version
	private long version;
	
	@Builder.Default
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<CustomerOrder> customerOrders = new HashSet<>();
}
