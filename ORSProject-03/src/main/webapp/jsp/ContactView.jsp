<%@page import="in.co.rays.project_3.controller.ContactCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Contact View</title>
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
	<form action="<%=ORSView.CONTACT_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.ContactDTO"
			scope="request"></jsp:useBean>

		<div class="row pt-3 pb-4">
			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Contact</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Contact</h3>
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

						<!-- Name -->
						<b>Name</b><span style="color:red">*</span><br>
						<input type="text" name="name" class="form-control"
							placeholder="Enter Name"
							value="<%=DataUtility.getStringData(dto.getName())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("name", request)%>
						</font><br><br>

						<!-- Email -->
						<b>Email</b><span style="color:red">*</span><br>
						<input type="text" name="email" class="form-control"
							placeholder="Enter Email"
							value="<%=DataUtility.getStringData(dto.getEmail())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("email", request)%>
						</font><br><br>

						<!-- Mobile No -->
						<b>Mobile No</b><span style="color:red">*</span><br>
						<input type="text" name="mobileNo" class="form-control"
							placeholder="Enter Mobile Number"
							value="<%=DataUtility.getStringData(dto.getMobileNo())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("mobileNo", request)%>
						</font><br><br>

						<!-- Message -->
						<b>Message</b><span style="color:red">*</span><br>
						<textarea name="message" class="form-control"
							placeholder="Enter Message"
							rows="3"><%=DataUtility.getStringData(dto.getMessage())%></textarea>
						<font color="red">
							<%=ServletUtility.getErrorMessage("message", request)%>
						</font><br><br>

						<!-- Buttons -->
						<div class="text-center">
						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=ContactCtl.OP_UPDATE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=ContactCtl.OP_CANCEL%>">
						<%
							} else {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=ContactCtl.OP_SAVE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=ContactCtl.OP_RESET%>">
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
