package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.PaymentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PaymentModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/PaymentCtl" })
public class PaymentCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PaymentCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		// Payment Code (Required)
		if (DataValidator.isNull(request.getParameter("paymentCode"))) {
			request.setAttribute("paymentCode", PropertyReader.getValue("error.require", "paymentCode"));
			pass = false;
		}

		// Amount (Required)
		if (DataValidator.isNull(request.getParameter("amount"))) {
			request.setAttribute("amount", PropertyReader.getValue("error.require", "amount"));
			pass = false;
		}

		// Payment Method (Required)
		if (DataValidator.isNull(request.getParameter("paymentMethod"))) {
			request.setAttribute("paymentMethod", PropertyReader.getValue("error.require", "paymentMethod"));
			pass = false;
		}

		// Payment Date (Required)
		if (DataValidator.isNull(request.getParameter("paymentDate"))) {
			request.setAttribute("paymentDate", PropertyReader.getValue("error.require", "paymentDate"));
			pass = false;
		}

		// Payment Status (Required)
		if (DataValidator.isNull(request.getParameter("paymentStatus"))) {
			request.setAttribute("paymentStatus", PropertyReader.getValue("error.require", "paymentStatus"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		PaymentDTO dto = new PaymentDTO();

		dto.setPaymentCode(request.getParameter("paymentCode"));
		dto.setAmount(DataUtility.getDouble(request.getParameter("amount")));
		dto.setPaymentMethod(request.getParameter("paymentMethod"));
		dto.setPaymentDate(DataUtility.getDate(request.getParameter("paymentDate")));
		dto.setPaymentStatus(request.getParameter("paymentStatus"));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();

		if (id > 0 || op != null) {

			PaymentDTO dto;

			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {

				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			PaymentDTO dto = (PaymentDTO) populateDTO(request);

			try {

				if (id > 0) {

					dto.setId(id);
					model.update(dto);
					ServletUtility.setDto(dto, request);
					ServletUtility.setSuccessMessage("Record Successfully Updated", request);

				} else {

					model.add(dto);
					ServletUtility.setSuccessMessage("Record Successfully Saved", request);
				}

			} catch (ApplicationException e) {

				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;

			} catch (DuplicateRecordException e) {

				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Payment Code Already Exists", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.PAYMENT_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.PAYMENT_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.PAYMENT_VIEW;
	}
}