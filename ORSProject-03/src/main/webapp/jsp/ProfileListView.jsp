<%@page import="in.co.rays.project_3.dto.ProfileDTO"%>
<%@page import="in.co.rays.project_3.controller.ProfileListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<html>
<head>
<title>Profile List</title>

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

	<form action="<%=ORSView.PROFILE_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.ProfileDTO"
			scope="request"></jsp:useBean>

		<%
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize(request);
		int index = ((pageNo - 1) * pageSize) + 1;
		int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

		List list = ServletUtility.getList(request);
		Iterator<ProfileDTO> it = list.iterator();
		%>

		<%
		if (list.size() != 0) {
		%>

		<center>
			<h1 class="text-dark pt-3">
				<u>Profile List</u>
			</h1>
		</center>

		<!-- Search Section -->
		<div class="row justify-content-center mt-3">

			<div class="col-md-3">
				<input type="text" name="fullName" placeholder="Enter Full Name"
					class="form-control"
					value="<%=ServletUtility.getParameter("fullName", request)%>">
			</div>

			<div class="col-md-3">
				<input type="text" name="profileStatus"
					placeholder="Enter Profile Status" class="form-control"
					value="<%=ServletUtility.getParameter("profileStatus", request)%>">
			</div>

			<div class="col-md-3 text-center">
				<input type="submit" class="btn btn-primary" name="operation"
					value="<%=ProfileListCtl.OP_SEARCH%>"> <input type="submit"
					class="btn btn-dark" name="operation"
					value="<%=ProfileListCtl.OP_RESET%>">
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
						<th class="text">Full Name</th>
						<th class="text">Gender</th>
						<th class="text">DOB</th>
						<th class="text">Profile Status</th>
						<th class="text">Edit</th>
					</tr>
				</thead>

				<%
				while (it.hasNext()) {
					dto = it.next();
				%>
				<tbody>
					<tr>
						<td align="center"><input type="checkbox" name="ids"
							value="<%=dto.getId()%>"></td>
						<td class="text"><%=index++%></td>
						<td class="text"><%=dto.getFullName()%></td>
						<td class="text"><%=dto.getGender()%></td>
						<td class="text"><%=DataUtility.getDateString(dto.getDob())%></td>
						<td class="text"><%=dto.getProfileStatus()%></td>
						<td class="text"><a href="ProfileCtl?id=<%=dto.getId()%>">Edit</a>
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
				<td><input type="submit" name="operation"
					class="btn btn-warning" value="<%=ProfileListCtl.OP_PREVIOUS%>"
					<%=pageNo > 1 ? "" : "disabled"%>></td>

				<td><input type="submit" name="operation"
					class="btn btn-primary" value="<%=ProfileListCtl.OP_NEW%>">
				</td>

				<td><input type="submit" name="operation"
					class="btn btn-danger" value="<%=ProfileListCtl.OP_DELETE%>">
				</td>

				<td align="right"><input type="submit" name="operation"
					class="btn btn-warning" value="<%=ProfileListCtl.OP_NEXT%>"
					<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
			</tr>
		</table>

		<%
		} else {
		%>

		<center>
			<h1 style="font-size: 40px;">Profile List</h1>
		</center>

		<div style="text-align: center;">
			<input type="submit" name="operation" class="btn btn-primary"
				value="<%=ProfileListCtl.OP_BACK%>">
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
