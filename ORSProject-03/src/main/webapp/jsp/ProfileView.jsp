<%@page import="in.co.rays.project_3.controller.ProfileCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Profile View</title>
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
	<form action="<%=ORSView.PROFILE_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.ProfileDTO"
			scope="request"></jsp:useBean>

		<div class="row pt-3 pb-4">
			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Profile</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Profile</h3>
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

						<!-- Full Name -->
						<b>Full Name</b><span style="color:red">*</span><br>
						<input type="text" name="fullName" class="form-control"
							placeholder="Enter Full Name"
							value="<%=DataUtility.getStringData(dto.getFullName())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("fullName", request)%>
						</font><br><br>

						<!-- Gender -->
						<b>Gender</b><span style="color:red">*</span><br>
						<select name="gender" class="form-control">
							<option value="">--Select--</option>
							<option value="Male"
								<%= "Male".equals(dto.getGender()) ? "selected" : "" %>>Male</option>
							<option value="Female"
								<%= "Female".equals(dto.getGender()) ? "selected" : "" %>>Female</option>
						</select>
						<font color="red">
							<%=ServletUtility.getErrorMessage("gender", request)%>
						</font><br><br>

						<!-- DOB -->
						<b>Date Of Birth</b><span style="color:red">*</span><br>
						<input type="text" id="datepicker3" name="dob"
							class="form-control" readonly="readonly"
							value="<%=DataUtility.getDateString(dto.getDob())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("dob", request)%>
						</font><br><br>

						<!-- Profile Status -->
						<b>Profile Status</b><span style="color:red">*</span><br>
						<input type="text" name="profileStatus"
							class="form-control"
							placeholder="Enter Profile Status"
							value="<%=DataUtility.getStringData(dto.getProfileStatus())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("profileStatus", request)%>
						</font><br><br>

						<!-- Buttons -->
						<div class="text-center">
						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=ProfileCtl.OP_UPDATE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=ProfileCtl.OP_CANCEL%>">
						<%
							} else {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=ProfileCtl.OP_SAVE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=ProfileCtl.OP_RESET%>">
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
