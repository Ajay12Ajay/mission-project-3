package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.AttendanceDTO;
import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.AttendanceModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "AttendanceListCtl", urlPatterns = { "/ctl/AttendanceListCtl" })
public class AttendanceListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AttendanceListCtl.class);

	/*
	 * protected void preload(HttpServletRequest request) {
	 * 
	 * AttendanceModelInt model = ModelFactory.getInstance().getAttendanceModel();
	 * 
	 * try { List list = model.list(); request.setAttribute("attendanceList", list);
	 * 
	 * } catch (Exception e) { log.error(e); } }
	 */
	
	@Override
	protected void preload(HttpServletRequest request) {

	    AttendanceModelInt model = ModelFactory.getInstance().getAttendanceModel();

	    try {

	        // Existing attendance list (if you want it)
	        List list = model.list();
	        request.setAttribute("attendanceList", list);

	        // 🔹 Static Status Preload
	        List<String> statusList = new java.util.ArrayList<String>();

	        statusList.add("Present");
	        statusList.add("Absent");

	        request.setAttribute("statusList", statusList);

	    } catch (Exception e) {
	        log.error(e);
	    }
	}


	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		AttendanceDTO dto = new AttendanceDTO();

		dto.setName(request.getParameter("name"));
		dto.setAttendanceDate(DataUtility.getDate(request.getParameter("attendanceDate")));
		dto.setAttendanceStatus(request.getParameter("attendanceStatus"));
		dto.setRemarks(request.getParameter("remarks"));

		populateBean(dto, request);

		return dto;
	}

	/**
	 * Display Logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("AttendanceListCtl doGet Start");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		AttendanceDTO dto = (AttendanceDTO) populateDTO(request);

		AttendanceModelInt model = ModelFactory.getInstance().getAttendanceModel();

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

		log.debug("AttendanceListCtl doGet End");
	}

	/**
	 * Submit Logic
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("AttendanceListCtl doPost Start");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));

		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;

		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		AttendanceDTO dto = (AttendanceDTO) populateDTO(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		AttendanceModelInt model = ModelFactory.getInstance().getAttendanceModel();

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

				ServletUtility.redirect(ORSView.ATTENDANCE_CTL, request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.ATTENDANCE_LIST_CTL, request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					AttendanceDTO deleteDto = new AttendanceDTO();

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

				ServletUtility.redirect(ORSView.ATTENDANCE_LIST_CTL, request, response);
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

		log.debug("AttendanceListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.ATTENDANCE_LIST_VIEW;
	}
}
