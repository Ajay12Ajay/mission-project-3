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
import in.co.rays.project_3.dto.IssueBookDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.IssueBookModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "IssueBookListCtl", urlPatterns = { "/ctl/IssueBookListCtl" })
public class IssueBookListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IssueBookListCtl.class);

	/**
	 * Preload Method
	 */
	protected void preload(HttpServletRequest request) {

		IssueBookModelInt model =
				ModelFactory.getInstance().getIssueBookModel();

		try {

			List<IssueBookDTO> list = model.list();

			List<String> studentNameList = new ArrayList<>();
			List<String> issueDateList = new ArrayList<>();
			List<String> returnDateList = new ArrayList<>();

			for (IssueBookDTO dto : list) {

				// Student Name
				if (dto.getStudentName() != null &&
						!studentNameList.contains(dto.getStudentName())) {
					studentNameList.add(dto.getStudentName());
				}

				// Issue Date
				if (dto.getIssueDate() != null) {
					String issueDate =
							DataUtility.getString(dto.getIssueDate());

					if (!issueDateList.contains(issueDate)) {
						issueDateList.add(issueDate);
					}
				}

				// Return Date
				if (dto.getReturnDate() != null) {
					String returnDate =
							DataUtility.getString(dto.getReturnDate());

					if (!returnDateList.contains(returnDate)) {
						returnDateList.add(returnDate);
					}
				}
			}

			request.setAttribute("studentNameList", studentNameList);
			request.setAttribute("issueDateList", issueDateList);
			request.setAttribute("returnDateList", returnDateList);

		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		IssueBookDTO dto = new IssueBookDTO();

		dto.setIssueCode(request.getParameter("issueCode"));
		dto.setStudentName(request.getParameter("studentName"));
		dto.setIssueDate(
				DataUtility.getDate(request.getParameter("issueDate")));
		dto.setReturnDate(
				DataUtility.getDate(request.getParameter("returnDate")));

		populateBean(dto, request);

		return dto;
	}

	/**
	 * Display Logic
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("IssueBookListCtl doGet Start");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(
				PropertyReader.getValue("page.size"));

		IssueBookDTO dto =
				(IssueBookDTO) populateDTO(request);

		IssueBookModelInt model =
				ModelFactory.getInstance().getIssueBookModel();

		try {

			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage(
						"No record found ", request);
			}

			request.setAttribute("nextListSize",
					(next == null) ? 0 : next.size());

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {

			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("IssueBookListCtl doGet End");
	}

	/**
	 * Submit Logic
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("IssueBookListCtl doPost Start");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ?
				DataUtility.getInt(PropertyReader.getValue("page.size"))
				: pageSize;

		IssueBookDTO dto =
				(IssueBookDTO) populateDTO(request);

		String op =
				DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		IssueBookModelInt model =
				ModelFactory.getInstance().getIssueBookModel();

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
						ORSView.ISSUEBOOK_CTL,
						request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(
						ORSView.ISSUEBOOK_LIST_CTL,
						request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					IssueBookDTO deleteDto =
							new IssueBookDTO();

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
						ORSView.ISSUEBOOK_LIST_CTL,
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

			request.setAttribute("nextListSize",
					(next == null) ? 0 : next.size());

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

		log.debug("IssueBookListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.ISSUEBOOK_LIST_VIEW;
	}
}