<%@page import="in.co.rays.project_3.controller.EventCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Event View</title>
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
	<form action="<%=ORSView.EVENT_CTL%>" method="post">

		<jsp:useBean id="dto"
			class="in.co.rays.project_3.dto.EventDTO"
			scope="request"></jsp:useBean>

		<div class="row pt-3 pb-4">
			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Event</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Event</h3>
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

						<!-- Event Code -->
						<b>Event Code</b><span style="color:red">*</span><br>
						<input type="text" name="eventCode"
							class="form-control"
							placeholder="Enter Event Code"
							value="<%=DataUtility.getStringData(dto.getEventCode())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("eventCode", request)%>
						</font><br><br>

						<!-- Event Name -->
						<b>Event Name</b><span style="color:red">*</span><br>
						<input type="text" name="eventName"
							class="form-control"
							placeholder="Enter Event Name"
							value="<%=DataUtility.getStringData(dto.getEventName())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("eventName", request)%>
						</font><br><br>

						<!-- Organizer -->
						<b>Organizer</b><span style="color:red">*</span><br>
						<input type="text" name="organizer"
							class="form-control"
							placeholder="Enter Organizer Name"
							value="<%=DataUtility.getStringData(dto.getOrganizer())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("organizer", request)%>
						</font><br><br>

						<!-- Event Date -->
						<b>Event Date</b><span style="color:red">*</span><br>
						<input type="text" id="datepicker3"
							name="eventDate"
							class="form-control"
							readonly="readonly"
							value="<%=DataUtility.getDateString(dto.getEventDate())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("eventDate", request)%>
						</font><br><br>

						<!-- Buttons -->
						<div class="text-center">
						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=EventCtl.OP_UPDATE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=EventCtl.OP_CANCEL%>">
						<%
							} else {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=EventCtl.OP_SAVE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=EventCtl.OP_RESET%>">
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