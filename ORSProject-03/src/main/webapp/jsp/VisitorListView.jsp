<%@page import="in.co.rays.project_3.dto.VisitorDTO"%>
<%@page import="in.co.rays.project_3.controller.VisitorListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<html>
<head>
<title>Visitor List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>
<script type="text/javascript"
	src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

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

<form action="<%=ORSView.VISITOR_LIST_CTL%>" method="post">

<jsp:useBean id="dto" class="in.co.rays.project_3.dto.VisitorDTO"
	scope="request"></jsp:useBean>

<%
int pageNo = ServletUtility.getPageNo(request);
int pageSize = ServletUtility.getPageSize(request);
int index = ((pageNo - 1) * pageSize) + 1;
int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

List<String> visitStatusList = (List<String>) request.getAttribute("visitStatusList");

List list = ServletUtility.getList(request);
Iterator<VisitorDTO> it = list.iterator();
%>

<%
if (list.size() != 0) {
%>

<center>
	<h1 class="text-dark pt-3">
		<u>Visitor List</u>
	</h1>
</center>

<!-- Search Section -->
<div class="row justify-content-center mt-3">

	<div class="col-md-3">
		<input type="text" name="visitorName"
			placeholder="Enter Visitor Name"
			class="form-control"
			value="<%=ServletUtility.getParameter("visitorName", request)%>">
	</div>

	<div class="col-md-3">
		<input type="text" name="visitorPurpose"
			placeholder="Enter Purpose"
			class="form-control"
			value="<%=ServletUtility.getParameter("visitorPurpose", request)%>">
	</div>

	<!-- Preload Visit Status Dropdown -->
	<div class="col-md-3">
		<select name="visitStatus" class="form-control">
			<option value="">--Select Status--</option>

			<%
			String selected = ServletUtility.getParameter("visitStatus", request);
			if (visitStatusList != null) {
				for (String s : visitStatusList) {
			%>
			<option value="<%=s%>" <%=s.equals(selected) ? "selected" : ""%>>
				<%=s%>
			</option>
			<%
			}
			}
			%>
		</select>
	</div>

	<div class="col-md-3 text-center mt-2">
		<input type="submit" class="btn btn-primary"
			name="operation"
			value="<%=VisitorListCtl.OP_SEARCH%>">

		<input type="submit" class="btn btn-dark"
			name="operation"
			value="<%=VisitorListCtl.OP_RESET%>">
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
		<th class="text">Pass Code</th>
		<th class="text">Visitor Name</th>
		<th class="text">Purpose</th>
		<th class="text">Visit Date</th>
		<th class="text">Status</th>
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
			<input type="checkbox" class="checkbox" name="ids"
				value="<%=dto.getId()%>">
		</td>
		<td class="text"><%=index++%></td>
		<td class="text"><%=dto.getVisitorPassCode()%></td>
		<td class="text"><%=dto.getVisitorName()%></td>
		<td class="text"><%=dto.getVisitorPurpose()%></td>
		<td class="text"><%=DataUtility.getDateString(dto.getVisitDate())%></td>
		<td class="text"><%=dto.getVisitStatus()%></td>
		<td class="text">
			<a href="VisitorCtl?id=<%=dto.getId()%>">Edit</a>
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
<td>
	<input type="submit" name="operation"
		class="btn btn-warning"
		value="<%=VisitorListCtl.OP_PREVIOUS%>"
		<%=pageNo > 1 ? "" : "disabled"%>>
</td>

<td>
	<input type="submit" name="operation"
		class="btn btn-primary"
		value="<%=VisitorListCtl.OP_NEW%>">
</td>

<td>
	<input type="submit" name="operation"
		class="btn btn-danger"
		value="<%=VisitorListCtl.OP_DELETE%>">
</td>

<td align="right">
	<input type="submit" name="operation"
		class="btn btn-warning"
		value="<%=VisitorListCtl.OP_NEXT%>"
		<%=(nextPageSize != 0) ? "" : "disabled"%>>
</td>
</tr>
</table>

<%
} else {
%>

<center>
	<h1 style="font-size: 40px;">Visitor List</h1>
</center>

<div style="text-align: center;">
	<input type="submit" name="operation"
		class="btn btn-primary"
		value="<%=VisitorListCtl.OP_BACK%>">
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