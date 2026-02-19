<%@page import="in.co.rays.project_3.dto.AttendanceDTO"%>
<%@page import="in.co.rays.project_3.controller.AttendanceListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<html>
<head>
<title>Attendance List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/list2.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 85px;
}

.text {
	text-align: center;
}
</style>
</head>

<%@include file="Header.jsp"%>
<%@include file="calendar.jsp"%>

<body class="hm">

<form action="<%=ORSView.ATTENDANCE_LIST_CTL%>" method="post">

	<jsp:useBean id="dto"
		class="in.co.rays.project_3.dto.AttendanceDTO"
		scope="request"></jsp:useBean>

	<%
	int pageNo = ServletUtility.getPageNo(request);
	int pageSize = ServletUtility.getPageSize(request);
	int index = ((pageNo - 1) * pageSize) + 1;
	int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
	
	List<String> statusList =
		    (List<String>) request.getAttribute("statusList");


	List list = ServletUtility.getList(request);
	Iterator<AttendanceDTO> it = list.iterator();
	%>

	<%
	if (list.size() != 0) {
	%>

	<center>
		<h1 class="text-dark pt-3">
			<u>Attendance List</u>
		</h1>
	</center>

	<!-- Search Section -->
	<!-- Search Section -->
<div class="container mt-4">
	<div class="search-card">

		<div class="row">

			<!-- Name -->
			<div class="col-md-3">
				<label><b>Name</b></label>
				<input type="text" name="name"
					class="form-control"
					placeholder="Enter Student Name"
					value="<%=ServletUtility.getParameter("name", request)%>">
			</div>

			<!-- Attendance Date -->
			<div class="col-md-3">
				<label><b>Attendance Date</b></label>
				<input type="text" id="datepicker3"
					name="attendanceDate"
					class="form-control"
					readonly="readonly"
					placeholder="Select Date"
					value="<%=ServletUtility.getParameter("attendanceDate", request)%>">
			</div>

			<!-- Status -->
			<div class="col-md-3">
				<label><b>Status</b></label>
				<select name="attendanceStatus" class="form-control">
					<option value="">--Select Status--</option>
					<%
						String selected =
							ServletUtility.getParameter("attendanceStatus", request);

						for (String s : statusList) {
					%>
						<option value="<%=s%>"
							<%= s.equals(selected) ? "selected" : "" %>>
							<%=s%>
						</option>
					<%
						}
					%>
				</select>
			</div>

			<!-- Buttons -->
			<div class="col-md-3 text-center" style="padding-top:32px;">
				<input type="submit" class="btn btn-primary"
					name="operation"
					value="<%=AttendanceListCtl.OP_SEARCH%>">

				<input type="submit" class="btn btn-dark"
					name="operation"
					value="<%=AttendanceListCtl.OP_RESET%>">
			</div>

		</div>

	</div>
</div>


	<br>

	<!-- Table -->
	<div class="table-responsive">
		<table class="table table-bordered table-dark table-hover">
			<thead>
				<tr style="background-color: #8C8C8C;">
					<th><input type="checkbox" id="select_all"> Select All</th>
					<th class="text">S.NO</th>
					<th class="text">Name</th>
					<th class="text">Attendance Date</th>
					<th class="text">Status</th>
					<th class="text">Remarks</th>
					<th class="text">Edit</th>
				</tr>
			</thead>

			<%
			while (it.hasNext()) {
				dto = it.next();
			%>

			<tbody>
				<tr>
					<td align="center">
						<input type="checkbox" name="ids"
							value="<%=dto.getId()%>">
					</td>
					<td class="text"><%=index++%></td>
					<td class="text"><%=dto.getName()%></td>
					<td class="text">
						<%=DataUtility.getDateString(dto.getAttendanceDate())%>
					</td>
					<td class="text"><%=dto.getAttendanceStatus()%></td>
					<td class="text"><%=dto.getRemarks()%></td>
					<td class="text">
						<a href="AttendanceCtl?id=<%=dto.getId()%>">Edit</a>
					</td>
				</tr>
			</tbody>

			<%
			}
			%>

		</table>
	</div>

	<!-- Pagination Buttons -->
	<table width="100%">
		<tr>

			<td>
				<input type="submit" name="operation"
					class="btn btn-warning"
					value="<%=AttendanceListCtl.OP_PREVIOUS%>"
					<%=pageNo > 1 ? "" : "disabled"%>>
			</td>

			<td>
				<input type="submit" name="operation"
					class="btn btn-primary"
					value="<%=AttendanceListCtl.OP_NEW%>">
			</td>

			<td>
				<input type="submit" name="operation"
					class="btn btn-danger"
					value="<%=AttendanceListCtl.OP_DELETE%>">
			</td>

			<td align="right">
				<input type="submit" name="operation"
					class="btn btn-warning"
					value="<%=AttendanceListCtl.OP_NEXT%>"
					<%=(nextPageSize != 0) ? "" : "disabled"%>>
			</td>

		</tr>
	</table>

	<%
	} else {
	%>

	<center>
		<h1 style="font-size: 40px;">Attendance List</h1>
	</center>

	<div style="text-align: center;">
		<input type="submit" name="operation"
			class="btn btn-primary"
			value="<%=AttendanceListCtl.OP_BACK%>">
	</div>

	<%
	}
	%>

	<input type="hidden" name="pageNo" value="<%=pageNo%>">
	<input type="hidden" name="pageSize" value="<%=pageSize%>">

</form>

<%@include file="FooterView.jsp"%>

</body>
</html>
