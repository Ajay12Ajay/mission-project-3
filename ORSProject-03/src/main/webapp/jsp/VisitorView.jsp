<%@page import="in.co.rays.project_3.controller.VisitorCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Visitor View</title>
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
	<form action="<%=ORSView.VISITOR_CTL%>" method="post">

		<jsp:useBean id="dto"
			class="in.co.rays.project_3.dto.VisitorDTO"
			scope="request"></jsp:useBean>

		<div class="row pt-3 pb-4">
			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Visitor</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Visitor</h3>
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

						<!-- Visitor Pass Code -->
						<b>Visitor Pass Code</b><span style="color:red">*</span><br>
						<input type="text" name="visitorPassCode"
							class="form-control"
							placeholder="Enter Visitor Pass Code"
							value="<%=DataUtility.getStringData(dto.getVisitorPassCode())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("visitorPassCode", request)%>
						</font><br><br>

						<!-- Visitor Name -->
						<b>Visitor Name</b><span style="color:red">*</span><br>
						<input type="text" name="visitorName" 
							class="form-control"
							placeholder="Enter Visitor Name"
							value="<%=DataUtility.getStringData(dto.getVisitorName())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("visitorName", request)%>
						</font><br><br>

						<!-- Visitor Purpose -->
						<b>Visitor Purpose</b><span style="color:red">*</span><br>
						<input type="text" name="visitorPurpose"
							class="form-control"
							placeholder="Enter Visitor Purpose"
							value="<%=DataUtility.getStringData(dto.getVisitorPurpose())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("visitorPurpose", request)%>
						</font><br><br>

						<!-- Visit Date -->
						<b>Visit Date</b><span style="color:red">*</span><br>
						<input type="text" id="datepicker3"
							name="visitDate"
							class="form-control"
							readonly="readonly"
							value="<%=DataUtility.getDateString(dto.getVisitDate())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("visitDate", request)%>
						</font><br><br>

						<!-- Visit Status (STATIC PRELOAD) -->
						<b>Visit Status</b><span style="color:red">*</span><br>
						<select name="visitStatus" class="form-control">
							<option value="">--Select Status--</option>

							<option value="Approved"
								<%= "Approved".equals(dto.getVisitStatus()) ? "selected" : "" %>>
								Approved
							</option>

							<option value="Pending"
								<%= "Pending".equals(dto.getVisitStatus()) ? "selected" : "" %>>
								Pending
							</option>

							<option value="Rejected"
								<%= "Rejected".equals(dto.getVisitStatus()) ? "selected" : "" %>>
								Rejected
							</option>

						</select>
						<font color="red">
							<%=ServletUtility.getErrorMessage("visitStatus", request)%>
						</font><br><br>

						<!-- Buttons -->
						<div class="text-center">
						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=VisitorCtl.OP_UPDATE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=VisitorCtl.OP_CANCEL%>">
						<%
							} else {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=VisitorCtl.OP_SAVE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=VisitorCtl.OP_RESET%>">
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