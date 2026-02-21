package in.co.rays.project_3.dto;

import java.util.Date;

public class VisitorDTO extends BaseDTO {

	private String visitorPassCode;

	private String visitorName;

	private String visitorPurpose;

	private Date visitDate;

	private String visitStatus;

	public String getVisitorPassCode() {
		return visitorPassCode;
	}

	public void setVisitorPassCode(String visitorPassCode) {
		this.visitorPassCode = visitorPassCode;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public String getVisitorPurpose() {
		return visitorPurpose;
	}

	public void setVisitorPurpose(String visitorPurpose) {
		this.visitorPurpose = visitorPurpose;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitStatus() {
		return visitStatus;
	}

	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
