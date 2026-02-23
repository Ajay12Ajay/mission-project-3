package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.PaymentDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PaymentModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "PaymentListCtl", urlPatterns = { "/ctl/PaymentListCtl" })
public class PaymentListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PaymentListCtl.class);

	/**
	 * 🔥 Preload Method
	 */
	/*
	 * protected void preload(HttpServletRequest request) {
	 * 
	 * try {
	 * 
	 * // ✅ Static Payment Method List List<String> methodList = new ArrayList<>();
	 * methodList.add("Cash"); methodList.add("Card"); methodList.add("UPI");
	 * methodList.add("Net Banking");
	 * 
	 * request.setAttribute("paymentMethodList", methodList);
	 * 
	 * // ✅ Static Payment Status List List<String> statusList = new ArrayList<>();
	 * statusList.add("Completed"); statusList.add("Pending");
	 * statusList.add("Failed");
	 * 
	 * request.setAttribute("paymentStatusList", statusList);
	 * 
	 * } catch (Exception e) { log.error(e); } }
	 */

	@Override
	protected void preload(HttpServletRequest request) {

		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();

		try {

			List<PaymentDTO> list = model.list();

			List<String> methodList = new ArrayList<>();
			List<String> statusList = new ArrayList<>();

			for (PaymentDTO dto : list) {

				// Dynamic Payment Method
				if (dto.getPaymentMethod() != null && !methodList.contains(dto.getPaymentMethod())) {

					methodList.add(dto.getPaymentMethod());
				}

				// Dynamic Payment Status
				if (dto.getPaymentStatus() != null && !statusList.contains(dto.getPaymentStatus())) {

					statusList.add(dto.getPaymentStatus());
				}
			}

			request.setAttribute("paymentMethodList", methodList);
			request.setAttribute("paymentStatusList", statusList);

		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		PaymentDTO dto = new PaymentDTO();

		dto.setPaymentCode(request.getParameter("paymentCode"));
		dto.setPaymentMethod(request.getParameter("paymentMethod"));
		dto.setPaymentStatus(request.getParameter("paymentStatus"));
		dto.setPaymentDate(DataUtility.getDate(request.getParameter("paymentDate")));

		populateBean(dto, request);

		return dto;
	}

	/**
	 * Display Logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("PaymentListCtl doGet Start");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		PaymentDTO dto = (PaymentDTO) populateDTO(request);

		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();

		try {

			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}

			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);
			} else {
				request.setAttribute("nextListSize", next.size());
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {

			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("PaymentListCtl doGet End");
	}

	/**
	 * Submit Logic
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("PaymentListCtl doPost Start");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		PaymentDTO dto = (PaymentDTO) populateDTO(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		PaymentModelInt model = ModelFactory.getInstance().getPaymentModel();

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;

				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;

				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.PAYMENT_CTL, request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.PAYMENT_LIST_CTL, request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					PaymentDTO deleteDto = new PaymentDTO();

					for (String id : ids) {
						deleteDto.setId(DataUtility.getLong(id));
						model.delete(deleteDto);
					}

					ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);

				} else {

					ServletUtility.setErrorMessage("Select atleast one record", request);
				}
			}

			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.PAYMENT_LIST_CTL, request, response);
				return;
			}

			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				if (!OP_DELETE.equalsIgnoreCase(op)) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}

			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);
			} else {
				request.setAttribute("nextListSize", next.size());
			}

			ServletUtility.setDto(dto, request);
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {

			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("PaymentListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.PAYMENT_LIST_VIEW;
	}
}