package com.iemr.mmu.data.anc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iemr.mmu.annotation.sqlInjectionSafe.SQLInjectionSafe;

public class WrapperImmunizationHistory {
	private Long beneficiaryRegID;
	private Long benVisitID;
	private Long visitCode;
	private Integer providerServiceMapID;
	private @SQLInjectionSafe String createdBy;
	private @SQLInjectionSafe String modifiedBy;

	private Integer vanID;
	private Integer parkingPlaceID;

	private ArrayList<ChildVaccineDetail1> immunizationList;

	public Integer getVanID() {
		return vanID;
	}

	public void setVanID(Integer vanID) {
		this.vanID = vanID;
	}

	public Integer getParkingPlaceID() {
		return parkingPlaceID;
	}

	public void setParkingPlaceID(Integer parkingPlaceID) {
		this.parkingPlaceID = parkingPlaceID;
	}

	public ArrayList<ChildVaccineDetail1> getImmunizationList() {
		return immunizationList;
	}

	public void setImmunizationList(ArrayList<ChildVaccineDetail1> immunizationList) {
		this.immunizationList = immunizationList;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getBenVisitID() {
		return benVisitID;
	}

	public void setBenVisitID(Long benVisitID) {
		this.benVisitID = benVisitID;
	}

	public Long getBeneficiaryRegID() {
		return beneficiaryRegID;
	}

	public void setBeneficiaryRegID(Long beneficiaryRegID) {
		this.beneficiaryRegID = beneficiaryRegID;
	}

	public Long getVisitCode() {
		return visitCode;
	}

	public void setVisitCode(Long visitCode) {
		this.visitCode = visitCode;
	}

	public Integer getProviderServiceMapID() {
		return providerServiceMapID;
	}

	public void setProviderServiceMapID(Integer providerServiceMapID) {
		this.providerServiceMapID = providerServiceMapID;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ArrayList<ChildVaccineDetail1> getBenChildVaccineDetails() {

		ArrayList<ChildVaccineDetail1> childVaccineDetailList = new ArrayList<ChildVaccineDetail1>();
		if (null != immunizationList && immunizationList.size() > 0) {
			for (ChildVaccineDetail1 childVaccineDetail : immunizationList) {

				List<Map<String, Object>> vaccinesList = childVaccineDetail.getVaccines();
				for (Map<String, Object> vaccine : vaccinesList) {
					ChildVaccineDetail1 vaccineDetail = new ChildVaccineDetail1(
							childVaccineDetail.getDefaultReceivingAge(), vaccine.get("vaccine").toString(),
							(Boolean) vaccine.get("status"), vaccine.get("sctCode") == null ? null : vaccine.get("sctCode").toString(),
									vaccine.get("sctTerm") == null ? null : vaccine.get("sctTerm").toString());
					vaccineDetail.setBeneficiaryRegID(beneficiaryRegID);
					vaccineDetail.setBenVisitID(benVisitID);
					vaccineDetail.setVisitCode(visitCode);
					vaccineDetail.setProviderServiceMapID(providerServiceMapID);
					vaccineDetail.setVanID(vanID);
					vaccineDetail.setParkingPlaceID(parkingPlaceID);
					vaccineDetail.setCreatedBy(createdBy);
					vaccineDetail.setModifiedBy(modifiedBy);
					childVaccineDetailList.add(vaccineDetail);
				}

			}
		} else {
			ChildVaccineDetail1 vaccineDetail = new ChildVaccineDetail1();
			vaccineDetail.setBeneficiaryRegID(beneficiaryRegID);
			vaccineDetail.setBenVisitID(benVisitID);
			vaccineDetail.setVisitCode(visitCode);
			vaccineDetail.setProviderServiceMapID(providerServiceMapID);
			vaccineDetail.setVanID(vanID);
			vaccineDetail.setParkingPlaceID(parkingPlaceID);
			vaccineDetail.setCreatedBy(createdBy);
			vaccineDetail.setModifiedBy(modifiedBy);
			childVaccineDetailList.add(vaccineDetail);
		}
		return childVaccineDetailList;
	}

	public static WrapperImmunizationHistory getChildVaccineDetail(ArrayList<Object[]> childVaccineDetail) {
		WrapperImmunizationHistory WIH = new WrapperImmunizationHistory();
		WIH.immunizationList = new ArrayList<ChildVaccineDetail1>();

		if (null != childVaccineDetail && childVaccineDetail.size() > 0) {
			Object[] obj1 = childVaccineDetail.get(0);
			WIH.beneficiaryRegID = (Long) obj1[0];
			WIH.benVisitID = (Long) obj1[1];
			WIH.providerServiceMapID = (Integer) obj1[2];
			WIH.visitCode = (Long) obj1[6];
			List<Map<String, Object>> vaccinesList = new ArrayList<Map<String, Object>>();
			ChildVaccineDetail1 childVaccine = null;
			Map<String, Object> vaccines = new HashMap<String, Object>();

			int size = childVaccineDetail.size();
			for (Object[] obj : childVaccineDetail) {
				if (!(null != childVaccine && childVaccine.getDefaultReceivingAge().equals((String) obj[3]))) {

					if (null != childVaccine) {
						WIH.immunizationList.add(childVaccine);
					}
					vaccinesList = new ArrayList<Map<String, Object>>();
					childVaccine = new ChildVaccineDetail1((String) obj[3]);
				}

				vaccines = new HashMap<String, Object>();
				vaccines.put("vaccine", (String) obj[4]);
				vaccines.put("status", (Boolean) obj[5]);
				vaccines.put("sctCode", (String) obj[7]);
				vaccines.put("sctTerm", (String) obj[8]);
				vaccinesList.add(vaccines);
				childVaccine.setVaccines(vaccinesList);

				int index = childVaccineDetail.indexOf(obj);
				if (index == size - 1) {
					WIH.immunizationList.add(childVaccine);
				}
			}
		}
		return WIH;
	}
}
