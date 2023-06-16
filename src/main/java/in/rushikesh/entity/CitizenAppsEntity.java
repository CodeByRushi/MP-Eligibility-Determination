package in.rushikesh.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Table(name = "CITIZEN_APPS")
@Entity
@Data
public class CitizenAppsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer app_id;
	
	private String fullname;
	
	private String email;
	
	private Long phno;
	
	private Long ssn;
	
	private String gender;
	
	private LocalDate dob;
	
	private String state_name;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDate create_date;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDate update_date;
	
	private String created_by;
	
	private String updated_by;

}
