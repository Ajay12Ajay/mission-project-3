package in.co.rays.project_3.dto;

import java.util.Date;

public class CertificateDTO extends BaseDTO {

	private String certificateName;

	private String issueTo;

	private Date issueDate;

	private String certificateStatus;

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getIssueTo() {
		return issueTo;
	}

	public void setIssueTo(String issueTo) {
		this.issueTo = issueTo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
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
