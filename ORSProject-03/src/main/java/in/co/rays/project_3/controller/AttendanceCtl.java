package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.AttendanceDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.AttendanceModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/AttendanceCtl" })
public class AttendanceCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AttendanceCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {

			request.setAttribute("name", PropertyReader.getValue("error.require", "name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("name"))) {

			request.setAttribute("name", "Name must contain alphabets only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("attendanceDate"))) {

			request.setAttribute("attendanceDate", PropertyReader.getValue("error.require", "attendanceDate"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("attendanceStatus"))) {

			request.setAttribute("attendanceStatus", PropertyReader.getValue("error.require", "attendanceStatus"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("remarks"))) {

			request.setAttribute("remarks", PropertyReader.getValue("error.require", "remarks"));
			pass = false;
		}

		return pass;
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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");

		long id = DataUtility.getLong(request.getParameter("id"));

		AttendanceModelInt model = ModelFactory.getInstance().getAttendanceModel();

		if (id > 0 || op != null) {

			AttendanceDTO dto;

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

		AttendanceModelInt model = ModelFactory.getInstance().getAttendanceModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			AttendanceDTO dto = (AttendanceDTO) populateDTO(request);

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

				ServletUtility.setErrorMessage("Attendance Name Already Exists", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ATTENDANCE_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ATTENDANCE_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {

		return ORSView.ATTENDANCE_VIEW;
	}
}
