package in.rushikesh.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.rushikesh.entity.EligDetails;

public interface EligDetailsRepo extends JpaRepository<EligDetails, Serializable>{

	@Query(value = "From EligDetails where case_num=:cnum")
	public EligDetails getDataByCaseNumber(Long cnum);
}
