package in.rushikesh.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligDetailsBinding {

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
