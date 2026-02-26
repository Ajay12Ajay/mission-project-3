package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.EventModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/EventCtl" })
public class EventCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EventCtl.class);

	/**
	 * Validation Logic
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		// Event Code
		if (DataValidator.isNull(request.getParameter("eventCode"))) {
			request.setAttribute("eventCode", PropertyReader.getValue("error.require", "eventCode"));
			pass = false;
		}

		// Event Name
		if (DataValidator.isNull(request.getParameter("eventName"))) {
			request.setAttribute("eventName", PropertyReader.getValue("error.require", "eventName"));
			pass = false;
		}

		// Organizer
		if (DataValidator.isNull(request.getParameter("organizer"))) {
			request.setAttribute("organizer", PropertyReader.getValue("error.require", "organizer"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("organizer"))) {
			request.setAttribute("organizer", "Organizer name must contain alphabets only");
			pass = false;
		}

		// Event Date
		if (DataValidator.isNull(request.getParameter("eventDate"))) {
			request.setAttribute("eventDate", PropertyReader.getValue("error.require", "eventDate"));
			pass = false;
		}

		return pass;
	}

	/**
	 * Populate DTO
	 */
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		EventDTO dto = new EventDTO();

		dto.setEventCode(request.getParameter("eventCode"));
		dto.setEventName(request.getParameter("eventName"));
		dto.setOrganizer(request.getParameter("organizer"));
		dto.setEventDate(DataUtility.getDate(request.getParameter("eventDate")));

		populateBean(dto, request);

		return dto;
	}

	/**
	 * Load Data for Edit
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		EventModelInt model = ModelFactory.getInstance().getEventModel();

		if (id > 0 || op != null) {

			EventDTO dto;

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

	/**
	 * Save / Update Logic
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		EventModelInt model = ModelFactory.getInstance().getEventModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			EventDTO dto = (EventDTO) populateDTO(request);

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
				ServletUtility.setErrorMessage("Event Code Already Exists", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EVENT_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.EVENT_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.EVENT_VIEW;
	}
}