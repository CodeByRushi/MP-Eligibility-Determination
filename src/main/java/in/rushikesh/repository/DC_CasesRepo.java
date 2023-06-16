package in.rushikesh.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.rushikesh.entity.DC_Cases;

public interface DC_CasesRepo extends JpaRepository<DC_Cases, Serializable> {

	@Query(value = "Select plan_id From DC_Cases where case_num=:cnum")
	public Integer getPlanIdByCaseNumber(Long cnum);
	
	@Query(value = "From DC_Cases where case_num=:cnum")
	public DC_Cases getAllDataByCaseNumber(Long cnum);
}
