package in.rushikesh.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table
@Data
public class EligDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer elig_id; 		
	private Long case_num;		
	private String holder_name;		
	private Long holder_ssn;		
	private String plan_name;		
	private String plan_status;		
	private LocalDate plan_start_date;		
	private LocalDate plan_end_date;		
	private Integer benefit_amt;		
	private String reason_of_denial;
}
