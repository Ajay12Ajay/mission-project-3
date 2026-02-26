<%@page import="in.co.rays.project_3.dto.EventDTO"%>
<%@page import="in.co.rays.project_3.controller.EventListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<html>
<head>
<title>Event List</title>

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

<body class="hm">

	<form action="<%=ORSView.EVENT_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.EventDTO"
			scope="request"></jsp:useBean>

		<%
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize(request);
		int index = ((pageNo - 1) * pageSize) + 1;
		int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

		List<String> eventNameList = (List<String>) request.getAttribute("eventNameList");
		List<String> organizerList = (List<String>) request.getAttribute("organizerList");
		List<String> eventDateList = (List<String>) request.getAttribute("eventDateList");

		List list = ServletUtility.getList(request);
		Iterator<EventDTO> it = list.iterator();
		%>

		<%
		if (list.size() != 0) {
		%>

		<center>
			<h1 class="text-dark pt-3">
				<u>Event List</u>
			</h1>
		</center>

		<!-- Search Section -->
		<div class="row justify-content-center mt-3">

			<!-- Dynamic Event Name -->
			<div class="col-md-3">
				<select name="eventName" class="form-control">
					<option value="">--Select Event Name--</option>
					<%
					String selectedEvent = ServletUtility.getParameter("eventName", request);
					if (eventNameList != null) {
						for (String s : eventNameList) {
					%>
					<option value="<%=s%>"
						<%=s.equals(selectedEvent) ? "selected" : ""%>>
						<%=s%>
					</option>
					<%
					}
					}
					%>
				</select>
			</div>

			<!-- Dynamic Organizer -->
			<div class="col-md-3">
				<select name="organizer" class="form-control">
					<option value="">--Select Organizer--</option>
					<%
					String selectedOrg = ServletUtility.getParameter("organizer", request);
					if (organizerList != null) {
						for (String s : organizerList) {
					%>
					<option value="<%=s%>" <%=s.equals(selectedOrg) ? "selected" : ""%>>
						<%=s%>
					</option>
					<%
					}
					}
					%>
				</select>
			</div>

			<!-- Dynamic Event Date -->
			<div class="col-md-3">
				<select name="eventDate" class="form-control">
					<option value="">--Select Event Date--</option>
					<%
					String selectedDate = ServletUtility.getParameter("eventDate", request);
					if (eventDateList != null) {
						for (String s : eventDateList) {
					%>
					<option value="<%=s%>"
						<%=s.equals(selectedDate) ? "selected" : ""%>>
						<%=s%>
					</option>
					<%
					}
					}
					%>
				</select>
			</div>

			<div class="col-md-3 text-center mt-2">
				<input type="submit" class="btn btn-primary" name="operation"
					value="<%=EventListCtl.OP_SEARCH%>"> <input type="submit"
					class="btn btn-dark" name="operation"
					value="<%=EventListCtl.OP_RESET%>">
			</div>

		</div>

		<br>

		<!-- Table -->
		<div class="table-responsive">
			<table class="table table-bordered table-dark table-hover">
				<thead>
					<tr style="background-color: #8C8C8C;">
						<th><input type="checkbox" id="select_all"> Select
							All</th>
						<th class="text">S.NO</th>
						<th class="text">Event Code</th>
						<th class="text">Event Name</th>
						<th class="text">Organizer</th>
						<th class="text">Event Date</th>
						<th class="text">Edit</th>
					</tr>
				</thead>

				<%
				while (it.hasNext()) {
					dto = it.next();
				%>
				<tbody>
					<tr>
						<td align="center"><input type="checkbox" class="checkbox"
							name="ids" value="<%=dto.getId()%>"></td>
						<td class="text"><%=index++%></td>
						<td class="text"><%=dto.getEventCode()%></td>
						<td class="text"><%=dto.getEventName()%></td>
						<td class="text"><%=dto.getOrganizer()%></td>
						<td class="text"><%=DataUtility.getDateString(dto.getEventDate())%></td>
						<td class="text"><a href="EventCtl?id=<%=dto.getId()%>">Edit</a>
						</td>
					</tr>
				</tbody>
				<%
				}
				%>
			</table>
		</div>

		<!-- Pagination -->
		<table width="100%">
			<tr>
				<td><input type="submit" name="operation"
					class="btn btn-warning" value="<%=EventListCtl.OP_PREVIOUS%>"
					<%=pageNo > 1 ? "" : "disabled"%>></td>

				<td><input type="submit" name="operation"
					class="btn btn-primary" value="<%=EventListCtl.OP_NEW%>"></td>

				<td><input type="submit" name="operation"
					class="btn btn-danger" value="<%=EventListCtl.OP_DELETE%>">
				</td>

				<td align="right"><input type="submit" name="operation"
					class="btn btn-warning" value="<%=EventListCtl.OP_NEXT%>"
					<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
			</tr>
		</table>

		<%
		} else {
		%>

		<center>
			<h1 style="font-size: 40px;">Event List</h1>
		</center>

		<div style="text-align: center;">
			<input type="submit" name="operation" class="btn btn-primary"
				value="<%=EventListCtl.OP_BACK%>">
		</div>

		<%
		}
		%>

		<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
			type="hidden" name="pageSize" value="<%=pageSize%>">

	</form>

	<%@include file="FooterView.jsp"%>

</body>
</html>