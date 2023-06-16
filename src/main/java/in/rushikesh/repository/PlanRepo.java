package in.rushikesh.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.rushikesh.entity.Plan;

public interface PlanRepo extends JpaRepository<Plan, Serializable>{
	
	List<Plan> findByPlanId(Integer planId);

}
