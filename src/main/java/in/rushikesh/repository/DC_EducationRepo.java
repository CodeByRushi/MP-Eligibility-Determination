package in.rushikesh.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.rushikesh.entity.DC_Education;

public interface DC_EducationRepo extends JpaRepository<DC_Education, Serializable> {

	@Query(value = "From DC_Education where case_num=:cnum")
	public List<DC_Education> getDataByCaseNumber(Long cnum);
}
