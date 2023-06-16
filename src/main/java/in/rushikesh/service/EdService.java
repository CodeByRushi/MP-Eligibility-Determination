package in.rushikesh.service;

import in.rushikesh.bindings.EligDetailsBinding;
import in.rushikesh.entity.EligDetails;

public interface EdService {

	public EligDetailsBinding getEligibilityDetails(Long caseNum);

//	public String getPlanName(Long caseNum);

//	public EligDetailsBinding checkSNAPCondition(Long caseNum);

//	public EligDetailsBinding checkCCAPCondition(Long caseNum);

//	public EligDetailsBinding checkMedicadeCondition(Long caseNum);

//	public EligDetailsBinding checkMedicareCondition(Long caseNum);

//	public EligDetailsBinding checkNJWCondition(Long caseNum);

//	public EligDetailsBinding saveDataInEligibilityAndCoTrigger(Long caseNum, EligDetails entity);
}
