package com.iemr.mmu.data.masterdata.ncdscreening;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "m_idrsscreenquestions")
public class IDRS_ScreenQuestions {

	@Id
	@GeneratedValue
	@Expose
	@Column(name = "IDRSQuestionID")
	private Integer idrsQuestionID;

	@Expose
	@Column(name = "Question")
	private String question;

	@Expose
	@Column(name = "DiseaseQuestionType")
	private String DiseaseQuestionType;

	@Expose
	@Column(name = "Deleted")
	private Boolean deleted;

	public Integer getIdrsQuestionID() {
		return idrsQuestionID;
	}

	public void setIdrsQuestionID(Integer idrsQuestionID) {
		this.idrsQuestionID = idrsQuestionID;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getDiseaseQuestionType() {
		return DiseaseQuestionType;
	}

	public void setDiseaseQuestionType(String diseaseQuestionType) {
		DiseaseQuestionType = diseaseQuestionType;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
