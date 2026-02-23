<%@page import="in.co.rays.project_3.dto.PaymentDTO"%>
<%@page import="in.co.rays.project_3.controller.PaymentListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>

<html>
<head>
<title>Payment List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>

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

	<form action="<%=ORSView.PAYMENT_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.PaymentDTO"
			scope="request"></jsp:useBean>

		<%
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize(request);
		int index = ((pageNo - 1) * pageSize) + 1;
		int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

		List<String> paymentMethodList = (List<String>) request.getAttribute("paymentMethodList");

		List<String> paymentStatusList = (List<String>) request.getAttribute("paymentStatusList");

		List list = ServletUtility.getList(request);
		Iterator<PaymentDTO> it = list.iterator();
		%>

		<%
		if (list.size() != 0) {
		%>

		<center>
			<h1 class="text-dark pt-3">
				<u>Payment List</u>
			</h1>
		</center>

		<!-- Search Section -->
		<div class="row justify-content-center mt-3">

			<div class="col-md-3">
				<input type="text" name="paymentCode"
					placeholder="Enter Payment Code" class="form-control"
					value="<%=ServletUtility.getParameter("paymentCode", request)%>">
			</div>

			<!-- Dynamic Payment Method -->
			<div class="col-md-3">
				<select name="paymentMethod" class="form-control">
					<option value="">--Select Method--</option>

					<%
					String selectedMethod = ServletUtility.getParameter("paymentMethod", request);

					if (paymentMethodList != null) {
						for (String s : paymentMethodList) {
					%>
					<option value="<%=s%>"
						<%=s.equals(selectedMethod) ? "selected" : ""%>>
						<%=s%>
					</option>
					<%
					}
					}
					%>
				</select>
			</div>

			<!-- Dynamic Payment Status -->
			<div class="col-md-3">
				<select name="paymentStatus" class="form-control">
					<option value="">--Select Status--</option>

					<%
					String selectedStatus = ServletUtility.getParameter("paymentStatus", request);

					if (paymentStatusList != null) {
						for (String s : paymentStatusList) {
					%>
					<option value="<%=s%>"
						<%=s.equals(selectedStatus) ? "selected" : ""%>>
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
					value="<%=PaymentListCtl.OP_SEARCH%>"> <input type="submit"
					class="btn btn-dark" name="operation"
					value="<%=PaymentListCtl.OP_RESET%>">
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
						<th class="text">Payment Code</th>
						<th class="text">Amount</th>
						<th class="text">Payment Method</th>
						<th class="text">Payment Date</th>
						<th class="text">Payment Status</th>
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
						<td class="text"><%=dto.getPaymentCode()%></td>
						<td class="text"><%=dto.getAmount()%></td>
						<td class="text"><%=dto.getPaymentMethod()%></td>
						<td class="text"><%=DataUtility.getDateString(dto.getPaymentDate())%>
						</td>
						<td class="text"><%=dto.getPaymentStatus()%></td>
						<td class="text"><a href="PaymentCtl?id=<%=dto.getId()%>">Edit</a>
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
					class="btn btn-warning" value="<%=PaymentListCtl.OP_PREVIOUS%>"
					<%=pageNo > 1 ? "" : "disabled"%>></td>

				<td><input type="submit" name="operation"
					class="btn btn-primary" value="<%=PaymentListCtl.OP_NEW%>">
				</td>

				<td><input type="submit" name="operation"
					class="btn btn-danger" value="<%=PaymentListCtl.OP_DELETE%>">
				</td>

				<td align="right"><input type="submit" name="operation"
					class="btn btn-warning" value="<%=PaymentListCtl.OP_NEXT%>"
					<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
			</tr>
		</table>

		<%
		} else {
		%>

		<center>
			<h1 style="font-size: 40px;">Payment List</h1>
		</center>

		<div style="text-align: center;">
			<input type="submit" name="operation" class="btn btn-primary"
				value="<%=PaymentListCtl.OP_BACK%>">
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