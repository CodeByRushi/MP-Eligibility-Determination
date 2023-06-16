package in.rushikesh.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import in.rushikesh.bindings.EligDetailsBinding;
import in.rushikesh.service.EdService;

@RestController
public class EdRestController {

	@Autowired
	private EdService service;

	@GetMapping("/eligibility/{caseNum}")
	public ResponseEntity<EligDetailsBinding> getEligDetails(@PathVariable Long caseNum) {
		EligDetailsBinding eligibilityDetails = service.getEligibilityDetails(caseNum);
		return new ResponseEntity<>(eligibilityDetails, HttpStatus.OK);
	}

}
