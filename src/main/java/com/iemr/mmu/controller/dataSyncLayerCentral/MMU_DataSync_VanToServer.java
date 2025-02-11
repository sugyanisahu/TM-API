package com.iemr.mmu.controller.dataSyncLayerCentral;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iemr.mmu.data.syncActivity_syncLayer.SyncDownloadMaster;
import com.iemr.mmu.service.dataSyncLayerCentral.GetDataFromVanAndSyncToDBImpl;
import com.iemr.mmu.service.dataSyncLayerCentral.GetMasterDataFromCentralForVanImpl;
import com.iemr.mmu.utils.response.OutputResponse;

import io.swagger.annotations.ApiOperation;

/***
 * 
 * @author NE298657
 * @date 16-08-2018
 * @operation Class used for data sync from van-to-server & server-to-van
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/dataSync", headers = "Authorization")
public class MMU_DataSync_VanToServer {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private GetDataFromVanAndSyncToDBImpl getDataFromVanAndSyncToDBImpl;
	@Autowired
	private GetMasterDataFromCentralForVanImpl getMasterDataFromCentralForVanImpl;

	@CrossOrigin()
	@ApiOperation(value = "sync data from van-to-server", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/van-to-server" }, method = { RequestMethod.POST })
	public String dataSyncToServer(@RequestBody String requestOBJ,
			@RequestHeader(value = "Authorization") String Authorization) {
		OutputResponse response = new OutputResponse();
		try {
			String s = getDataFromVanAndSyncToDBImpl.syncDataToServer(requestOBJ, Authorization);
			if (s != null)
				response.setResponse(s);
			else
				response.setError(5000, "data dync failed");
		} catch (Exception e) {
			response.setError(e);
			logger.error("Upload SYNC Exception" + e);
		}
		return response.toString();
	}

	@CrossOrigin()
	@ApiOperation(value = "download data from server-to-van", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/server-to-van" }, method = { RequestMethod.POST })
	public String dataDownloadFromServer(@RequestBody SyncDownloadMaster syncDownloadMaster,
			@RequestHeader(value = "Authorization") String Authorization) {
		OutputResponse response = new OutputResponse();
		try {
			if (syncDownloadMaster != null) {
				String s = getMasterDataFromCentralForVanImpl.getMasterDataForVan(syncDownloadMaster);
				if (s != null)
					response.setResponse(s);
				else
					response.setError(5000, "Error in master download for table " + syncDownloadMaster.getSchemaName()
							+ "." + syncDownloadMaster.getTableName());
			} else {
				response.setError(5000, "Invalid request");
			}
		} catch (Exception e) {
			response.setError(e);
		}
		return response.toStringWithSerialization();
	}

}
