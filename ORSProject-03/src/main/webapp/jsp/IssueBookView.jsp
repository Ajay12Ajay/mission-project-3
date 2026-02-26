<%@page import="in.co.rays.project_3.controller.IssueBookCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Issue Book View</title>
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
	<form action="<%=ORSView.ISSUEBOOK_CTL%>" method="post">

		<jsp:useBean id="dto"
			class="in.co.rays.project_3.dto.IssueBookDTO"
			scope="request"></jsp:useBean>

		<div class="row pt-3 pb-4">
			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Issue Book</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Issue Book</h3>
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

						<!-- Issue Code -->
						<b>Issue Code</b><span style="color:red">*</span><br>
						<input type="text" name="issueCode"
							class="form-control"
							placeholder="Enter Issue Code"
							value="<%=DataUtility.getStringData(dto.getIssueCode())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("issueCode", request)%>
						</font><br><br>

						<!-- Student Name -->
						<b>Student Name</b><span style="color:red">*</span><br>
						<input type="text" name="studentName"
							class="form-control"
							placeholder="Enter Student Name"
							value="<%=DataUtility.getStringData(dto.getStudentName())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("studentName", request)%>
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

						<!-- Return Date -->
						<b>Return Date</b><span style="color:red">*</span><br>
						<input type="text" id="datepicker"
							name="returnDate"
							class="form-control"
							readonly="readonly"
							value="<%=DataUtility.getDateString(dto.getReturnDate())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("returnDate", request)%>
						</font><br><br>

						<!-- Buttons -->
						<div class="text-center">
						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=IssueBookCtl.OP_UPDATE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=IssueBookCtl.OP_CANCEL%>">
						<%
							} else {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=IssueBookCtl.OP_SAVE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=IssueBookCtl.OP_RESET%>">
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