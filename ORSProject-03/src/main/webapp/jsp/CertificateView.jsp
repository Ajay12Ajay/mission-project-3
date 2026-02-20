<%@page import="in.co.rays.project_3.controller.CertificateCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Certificate View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/user1.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 75px;
}
</style>
</head>

<body class="p4">

	<div class="header">
		<%@include file="Header.jsp"%>
		<%@include file="calendar.jsp"%>
	</div>

	<main>
	<form action="<%=ORSView.CERTIFICATE_CTL%>" method="post">

		<jsp:useBean id="dto"
			class="in.co.rays.project_3.dto.CertificateDTO"
			scope="request"></jsp:useBean>

		<div class="row pt-3 pb-4">
			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Certificate</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Certificate</h3>
						<%
							}
						%>

						<!-- Success Message -->
						<%
							if (!ServletUtility.getSuccessMessage(request).equals("")) {
						%>
						<div class="alert alert-success">
							<%=ServletUtility.getSuccessMessage(request)%>
						</div>
						<%
							}
						%>

						<!-- Error Message -->
						<%
							if (!ServletUtility.getErrorMessage(request).equals("")) {
						%>
						<div class="alert alert-danger">
							<%=ServletUtility.getErrorMessage(request)%>
						</div>
						<%
							}
						%>

						<input type="hidden" name="id" value="<%=dto.getId()%>">
						<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
						<input type="hidden" name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- Certificate Name -->
						<b>Certificate Name</b><span style="color:red">*</span><br>
						<input type="text" name="certificateName"
							class="form-control"
							placeholder="Enter Certificate Name"
							value="<%=DataUtility.getStringData(dto.getCertificateName())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("certificateName", request)%>
						</font><br><br>

						<!-- Issue To -->
						<b>Issue To</b><span style="color:red">*</span><br>
						<input type="text" name="issueTo"
							class="form-control"
							placeholder="Enter Issue To"
							value="<%=DataUtility.getStringData(dto.getIssueTo())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("issueTo", request)%>
						</font><br><br>

						<!-- Issue Date -->
						<b>Issue Date</b><span style="color:red">*</span><br>
						<input type="text" id="datepicker3"
							name="issueDate"
							class="form-control"
							readonly="readonly"
							value="<%=DataUtility.getDateString(dto.getIssueDate())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("issueDate", request)%>
						</font><br><br>

						<!-- Certificate Status (STATIC PRELOAD) -->
						<b>Certificate Status</b><span style="color:red">*</span><br>
						<select name="certificateStatus" class="form-control">
							<option value="">--Select Status--</option>

							<option value="Delivered"
								<%= "Delivered".equals(dto.getCertificateStatus()) ? "selected" : "" %>>
								Delivered
							</option>

							<option value="Non Delivered"
								<%= "Non Delivered".equals(dto.getCertificateStatus()) ? "selected" : "" %>>
								Non Delivered
							</option>

						</select>
						<font color="red">
							<%=ServletUtility.getErrorMessage("certificateStatus", request)%>
						</font><br><br>

						<!-- Buttons -->
						<div class="text-center">
						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=CertificateCtl.OP_UPDATE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=CertificateCtl.OP_CANCEL%>">
						<%
							} else {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=CertificateCtl.OP_SAVE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=CertificateCtl.OP_RESET%>">
						<%
							}
						%>
						</div>

					</div>
				</div>
			</div>

			<div class="col-md-4"></div>
		</div>

	</form>
	</main>

	<%@include file="FooterView.jsp"%>

</body>
</html>