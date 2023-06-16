package in.rushikesh.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.rushikesh.entity.CitizenAppsEntity;

public interface CitizenAppsRepo extends JpaRepository<CitizenAppsEntity, Serializable> {
	

}
