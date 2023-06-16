package in.rushikesh.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.rushikesh.entity.DC_Education;
import in.rushikesh.entity.DC_Income;

public interface DC_IncomeRepo extends JpaRepository<DC_Income, Serializable> {

	@Query(value = "From DC_Income where case_num=:cnum")
	public List<DC_Income> getDataByCaseNumber(Long cnum);
	
	@Query(value = "Select emp_income From DC_Income where case_num=:cnum")
	public Integer getIncomeByCaseNumber(Long cnum);
	
	
}
