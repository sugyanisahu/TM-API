package com.iemr.mmu.repo.quickConsultation;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.iemr.mmu.data.quickConsultation.PrescriptionDetail;

@Repository
@RestResource(exported = false)
public interface PrescriptionDetailRepo extends CrudRepository<PrescriptionDetail, Long> {

	@Query(" SELECT prescriptionID, beneficiaryRegID, benVisitID, providerServiceMapID, diagnosisProvided, "
			+ " instruction, externalInvestigation, visitCode from PrescriptionDetail ba "
			+ " WHERE ba.beneficiaryRegID = :benRegID AND ba.visitCode = :visitCode AND ba.deleted = false "
			+ " ORDER BY createdDate DESC ")
	public ArrayList<Object[]> getBenPrescription(@Param("benRegID") Long benRegID, @Param("visitCode") Long visitCode);

	public ArrayList<PrescriptionDetail> findByBeneficiaryRegIDAndVisitCode(Long benRegID, Long visitCode);
	
	public ArrayList<PrescriptionDetail> findByBeneficiaryRegIDAndVisitCodeOrderByPrescriptionIDDesc(Long benRegID,
			Long visitCode);

	@Query(" SELECT prescriptionID, beneficiaryRegID, benVisitID, providerServiceMapID, diagnosisProvided, instruction, "
			+ " visitCode FROM PrescriptionDetail ba "
			+ " WHERE ba.beneficiaryRegID = :benRegID AND ba.visitCode = :visitCode "
			+ " AND ba.deleted = false ORDER BY createdDate DESC ")
	public ArrayList<Object[]> getGeneralOPDDiagnosisDetails(@Param("benRegID") Long benRegID,
			@Param("visitCode") Long visitCode);

	@Query("SELECT t from PrescriptionDetail t where t.prescriptionID = :prescriptionID AND "
			+ " t.beneficiaryRegID=:benRegID AND t.visitCode = :visitCode")
	public PrescriptionDetail getGeneralOPDDiagnosisStatus(@Param("benRegID") Long benRegID,
			@Param("visitCode") Long visitCode, @Param("prescriptionID") Long prescriptionID);

	@Query(nativeQuery = true, value = "SELECT ExternalInvestigation, instruction FROM t_prescription "
			+ " WHERE BeneficiaryRegID=:benRegID AND VisitCode = :visitCode ORDER BY CreatedDate DESC LIMIT 1")
	public ArrayList<Object[]> getExternalinvestigationForVisitCode(@Param("benRegID") Long benRegID,
			@Param("visitCode") Long visitCode);

	@Transactional
	@Modifying
	@Query("update PrescriptionDetail set diagnosisProvided=:diagnosisProvided, "
			+ " instruction=:instruction, externalInvestigation =:externalInvestigation, "
			+ " modifiedBy=:modifiedBy, processed=:processed, "
			+ " diagnosisProvided_SCTCode =:diagnosisProvided_SCTCode, "
			+ " diagnosisProvided_SCTTerm =:diagnosisProvided_SCTTerm "
			+ " where prescriptionID=:prescriptionID AND visitCode=:visitCode "
			+ " AND beneficiaryRegID=:beneficiaryRegID ")
	public int updatePrescription(@Param("diagnosisProvided") String diagnosisProvided,
			@Param("instruction") String instruction, @Param("modifiedBy") String modifiedBy,
			@Param("processed") String processed, @Param("beneficiaryRegID") Long beneficiaryRegID,
			@Param("visitCode") Long visitCode, @Param("prescriptionID") Long prescriptionID,
			@Param("externalInvestigation") String externalInvestigation,
			@Param("diagnosisProvided_SCTCode") String diagnosisProvided_SCTCode,
			@Param("diagnosisProvided_SCTTerm") String diagnosisProvided_SCTTerm);

	// covid 19
	// PrescriptionDetail findByBeneficiaryRegIDAndVisitCode(Long benRegID, Long
	// visitCode);
	@Query("SELECT diagnosisProvided from PrescriptionDetail t where t.visitCode = :visitCode AND t.prescriptionID = :prescriptionID AND (t.deleted IS FALSE OR t.deleted IS NULL) ")
	
	public List<Object> getProvisionalDiagnosis(@Param("visitCode") Long visitCode,@Param("prescriptionID") Long prescriptionID);

}
