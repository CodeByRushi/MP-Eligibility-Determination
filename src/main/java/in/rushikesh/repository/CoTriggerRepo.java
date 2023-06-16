package in.rushikesh.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.rushikesh.entity.CoTrigger;

public interface CoTriggerRepo extends JpaRepository<CoTrigger, Serializable> {

}
