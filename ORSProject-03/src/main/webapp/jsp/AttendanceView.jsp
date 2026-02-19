<%@page import="in.co.rays.project_3.controller.AttendanceCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Attendance View</title>
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
<form action="<%=ORSView.ATTENDANCE_CTL%>" method="post">

	<jsp:useBean id="dto"
		class="in.co.rays.project_3.dto.AttendanceDTO"
		scope="request"></jsp:useBean>

	<div class="row pt-3 pb-4">
		<div class="col-md-4"></div>

		<div class="col-md-4">
			<div class="card">
				<div class="card-body">

					<%
					if (dto.getId() != null && dto.getId() > 0) {
					%>
					<h3 class="text-center text-primary">Update Attendance</h3>
					<%
					} else {
					%>
					<h3 class="text-center text-primary">Add Attendance</h3>
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
					<input type="text" name="name"
						class="form-control"
						placeholder="Enter Name"
						value="<%=DataUtility.getStringData(dto.getName())%>">
					<font color="red">
						<%=ServletUtility.getErrorMessage("name", request)%>
					</font>
					<br><br>

					<!-- Attendance Date -->
					<b>Attendance Date</b><span style="color:red">*</span><br>
					<input type="text" id="datepicker3"
						name="attendanceDate"
						class="form-control"
						readonly="readonly"
						value="<%=DataUtility.getDateString(dto.getAttendanceDate())%>">
					<font color="red">
						<%=ServletUtility.getErrorMessage("attendanceDate", request)%>
					</font>
					<br><br>

					<!-- Attendance Status (Static Preload) -->
					<b>Attendance Status</b><span style="color:red">*</span><br>
					<select name="attendanceStatus"
						class="form-control">
						<option value="">--Select--</option>
						<option value="Present"
							<%= "Present".equals(dto.getAttendanceStatus()) ? "selected" : "" %>>
							Present
						</option>
						<option value="Absent"
							<%= "Absent".equals(dto.getAttendanceStatus()) ? "selected" : "" %>>
							Absent
						</option>
					</select>
					<font color="red">
						<%=ServletUtility.getErrorMessage("attendanceStatus", request)%>
					</font>
					<br><br>

					<!-- Remarks -->
					<b>Remarks</b><br>
					<textarea name="remarks"
						class="form-control"
						placeholder="Enter Remarks"><%=DataUtility.getStringData(dto.getRemarks())%></textarea>
					<br><br>

					<!-- Buttons -->
					<div class="text-center">
					<%
					if (dto.getId() != null && dto.getId() > 0) {
					%>
						<input type="submit" name="operation"
							class="btn btn-success"
							value="<%=AttendanceCtl.OP_UPDATE%>">

						<input type="submit" name="operation"
							class="btn btn-warning"
							value="<%=AttendanceCtl.OP_CANCEL%>">
					<%
					} else {
					%>
						<input type="submit" name="operation"
							class="btn btn-success"
							value="<%=AttendanceCtl.OP_SAVE%>">

						<input type="submit" name="operation"
							class="btn btn-warning"
							value="<%=AttendanceCtl.OP_RESET%>">
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
