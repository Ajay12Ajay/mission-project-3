<%@page import="in.co.rays.project_3.dto.IssueBookDTO"%>
<%@page import="in.co.rays.project_3.controller.IssueBookListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<html>
<head>
<title>Issue Book List</title>

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

	<form action="<%=ORSView.ISSUEBOOK_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.IssueBookDTO"
			scope="request"></jsp:useBean>

		<%
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize(request);
		int index = ((pageNo - 1) * pageSize) + 1;
		int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

		List<String> studentNameList = (List<String>) request.getAttribute("studentNameList");
		List<String> issueDateList = (List<String>) request.getAttribute("issueDateList");
		List<String> returnDateList = (List<String>) request.getAttribute("returnDateList");

		List list = ServletUtility.getList(request);
		Iterator<IssueBookDTO> it = list.iterator();
		%>

		<%
		if (list.size() != 0) {
		%>

		<center>
			<h1 class="text-dark pt-3">
				<u>Issue Book List</u>
			</h1>
		</center>

		<!-- Search Section -->
		<div class="row justify-content-center mt-3">

			<!-- Student Name Dropdown -->
			<div class="col-md-3">
				<select name="studentName" class="form-control">
					<option value="">--Select Student--</option>
					<%
					String selectedStudent = ServletUtility.getParameter("studentName", request);
					if (studentNameList != null) {
						for (String s : studentNameList) {
					%>
					<option value="<%=s%>"
						<%=s.equals(selectedStudent) ? "selected" : ""%>>
						<%=s%>
					</option>
					<%
					}
					}
					%>
				</select>
			</div>

			<!-- Issue Date Dropdown -->
			<div class="col-md-3">
				<select name="issueDate" class="form-control">
					<option value="">--Select Issue Date--</option>
					<%
					String selectedIssueDate = ServletUtility.getParameter("issueDate", request);
					if (issueDateList != null) {
						for (String d : issueDateList) {
					%>
					<option value="<%=d%>"
						<%=d.equals(selectedIssueDate) ? "selected" : ""%>>
						<%=d%>
					</option>
					<%
					}
					}
					%>
				</select>
			</div>

			<!-- Return Date Dropdown -->
			<div class="col-md-3">
				<select name="returnDate" class="form-control">
					<option value="">--Select Return Date--</option>
					<%
					String selectedReturnDate = ServletUtility.getParameter("returnDate", request);
					if (returnDateList != null) {
						for (String d : returnDateList) {
					%>
					<option value="<%=d%>"
						<%=d.equals(selectedReturnDate) ? "selected" : ""%>>
						<%=d%>
					</option>
					<%
					}
					}
					%>
				</select>
			</div>

			<div class="col-md-3 text-center mt-2">
				<input type="submit" class="btn btn-primary" name="operation"
					value="<%=IssueBookListCtl.OP_SEARCH%>"> <input
					type="submit" class="btn btn-dark" name="operation"
					value="<%=IssueBookListCtl.OP_RESET%>">
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
						<th class="text">Issue Code</th>
						<th class="text">Student Name</th>
						<th class="text">Issue Date</th>
						<th class="text">Return Date</th>
						<th class="text">Edit</th>
					</tr>
				</thead>

				<tbody>
					<%
					while (it.hasNext()) {
						dto = it.next();
					%>
					<tr>
						<td align="center"><input type="checkbox" class="checkbox"
							name="ids" value="<%=dto.getId()%>"></td>
						<td class="text"><%=index++%></td>
						<td class="text"><%=dto.getIssueCode()%></td>
						<td class="text"><%=dto.getStudentName()%></td>
						<td class="text"><%=DataUtility.getDateString(dto.getIssueDate())%></td>
						<td class="text"><%=DataUtility.getDateString(dto.getReturnDate())%></td>
						<td class="text"><a href="IssueBookCtl?id=<%=dto.getId()%>">Edit</a>
						</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
		</div>

		<!-- Pagination -->
		<table width="100%">
			<tr>
				<td><input type="submit" name="operation"
					class="btn btn-warning" value="<%=IssueBookListCtl.OP_PREVIOUS%>"
					<%=pageNo > 1 ? "" : "disabled"%>></td>

				<td><input type="submit" name="operation"
					class="btn btn-primary" value="<%=IssueBookListCtl.OP_NEW%>">
				</td>

				<td><input type="submit" name="operation"
					class="btn btn-danger" value="<%=IssueBookListCtl.OP_DELETE%>">
				</td>

				<td align="right"><input type="submit" name="operation"
					class="btn btn-warning" value="<%=IssueBookListCtl.OP_NEXT%>"
					<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
			</tr>
		</table>

		<%
		} else {
		%>

		<center>
			<h1 style="font-size: 40px;">Issue Book List</h1>
		</center>

		<div style="text-align: center;">
			<input type="submit" name="operation" class="btn btn-primary"
				value="<%=IssueBookListCtl.OP_BACK%>">
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