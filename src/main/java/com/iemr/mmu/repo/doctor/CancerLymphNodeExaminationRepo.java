package com.iemr.mmu.repo.doctor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iemr.mmu.data.doctor.CancerLymphNodeDetails;

@Repository
@RestResource(exported = false)
public interface CancerLymphNodeExaminationRepo extends CrudRepository<CancerLymphNodeDetails, Long> {

	@Query(" SELECT c from CancerLymphNodeDetails c WHERE c.beneficiaryRegID = :benRegID "
			+ "AND c.deleted = false AND c.visitCode = :visitCode")
	public List<CancerLymphNodeDetails> getBenCancerLymphNodeDetails(@Param("benRegID") Long benRegID,
			@Param("visitCode") Long visitCode);

	@Query("SELECT ID, processed from CancerLymphNodeDetails where beneficiaryRegID=:benRegID AND visitCode = :visitCode AND deleted = false")
	public ArrayList<Object[]> getCancerLymphNodeDetailsStatus(@Param("benRegID") Long benRegID,
			@Param("visitCode") Long visitCode);

	@Query("SELECT ID, processed from CancerLymphNodeDetails where beneficiaryRegID=:benRegID "
			+ " AND visitCode = :visitCode AND deleted = false AND lymphNodeName IN (:nameList)")
	public ArrayList<Object[]> getCancerLymphNodeDetailsStatusForLymphnodeNameList(@Param("benRegID") Long benRegID,
			@Param("visitCode") Long visitCode, @Param("nameList") List<String> nameList);

	@Modifying
	@Transactional
	@Query("update CancerLymphNodeDetails set deleted=true, processed=:processed WHERE ID = :ID")
	public int deleteExistingLymphNodeDetails(@Param("ID") Long ID, @Param("processed") String processed);

	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query("update CancerLymphNodeDetails set providerServiceMapID=:providerServiceMapID, lymphNodeName=:lymphNodeName, "
	 * +
	 * "mobility_Left=:mobility_Left, size_Left=:size_Left, mobility_Right=:mobility_Right, "
	 * +
	 * "size_Right=:size_Right, modifiedBy=:modifiedBy, processed=:processed where "
	 * + " beneficiaryRegID=:benRegID AND benVisitID = :benVisitID") public int
	 * updateCancerLymphNodeDetails(@Param("providerServiceMapID") Integer
	 * providerServiceMapID,
	 * 
	 * @Param("lymphNodeName") String lymphNodeName,
	 * 
	 * @Param("mobility_Left") Boolean mobility_Left,
	 * 
	 * @Param("size_Left") String size_Left,
	 * 
	 * @Param("mobility_Right") Boolean mobility_Right,
	 * 
	 * @Param("size_Right") String size_Right,
	 * 
	 * @Param("modifiedBy") String modifiedBy,
	 * 
	 * @Param("benRegID") Long benRegID, @Param("benVisitID") Long benVisitID,
	 * 
	 * @Param("processed") String processed);
	 */
}
