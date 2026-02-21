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
import in.co.rays.project_3.dto.VisitorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.VisitorModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "VisitorListCtl", urlPatterns = { "/ctl/VisitorListCtl" })
public class VisitorListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(VisitorListCtl.class);

	/**
	 * 🔥 Preload Method
	 */
	protected void preload(HttpServletRequest request) {

		VisitorModelInt model = ModelFactory.getInstance().getVisitorModel();

		try {

			// ✅ Static Status List
			List<String> statusList = new ArrayList<>();
			statusList.add("Approved");
			statusList.add("Pending");
			statusList.add("Rejected");

			request.setAttribute("visitStatusList", statusList);

			// ✅ Dynamic Purpose List (Good for filtering)
			List<VisitorDTO> list = model.list();
			List<String> purposeList = new ArrayList<>();

			for (VisitorDTO dto : list) {
				if (dto.getVisitorPurpose() != null &&
					!purposeList.contains(dto.getVisitorPurpose())) {
					purposeList.add(dto.getVisitorPurpose());
				}
			}

			request.setAttribute("visitorPurposeList", purposeList);

		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		VisitorDTO dto = new VisitorDTO();

		dto.setVisitorPassCode(request.getParameter("visitorPassCode"));
		dto.setVisitorName(request.getParameter("visitorName"));
		dto.setVisitorPurpose(request.getParameter("visitorPurpose"));
		dto.setVisitDate(DataUtility.getDate(request.getParameter("visitDate")));
		dto.setVisitStatus(request.getParameter("visitStatus"));

		populateBean(dto, request);

		return dto;
	}

	/**
	 * Display Logic
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("VisitorListCtl doGet Start");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(
				PropertyReader.getValue("page.size"));

		VisitorDTO dto = (VisitorDTO) populateDTO(request);

		VisitorModelInt model =
				ModelFactory.getInstance().getVisitorModel();

		try {

			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage(
						"No record found ", request);
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

		log.debug("VisitorListCtl doGet End");
	}

	/**
	 * Submit Logic
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("VisitorListCtl doPost Start");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ?
				DataUtility.getInt(PropertyReader.getValue("page.size"))
				: pageSize;

		VisitorDTO dto = (VisitorDTO) populateDTO(request);

		String op = DataUtility.getString(
				request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		VisitorModelInt model =
				ModelFactory.getInstance().getVisitorModel();

		try {

			if (OP_SEARCH.equalsIgnoreCase(op)
					|| OP_NEXT.equalsIgnoreCase(op)
					|| OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;

				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;

				} else if (OP_PREVIOUS.equalsIgnoreCase(op)
						&& pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {

				ServletUtility.redirect(
						ORSView.VISITOR_CTL, request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(
						ORSView.VISITOR_LIST_CTL,
						request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					VisitorDTO deleteDto = new VisitorDTO();

					for (String id : ids) {
						deleteDto.setId(
							DataUtility.getLong(id));
						model.delete(deleteDto);
					}

					ServletUtility.setSuccessMessage(
							"Data Successfully Deleted!",
							request);

				} else {
					ServletUtility.setErrorMessage(
							"Select atleast one record",
							request);
				}
			}

			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(
						ORSView.VISITOR_LIST_CTL,
						request, response);
				return;
			}

			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				if (!OP_DELETE.equalsIgnoreCase(op)) {
					ServletUtility.setErrorMessage(
							"No record found ", request);
				}
			}

			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);
			} else {
				request.setAttribute("nextListSize",
						next.size());
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

		log.debug("VisitorListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.VISITOR_LIST_VIEW;
	}
}