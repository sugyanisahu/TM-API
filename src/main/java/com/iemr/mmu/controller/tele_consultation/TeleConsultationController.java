package com.iemr.mmu.controller.tele_consultation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iemr.mmu.service.tele_consultation.TeleConsultationServiceImpl;
import com.iemr.mmu.utils.response.OutputResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/tc", headers = "Authorization")
public class TeleConsultationController {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private TeleConsultationServiceImpl teleConsultationServiceImpl;

	@CrossOrigin
	@ApiOperation(value = "update beneficiary arrival status based on request", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/update/benArrivalStatus" }, method = { RequestMethod.POST })
	public String benArrivalStatusUpdater(@RequestBody String requestOBJ) {
		OutputResponse response = new OutputResponse();
		try {
			if (requestOBJ != null) {
				int i = teleConsultationServiceImpl.updateBeneficiaryArrivalStatus(requestOBJ);
				if (i > 0)
					response.setResponse("Beneficiary arrival status updated successfully.");
				else
					response.setError(5000, "Error in updating beneficiary arrival status.");
			} else
				response.setError(5000, "Invalid request");
		} catch (Exception e) {
			logger.error("error while updating beneficiary arrival status = " + e);
			response.setError(5000, "Error while updating beneficiary arrival status." + e);
		}
		return response.toString();
	}

	@CrossOrigin
	@ApiOperation(value = "update beneficiary status based on request", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/cancel/benTCRequest" }, method = { RequestMethod.POST })
	public String updateBeneficiaryStatusToCancelTCRequest(@RequestBody String requestOBJ,
			@RequestHeader String Authorization) {
		OutputResponse response = new OutputResponse();
		try {
			if (requestOBJ != null) {
				int i = teleConsultationServiceImpl.updateBeneficiaryStatusToCancelTCRequest(requestOBJ, Authorization);
				if (i > 0)
					response.setResponse("Beneficiary TC request cancelled successfully.");
				else
					response.setError(5000, "Teleconsultation cancel request failed.");
			} else
				response.setError(5000, "Invalid request");
		} catch (Exception e) {
			logger.error("error while updating beneficiary status for Teleconsultation cancel request = " + e);
			response.setError(5000,
					"Error while updating beneficiary status for Teleconsultation cancel request = " + e);
		}
		return response.toString();
	}

	@CrossOrigin
	@ApiOperation(value = "Check if specialist can proceed with beneficiary", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/check/benTCRequestStatus" }, method = { RequestMethod.POST })
	public String checkBeneficiaryStatusToProceedWithSpecialist(@RequestBody String requestOBJ) {
		OutputResponse response = new OutputResponse();
		try {
			if (requestOBJ != null) {
				int i = teleConsultationServiceImpl.checkBeneficiaryStatusForSpecialistTransaction(requestOBJ);
				if (i > 0)
					response.setResponse("Specialist can proceed with beneficiary TM session.");
				else
					response.setError(5000, "Issue while fetching beneficiary status.");
			} else
				response.setError(5000, "Invalid request");
		} catch (Exception e) {
			logger.error("Issue while fetching beneficiary status =  " + e);
			response.setError(5000, "Issue while fetching beneficiary status = " + e);
		}
		return response.toString();
	}

	@CrossOrigin
	@ApiOperation(value = "create TC request from worklist whose visit is created", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/create/benTCRequestWithVisitCode" }, method = { RequestMethod.POST })
	public String createTCRequestForBeneficiary(@RequestBody String requestOBJ, @RequestHeader String Authorization) {
		OutputResponse response = new OutputResponse();
		try {
			if (requestOBJ != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(requestOBJ);
				jsnOBJ = jsnElmnt.getAsJsonObject();

				int i = teleConsultationServiceImpl.createTCRequestFromWorkList(jsnOBJ, Authorization);
				if (i > 0)
					response.setResponse("Teleconsultation request created successfully.");
				else
					response.setError(5000, "Issue while creating Teleconsultation request.");
			} else
				response.setError(5000, "Invalid request");
		} catch (Exception e) {
			logger.error("Issue while creating Teleconsultation request =  " + e);
			response.setError(5000, "Issue while creating Teleconsultation request = " + e);
		}
		return response.toString();
	}

	// TC request List
	@CrossOrigin
	@ApiOperation(value = "get tc request list for a specialist", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/getTCRequestList" }, method = { RequestMethod.POST })
	public String getTCSpecialistWorkListNew(@RequestBody String requestOBJ) {
		OutputResponse response = new OutputResponse();
		try {
			if (requestOBJ != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(requestOBJ);
				jsnOBJ = jsnElmnt.getAsJsonObject();

				String s = teleConsultationServiceImpl.getTCRequestListBySpecialistIdAndDate(
						jsnOBJ.get("psmID").getAsInt(), jsnOBJ.get("userID").getAsInt(),
						jsnOBJ.get("date").getAsString());
				if (s != null)
					response.setResponse(s);
			} else {
				logger.error("Invalid request, either ProviderServiceMapID or userID or reqDate is invalid");
				response.setError(5000,
						"Invalid request, either ProviderServiceMapID or UserID or RequestDate is invalid");
			}

		} catch (Exception e) {
			logger.error("Error in TC requestList" + e);
			response.setError(5000, "Error while getting TC requestList");
		}
		return response.toString();
	}

	@CrossOrigin
	@ApiOperation(value = "update first consultation start time", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/startconsultation" }, method = { RequestMethod.POST })
	public String startconsultation(@RequestBody String requestOBJ) {
		OutputResponse response = new OutputResponse();
		try {
			if (requestOBJ != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(requestOBJ);
				jsnOBJ = jsnElmnt.getAsJsonObject();

				Integer s = teleConsultationServiceImpl.startconsultation(
						jsnOBJ.get("benRegID").getAsLong(), jsnOBJ.get("visitCode").getAsLong());
				if (s != null)
					response.setResponse(s.toString());
			} else {
				logger.error("Invalid request, either ProviderServiceMapID or userID or reqDate is invalid");
				response.setError(5000,
						"Invalid request, either ProviderServiceMapID or UserID or RequestDate is invalid");
			}

		} catch (Exception e) {
			logger.error("Error in TC requestList" + e);
			response.setError(5000, "Error while getting TC requestList");
		}
		return response.toString();
	}
}
