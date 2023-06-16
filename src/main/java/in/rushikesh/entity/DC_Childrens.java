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

@Entity
@Table
@Data
public class DC_Childrens {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer children_id;
	
	private Long case_num;	
	
	private String children_name;
	
	private LocalDate children_dob;	
	
	private Integer children_ssn;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDate create_date;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDate update_date;
	
	private String created_by;
	
	private String updated_by;
}
