package com.iemr.mmu.service.ncdscreening;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.iemr.mmu.data.anc.WrapperAncFindings;
import com.iemr.mmu.data.anc.WrapperBenInvestigationANC;
import com.iemr.mmu.data.ncdcare.NCDCareDiagnosis;
import com.iemr.mmu.data.nurse.CommonUtilityClass;
import com.iemr.mmu.data.quickConsultation.PrescribedDrugDetail;
import com.iemr.mmu.data.quickConsultation.PrescriptionDetail;
import com.iemr.mmu.data.snomedct.SCTDescription;
import com.iemr.mmu.data.tele_consultation.TeleconsultationRequestOBJ;
import com.iemr.mmu.repo.quickConsultation.PrescriptionDetailRepo;
import com.iemr.mmu.service.common.transaction.CommonDoctorServiceImpl;
import com.iemr.mmu.service.common.transaction.CommonNurseServiceImpl;
import com.iemr.mmu.service.common.transaction.CommonServiceImpl;
import com.iemr.mmu.service.tele_consultation.SMSGatewayServiceImpl;
import com.iemr.mmu.service.tele_consultation.TeleConsultationServiceImpl;
import com.iemr.mmu.utils.mapper.InputMapper;

@Service
public class NCDSCreeningDoctorServiceImpl implements NCDSCreeningDoctorService {
    
	private PrescriptionDetailRepo prescriptionDetailRepo;

	@Autowired
	public void setPrescriptionDetailRepo(PrescriptionDetailRepo prescriptionDetailRepo) {
		this.prescriptionDetailRepo = prescriptionDetailRepo;
	}
	
	@Autowired
	private CommonDoctorServiceImpl commonDoctorServiceImpl;
	@Autowired
	private TeleConsultationServiceImpl teleConsultationServiceImpl;
	@Autowired
	private CommonNurseServiceImpl commonNurseServiceImpl;
	@Autowired
	private CommonServiceImpl commonServiceImpl;
	@Autowired
	private SMSGatewayServiceImpl sMSGatewayServiceImpl;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateDoctorData(JsonObject requestOBJ, String Authorization) throws Exception {
		int updateSuccessFlag = 0;
		Long prescriptionID = null;
		Long investigationSuccessFlag = null;
		Integer findingSuccessFlag = null;
		// Integer diagnosisSuccessFlag = null;
		Integer prescriptionSuccessFlag = null;
		Long referSaveSuccessFlag = null;
		// Integer tcRequestStatusFlag = null;

		if (requestOBJ != null) {
			TeleconsultationRequestOBJ tcRequestOBJ = null;
//			TcSpecialistSlotBookingRequestOBJ tcSpecialistSlotBookingRequestOBJ = null;
			CommonUtilityClass commonUtilityClass = InputMapper.gson().fromJson(requestOBJ, CommonUtilityClass.class);

			// teleconsultation request
			tcRequestOBJ = commonServiceImpl.createTcRequest(requestOBJ, commonUtilityClass, Authorization);
			if (tcRequestOBJ != null && (tcRequestOBJ.getTmRequestID() == null || tcRequestOBJ.getTmRequestID() <= 0))
				throw new RuntimeException("Error in creating TC request. Error occured while creating TC request ID");

			JsonArray testList = null;
			JsonArray drugList = null;

			Boolean isTestPrescribed = false;
			Boolean isMedicinePrescribed = false;

			// checking if test is prescribed
			if (requestOBJ.has("investigation") && !requestOBJ.get("investigation").isJsonNull()
					&& requestOBJ.get("investigation") != null) {
				testList = requestOBJ.getAsJsonObject("investigation").getAsJsonArray("laboratoryList");
				if (testList != null && !testList.isJsonNull() && testList.size() > 0)
					isTestPrescribed = true;
			}

			// checking if medicine is prescribed
			if (requestOBJ.has("prescription") && !requestOBJ.get("prescription").isJsonNull()
					&& requestOBJ.get("prescription") != null) {
				drugList = requestOBJ.getAsJsonArray("prescription");
				if (drugList != null && !drugList.isJsonNull() && drugList.size() > 0) {
					isMedicinePrescribed = true;
				}
			}

			if (requestOBJ.has("findings") && !requestOBJ.get("findings").isJsonNull()) {

				WrapperAncFindings wrapperAncFindings = InputMapper.gson().fromJson(requestOBJ.get("findings"),
						WrapperAncFindings.class);
				findingSuccessFlag = commonDoctorServiceImpl.updateDocFindings(wrapperAncFindings);

			} else {
				findingSuccessFlag = 1;
			}
			
			// creating prescription OBJ
						PrescriptionDetail prescriptionDetail = null;
						if (requestOBJ.has("diagnosis") && !requestOBJ.get("diagnosis").isJsonNull()) {
							// JsonObject diagnosisObj = requestOBJ.getAsJsonObject("diagnosis");
							prescriptionDetail = InputMapper.gson().fromJson(requestOBJ.get("diagnosis"), PrescriptionDetail.class);
						}			

			// generate WrapperBenInvestigationANC OBJ
			WrapperBenInvestigationANC wrapperBenInvestigationANC = InputMapper.gson()
					.fromJson(requestOBJ.get("investigation"), WrapperBenInvestigationANC.class);

//			//save specialist diagnosis
//			String instruction = null;
//			if (requestOBJ.has("diagnosis") && !requestOBJ.get("diagnosis").isJsonNull()
//			&& requestOBJ.get("diagnosis").getAsJsonObject().has("specialistDiagnosis")
//			&& !requestOBJ.get("diagnosis").getAsJsonObject().get("specialistDiagnosis").isJsonNull()) {
//				instruction = requestOBJ.get("diagnosis").getAsJsonObject().get("specialistDiagnosis").getAsString();
//			}
//			
//			// save doctor diagnosis
//			String doctorDiagnosis = null;
//			if (requestOBJ.has("diagnosis") && !requestOBJ.get("diagnosis").isJsonNull()
//					&& requestOBJ.get("diagnosis").getAsJsonObject().has("doctorDiagnosis")
//					&& !requestOBJ.get("diagnosis").getAsJsonObject().get("doctorDiagnosis").isJsonNull()) {
//				doctorDiagnosis = requestOBJ.get("diagnosis").getAsJsonObject().get("doctorDiagnosis").getAsString();
//			}

//			// generate prescription OBJ & diagnosis OBJ
//			PrescriptionDetail prescriptionDetail = null;
//			NCDCareDiagnosis ncdCareDiagnosis = null;
//			if (requestOBJ.has("diagnosis") && !requestOBJ.get("diagnosis").isJsonNull()) {
//				prescriptionDetail = InputMapper.gson().fromJson(requestOBJ.get("diagnosis"), PrescriptionDetail.class);
//				prescriptionDetail.setExternalInvestigation(wrapperBenInvestigationANC.getExternalInvestigations());
//				prescriptionID = prescriptionDetail.getPrescriptionID();
//				;
//			    if (commonUtilityClass.getIsSpecialist() && instruction != null)
//			                    prescriptionDetail.setInstruction(instruction);
//
//			    if (!commonUtilityClass.getIsSpecialist() && doctorDiagnosis != null)
//			                    prescriptionDetail.setDiagnosisProvided(doctorDiagnosis);
//
////				if (doctorDiagnosis != null)
////					prescriptionDetail.setDiagnosisProvided(doctorDiagnosis);
//				// ncdCareDiagnosis = InputMapper.gson().fromJson(requestOBJ.get("diagnosis"),
//				// NCDCareDiagnosis.class);
//			}
			
			if (prescriptionDetail != null) {
				prescriptionDetail.setExternalInvestigation(wrapperBenInvestigationANC.getExternalInvestigations());
				prescriptionID = prescriptionDetail.getPrescriptionID();
			}

			// update prescription
			if (prescriptionDetail != null) {
				int p = commonNurseServiceImpl.updatePrescription(prescriptionDetail);
			}

			// update prescribed lab test
			if (isTestPrescribed) {
				wrapperBenInvestigationANC.setPrescriptionID(prescriptionID);
				investigationSuccessFlag = commonNurseServiceImpl.saveBenInvestigation(wrapperBenInvestigationANC);
			} else {
				investigationSuccessFlag = new Long(1);
			}

			// update prescribed medicine
			if (isMedicinePrescribed) {
				PrescribedDrugDetail[] prescribedDrugDetail = InputMapper.gson()
						.fromJson(requestOBJ.get("prescription"), PrescribedDrugDetail[].class);
				List<PrescribedDrugDetail> prescribedDrugDetailList = Arrays.asList(prescribedDrugDetail);

				for (PrescribedDrugDetail tmpObj : prescribedDrugDetailList) {
					tmpObj.setPrescriptionID(prescriptionID);
					tmpObj.setBeneficiaryRegID(commonUtilityClass.getBeneficiaryRegID());
					tmpObj.setBenVisitID(commonUtilityClass.getBenVisitID());
					tmpObj.setVisitCode(commonUtilityClass.getVisitCode());
					tmpObj.setProviderServiceMapID(commonUtilityClass.getProviderServiceMapID());
				}
				Integer r = commonNurseServiceImpl.saveBenPrescribedDrugsList(prescribedDrugDetailList);
				if (r > 0 && r != null) {
					prescriptionSuccessFlag = r;
				}
			} else {
				prescriptionSuccessFlag = 1;
			}

			// update referral
			if (requestOBJ.has("refer") && !requestOBJ.get("refer").isJsonNull()) {
				referSaveSuccessFlag = commonDoctorServiceImpl
						.updateBenReferDetails(requestOBJ.get("refer").getAsJsonObject());
			} else {
				referSaveSuccessFlag = new Long(1);
			}

			// check if all data updated successfully
			if ((findingSuccessFlag != null && findingSuccessFlag > 0)
					&& (investigationSuccessFlag != null && investigationSuccessFlag > 0)
					&& (prescriptionSuccessFlag != null && prescriptionSuccessFlag > 0)
					&& (referSaveSuccessFlag != null && referSaveSuccessFlag > 0)) {

				// call method to update beneficiary flow table
				int i = commonDoctorServiceImpl.updateBenFlowtableAfterDocDataUpdate(commonUtilityClass,
						isTestPrescribed, isMedicinePrescribed, tcRequestOBJ);

				if (i > 0)
					updateSuccessFlag = 1;
				else
					throw new RuntimeException("Error occurred while updating data. Beneficiary status update failed");

				if (i > 0 && tcRequestOBJ != null && tcRequestOBJ.getWalkIn() == false) {
					int k = sMSGatewayServiceImpl.smsSenderGateway("schedule", commonUtilityClass.getBeneficiaryRegID(),
							tcRequestOBJ.getSpecializationID(), tcRequestOBJ.getTmRequestID(), null,
							commonUtilityClass.getCreatedBy(),
							tcRequestOBJ.getAllocationDate() != null ? String.valueOf(tcRequestOBJ.getAllocationDate())
									: "",
							null, Authorization);
				}

			} else {
				throw new RuntimeException();
			}
		} else {
			// request OBJ is null.
		}
		return updateSuccessFlag;
	}
	
	public String getNCDDiagnosisData(Long beneficiaryRegID, Long visitCode) {
		PrescriptionDetail obj;
		SCTDescription sctOBJ;
		ArrayList<SCTDescription> sctOBJList = new ArrayList<>();
		// ArrayList<Object[]> diagnosisDetails =
		// prescriptionDetailRepo.getBenPrescription(beneficiaryRegID, visitCode);
		// PrescriptionDetail diagnosisList =
		// PrescriptionDetail.getPrescriptions(diagnosisDetails);

		ArrayList<PrescriptionDetail> prescriptionDetailRS = prescriptionDetailRepo
				.findByBeneficiaryRegIDAndVisitCode(beneficiaryRegID, visitCode);

		if (prescriptionDetailRS != null && prescriptionDetailRS.size() > 0) {
			obj = prescriptionDetailRS.get(0);
			if (obj != null && obj.getDiagnosisProvided_SCTCode() != null && obj.getDiagnosisProvided() != null) {
				String[] conceptIDArr = obj.getDiagnosisProvided_SCTCode().split(Pattern.quote("  ||  "));
				String[] termArr = obj.getDiagnosisProvided().split(Pattern.quote("  ||  "));

				// StringBuilder pd = new StringBuilder();
				int pointer = 0;
				for (String s : termArr) {
					// if (termArr.length == (pointer + 1))
					// pd.append(s);
					// else
					// pd.append(s).append(" || ");
					sctOBJ = new SCTDescription();
					sctOBJ.setConceptID(conceptIDArr[pointer]);
					sctOBJ.setTerm(s);
					sctOBJList.add(sctOBJ);

					pointer++;
				}

				obj.setProvisionalDiagnosisList(sctOBJList);
				// obj.setDiagnosisProvided(pd.toString());
			}
		} else {
			obj = new PrescriptionDetail();
		}

		return new Gson().toJson(obj);
	}
}
