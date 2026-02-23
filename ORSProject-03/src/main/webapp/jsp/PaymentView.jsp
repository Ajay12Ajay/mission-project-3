<%@page import="in.co.rays.project_3.controller.PaymentCtl"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>Payment View</title>
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
	<form action="<%=ORSView.PAYMENT_CTL%>" method="post">

		<jsp:useBean id="dto"
			class="in.co.rays.project_3.dto.PaymentDTO"
			scope="request"></jsp:useBean>

		<div class="row pt-3 pb-4">
			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
						<h3 class="text-center text-primary">Update Payment</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Payment</h3>
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

						<!-- Payment Code -->
						<b>Payment Code</b><span style="color:red">*</span><br>
						<input type="text" name="paymentCode"
							class="form-control"
							placeholder="Enter Payment Code"
							value="<%=DataUtility.getStringData(dto.getPaymentCode())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("paymentCode", request)%>
						</font><br><br>

						<!-- Amount -->
						<b>Amount</b><span style="color:red">*</span><br>
						<input type="number" step="0.01" name="amount"
							class="form-control"
							placeholder="Enter Amount"
							value="<%=dto.getAmount() != null ? dto.getAmount() : ""%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("amount", request)%>
						</font><br><br>

						<!-- Payment Date -->
						<b>Payment Date</b><span style="color:red">*</span><br>
						<input type="text" id="datepicker3"
							name="paymentDate"
							class="form-control"
							readonly="readonly"
							value="<%=DataUtility.getDateString(dto.getPaymentDate())%>">
						<font color="red">
							<%=ServletUtility.getErrorMessage("paymentDate", request)%>
						</font><br><br>

						<!-- Payment Method (STATIC PRELOAD) -->
						<b>Payment Method</b><span style="color:red">*</span><br>
						<select name="paymentMethod" class="form-control">
							<option value="">--Select Method--</option>

							<option value="Cash"
								<%= "Cash".equals(dto.getPaymentMethod()) ? "selected" : "" %>>
								Cash
							</option>

							<option value="UPI"
								<%= "UPI".equals(dto.getPaymentMethod()) ? "selected" : "" %>>
								UPI
							</option>

							<option value="Card"
								<%= "Card".equals(dto.getPaymentMethod()) ? "selected" : "" %>>
								Card
							</option>

							<option value="Net Banking"
								<%= "Net Banking".equals(dto.getPaymentMethod()) ? "selected" : "" %>>
								Net Banking
							</option>

						</select>
						<font color="red">
							<%=ServletUtility.getErrorMessage("paymentMethod", request)%>
						</font><br><br>

						<!-- Payment Status (STATIC PRELOAD) -->
						<b>Payment Status</b><span style="color:red">*</span><br>
						<select name="paymentStatus" class="form-control">
							<option value="">--Select Status--</option>

							<option value="Pending"
								<%= "Pending".equals(dto.getPaymentStatus()) ? "selected" : "" %>>
								Pending
							</option>

							<option value="Completed"
								<%= "Completed".equals(dto.getPaymentStatus()) ? "selected" : "" %>>
								Completed
							</option>

							<option value="Failed"
								<%= "Failed".equals(dto.getPaymentStatus()) ? "selected" : "" %>>
								Failed
							</option>

						</select>
						<font color="red">
							<%=ServletUtility.getErrorMessage("paymentStatus", request)%>
						</font><br><br>

						<!-- Buttons -->
						<div class="text-center">
						<%
							if (dto.getId() != null && dto.getId() > 0) {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=PaymentCtl.OP_UPDATE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=PaymentCtl.OP_CANCEL%>">
						<%
							} else {
						%>
							<input type="submit" name="operation"
								class="btn btn-success"
								value="<%=PaymentCtl.OP_SAVE%>">

							<input type="submit" name="operation"
								class="btn btn-warning"
								value="<%=PaymentCtl.OP_RESET%>">
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