package in.rushikesh.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table
@Data
public class CoTrigger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trig_id;	
	private Long case_num;	
	private String trg_status;	
	private Long notice; 	

}
