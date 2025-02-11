package com.iemr.mmu.repo.masterrepo.nurse;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.iemr.mmu.data.masterdata.nurse.FamilyMemberType;
@Repository
@RestResource(exported = false)
public interface FamilyMemberMasterRepo extends CrudRepository<FamilyMemberType, Long>{

	@Query("select benRelationshipID, benRelationshipType, gender  from FamilyMemberType where deleted = false and benRelationshipID>3 order by benRelationshipType ")
	public ArrayList<Object[]> getFamilyMemberTypeMaster();
	
}
