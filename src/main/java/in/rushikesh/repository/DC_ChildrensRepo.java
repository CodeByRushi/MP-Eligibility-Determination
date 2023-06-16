package in.rushikesh.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.rushikesh.entity.DC_Childrens;

public interface DC_ChildrensRepo extends JpaRepository<DC_Childrens, Serializable> {

	@Query(value = "From DC_Childrens where case_num=:cnum")
	public List<DC_Childrens> getDataByCaseNumber(Long cnum);
}
