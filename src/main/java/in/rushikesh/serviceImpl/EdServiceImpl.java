package in.rushikesh.serviceImpl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.rushikesh.bindings.EligDetailsBinding;
import in.rushikesh.entity.CitizenAppsEntity;
import in.rushikesh.entity.CoTrigger;
import in.rushikesh.entity.DC_Cases;
import in.rushikesh.entity.DC_Childrens;
import in.rushikesh.entity.DC_Education;
import in.rushikesh.entity.DC_Income;
import in.rushikesh.entity.EligDetails;
import in.rushikesh.entity.Plan;
import in.rushikesh.repository.CitizenAppsRepo;
import in.rushikesh.repository.CoTriggerRepo;
import in.rushikesh.repository.DC_CasesRepo;
import in.rushikesh.repository.DC_ChildrensRepo;
import in.rushikesh.repository.DC_EducationRepo;
import in.rushikesh.repository.DC_IncomeRepo;
import in.rushikesh.repository.EligDetailsRepo;
import in.rushikesh.repository.PlanRepo;
import in.rushikesh.service.EdService;

@Service
public class EdServiceImpl implements EdService {

	private static CitizenAppsRepo citizenRepo;

	private static CoTriggerRepo coTriggerRepo;

	private static DC_CasesRepo caseRepo;

	private static DC_ChildrensRepo childrenRepo;

	private static DC_EducationRepo educationRepo;

	private static DC_IncomeRepo incomeRepo;

	private static EligDetailsRepo eligibilityRepo;

	private static PlanRepo planRepo;

	@Autowired
	public void setDependencyCitizen(CitizenAppsRepo citizenRepo) {
		this.citizenRepo = citizenRepo;
	}

	@Autowired
	public void setDependencyCoTrigger(CoTriggerRepo coTriggerRepo) {
		this.coTriggerRepo = coTriggerRepo;
	}

	@Autowired
	public void setDependencyDC_Cases(DC_CasesRepo caseRepo) {
		this.caseRepo = caseRepo;
	}

	@Autowired
	public void setDependencyDC_Childrens(DC_ChildrensRepo childrenRepo) {
		this.childrenRepo = childrenRepo;
	}

	@Autowired
	public void setDependencyDC_Education(DC_EducationRepo educationRepo) {
		this.educationRepo = educationRepo;
	}

	@Autowired
	public void setDependency(DC_IncomeRepo incomeRepo) {
		this.incomeRepo = incomeRepo;
	}

	@Autowired
	public void setDependencyEligDetails(EligDetailsRepo eligibilityRepo) {
		this.eligibilityRepo = eligibilityRepo;
	}

	@Autowired
	public void setDependencyPlan(PlanRepo planRepo) {
		this.planRepo = planRepo;
	}

	@Override
	public EligDetailsBinding getEligibilityDetails(Long caseNum) {
		EligDetails dataByCaseNumber = eligibilityRepo.getDataByCaseNumber(caseNum);
		EligDetailsBinding binding = new EligDetailsBinding();

		if (dataByCaseNumber != null) {// When Eligibility is already determined.

			BeanUtils.copyProperties(dataByCaseNumber, binding);
			return binding;
		} else {// When Eligibility is not determined.

			// checking if valid case number
			DC_Cases dcCase = caseRepo.getAllDataByCaseNumber(caseNum);
			if (dcCase != null) {
				String planName = getPlanName(caseNum);
				if (planName.equals("SNAP"))
					return checkSNAPCondition(caseNum);
				else if (planName.equals("CCAP"))
					return checkCCAPCondition(caseNum);
				else if (planName.equals("Medicaid"))
					return checkMedicaidCondition(caseNum);
				else if (planName.equals("Medicare"))
					return checkMedicareCondition(caseNum);
				else if (planName.equals("NJW"))
					return checkNJWCondition(caseNum);
				else
					return null;// logic for this plan is not with us.
			} else {
				return null;// Invalid case number
			}

		}

	}

	private static String getPlanName(Long caseNum) {
		Integer planId = caseRepo.getPlanIdByCaseNumber(caseNum);
		Optional<Plan> plans = planRepo.findById(planId);
		if (plans.isPresent())
			return plans.get().getPlanName();
		return null;// Invalid Plan Id
	}

	private static Integer getAppId(Long caseNum) {

		DC_Cases caseData = caseRepo.getAllDataByCaseNumber(caseNum);
		return caseData.getApp_id();
	}

	private static EligDetailsBinding checkSNAPCondition(Long caseNum) {
		// SNAP Condition : If employment_income <=300$ then citizen is eligible for
		// SNAP
		Integer income = incomeRepo.getIncomeByCaseNumber(caseNum);
		Optional<CitizenAppsEntity> citizen = citizenRepo.findById(getAppId(caseNum));
		CitizenAppsEntity citizenAppsEntity = citizen.get();

		EligDetails entity = new EligDetails();
		entity.setCase_num(caseNum);
		entity.setHolder_name(citizenAppsEntity.getFullname());
		entity.setHolder_ssn(citizenAppsEntity.getSsn());
		entity.setPlan_name("SNAP");

		if (income <= 300) {
			entity.setPlan_status("Approved");
			entity.setPlan_start_date(LocalDate.now());
			entity.setPlan_end_date(LocalDate.now());
			entity.setBenefit_amt(500);
		} else {
			entity.setPlan_status("Rejected");
			entity.setReason_of_denial("Income is Greater than 300$.");
		}
		return saveDataInEligibilityAndCoTrigger(caseNum, entity);
	}

	private static Integer getAge(LocalDate dob) {

		// Get the current date
		LocalDate currentDate = LocalDate.now();

		// Calculate the age
		Period age = Period.between(dob, currentDate);

		return age.getYears();
	}

	private static EligDetailsBinding checkCCAPCondition(Long caseNum) {
//		CCAP Condition : If emplyment_income <=300$ and kids count > 0 and each kid age <=16 then eligible for CCAP
		Integer income = incomeRepo.getIncomeByCaseNumber(caseNum);
		Optional<CitizenAppsEntity> citizen = citizenRepo.findById(getAppId(caseNum));
		CitizenAppsEntity citizenAppsEntity = citizen.get();

		EligDetails entity = new EligDetails();
		entity.setCase_num(caseNum);
		entity.setHolder_name(citizenAppsEntity.getFullname());
		entity.setHolder_ssn(citizenAppsEntity.getSsn());
		entity.setPlan_name("CCAP");

		String reasonForDenial = null;
		if (income <= 300) {
			List<DC_Childrens> childrens = childrenRepo.getDataByCaseNumber(caseNum);
			if (childrens.size() > 0) {
				for (DC_Childrens child : childrens) {
					LocalDate dob = child.getChildren_dob();
					Integer age = getAge(dob);
					if (age <= 16) {
						continue;
					} else {
						reasonForDenial = "One of the child age is greater 16 years.";
						break;
					}

				}
			} else {
				reasonForDenial = "Citizen not having child.";
			}

		} else {
			reasonForDenial = "Income is Greater than 300$.";
		}
		if (reasonForDenial == null) {
			entity.setPlan_status("Approved");
			entity.setPlan_start_date(LocalDate.now());
			entity.setPlan_end_date(LocalDate.now());
			entity.setBenefit_amt(600);
		} else {
			entity.setPlan_status("Rejected");
			entity.setReason_of_denial(reasonForDenial);
		}
		return saveDataInEligibilityAndCoTrigger(caseNum, entity);

	}

	private static EligDetailsBinding checkMedicaidCondition(Long caseNum) {
//		Medicaid : If employment_income <=300$ and Property Income is 0 then eligible for Medicaid
		List<DC_Income> empIncome = incomeRepo.getDataByCaseNumber(caseNum);
		Optional<CitizenAppsEntity> citizen = citizenRepo.findById(getAppId(caseNum));

		CitizenAppsEntity citizenAppsEntity = citizen.get();
		EligDetails entity = new EligDetails();
		entity.setCase_num(caseNum);
		entity.setHolder_name(citizenAppsEntity.getFullname());
		entity.setHolder_ssn(citizenAppsEntity.getSsn());
		entity.setPlan_name("Medicaid");

		String reasonForDenial = null;
		if (empIncome.get(0).getEmp_income() <= 300) {

			if (empIncome.get(0).getProperty_income() != 0) {

				reasonForDenial = "Property Income is greater than Zero.";
			}
		} else {
			reasonForDenial = "Income is Greater than 300$.";
		}

		if (reasonForDenial == null) {
			entity.setPlan_status("Approved");
			entity.setPlan_start_date(LocalDate.now());
			entity.setPlan_end_date(LocalDate.now());
			entity.setBenefit_amt(700);
		} else {
			entity.setPlan_status("Rejected");
			entity.setReason_of_denial(reasonForDenial);
		}
		return saveDataInEligibilityAndCoTrigger(caseNum, entity);
	}

	private static EligDetailsBinding checkMedicareCondition(Long caseNum) {
//		Medicare : If citizen age is >=65 then eligible for Medicare
		Integer appId = getAppId(caseNum);
		Optional<CitizenAppsEntity> citizen = citizenRepo.findById(appId);
		CitizenAppsEntity citizenAppsEntity = citizen.get();
		EligDetails entity = new EligDetails();
		entity.setCase_num(caseNum);
		entity.setHolder_name(citizenAppsEntity.getFullname());
		entity.setHolder_ssn(citizenAppsEntity.getSsn());
		entity.setPlan_name("Medicaid");
		Integer age = getAge(citizenAppsEntity.getDob());
		String reasonForDenial = null;
		if (age < 65) {
			reasonForDenial = "Citizen Age is less than 65";
		}
		if (reasonForDenial == null) {
			entity.setPlan_status("Approved");
			entity.setPlan_start_date(LocalDate.now());
			entity.setPlan_end_date(LocalDate.now());
			entity.setBenefit_amt(800);
		} else {
			entity.setPlan_status("Rejected");
			entity.setReason_of_denial(reasonForDenial);
		}
		return saveDataInEligibilityAndCoTrigger(caseNum, entity);
	}

	private static EligDetailsBinding checkNJWCondition(Long caseNum) {
//		NJW : If citizen is un-employed and graduated then eligible for NJW (New Jersey Works)

		List<DC_Income> income = incomeRepo.getDataByCaseNumber(caseNum);

		Integer appId = getAppId(caseNum);
		Optional<CitizenAppsEntity> citizen = citizenRepo.findById(appId);
		CitizenAppsEntity citizenAppsEntity = citizen.get();
		EligDetails entity = new EligDetails();
		entity.setCase_num(caseNum);
		entity.setHolder_name(citizenAppsEntity.getFullname());
		entity.setHolder_ssn(citizenAppsEntity.getSsn());
		entity.setPlan_name("NJW");

		List<DC_Education> education = educationRepo.getDataByCaseNumber(caseNum);
		String reasonForDenial = null;
		if (income.get(0).getEmp_income() == 0 && income.get(0).getProperty_income() == 0) {
			if (education.get(0).getGraduation_year() == null) {
				reasonForDenial = "Graduation is not completed.";
			}
		} else {
			reasonForDenial = "Citizen is employed";
		}

		if (reasonForDenial == null) {
			entity.setPlan_status("Approved");
			entity.setPlan_start_date(LocalDate.now());
			entity.setPlan_end_date(LocalDate.now());
			entity.setBenefit_amt(900);
		} else {
			entity.setPlan_status("Rejected");
			entity.setReason_of_denial(reasonForDenial);
		}
		return saveDataInEligibilityAndCoTrigger(caseNum, entity);

	}

	private static EligDetailsBinding saveDataInEligibilityAndCoTrigger(Long caseNum, EligDetails entity) {
		// Save the data into Elig_DTLS table and in cotrigger table

		EligDetails eligibilityDetails = eligibilityRepo.save(entity);
		CoTrigger coEntity = new CoTrigger();
		coEntity.setCase_num(caseNum);
		coEntity.setTrg_status("Pending");
		coEntity.setNotice(123123l);// random number
		CoTrigger trigger = coTriggerRepo.save(coEntity);

		EligDetailsBinding binding = new EligDetailsBinding();
		BeanUtils.copyProperties(eligibilityDetails, binding);
		return binding;
	}

}
