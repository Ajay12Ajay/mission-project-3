<%@page import="in.co.rays.project_3.dto.CertificateDTO"%>
<%@page import="in.co.rays.project_3.controller.CertificateListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<html>
<head>
<title>Certificate List</title>

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

	<form action="<%=ORSView.CERTIFICATE_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.CertificateDTO"
			scope="request"></jsp:useBean>

		<%
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize(request);
		int index = ((pageNo - 1) * pageSize) + 1;
		int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

		List<String> issueToList = (List<String>) request.getAttribute("issueToList");

		List list = ServletUtility.getList(request);
		Iterator<CertificateDTO> it = list.iterator();
		%>

		<%
		if (list.size() != 0) {
		%>

		<center>
			<h1 class="text-dark pt-3">
				<u>Certificate List</u>
			</h1>
		</center>

		<!-- Search Section -->
		<div class="row justify-content-center mt-3">

			<div class="col-md-3">
				<input type="text" name="certificateName"
					placeholder="Enter Certificate Name" class="form-control"
					value="<%=ServletUtility.getParameter("certificateName", request)%>">
			</div>

			<!--  Preload IssueTo Dropdown -->
			<div class="col-md-3">
				<select name="issueTo" class="form-control">
					<option value="">--Select Issue To--</option>

					<%
					String selected = ServletUtility.getParameter("issueTo", request);
					if (issueToList != null) {
						for (String s : issueToList) {
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

			<div class="col-md-3 text-center">
				<input type="submit" class="btn btn-primary" name="operation"
					value="<%=CertificateListCtl.OP_SEARCH%>"> <input
					type="submit" class="btn btn-dark" name="operation"
					value="<%=CertificateListCtl.OP_RESET%>">
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
						<th class="text">Certificate Name</th>
						<th class="text">Issue To</th>
						<th class="text">Issue Date</th>
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
						<td align="center"><input type="checkbox" name="ids"
							value="<%=dto.getId()%>"></td>
						<td class="text"><%=index++%></td>
						<td class="text"><%=dto.getCertificateName()%></td>
						<td class="text"><%=dto.getIssueTo()%></td>
						<td class="text"><%=DataUtility.getDateString(dto.getIssueDate())%>
						</td>
						<td class="text"><%=dto.getCertificateStatus()%></td>
						<td class="text"><a href="CertificateCtl?id=<%=dto.getId()%>">Edit</a>
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
					class="btn btn-warning" value="<%=CertificateListCtl.OP_PREVIOUS%>"
					<%=pageNo > 1 ? "" : "disabled"%>></td>

				<td><input type="submit" name="operation"
					class="btn btn-primary" value="<%=CertificateListCtl.OP_NEW%>">
				</td>

				<td><input type="submit" name="operation"
					class="btn btn-danger" value="<%=CertificateListCtl.OP_DELETE%>">
				</td>

				<td align="right"><input type="submit" name="operation"
					class="btn btn-warning" value="<%=CertificateListCtl.OP_NEXT%>"
					<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
			</tr>
		</table>

		<%
		} else {
		%>

		<center>
			<h1 style="font-size: 40px;">Certificate List</h1>
		</center>

		<div style="text-align: center;">
			<input type="submit" name="operation" class="btn btn-primary"
				value="<%=CertificateListCtl.OP_BACK%>">
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