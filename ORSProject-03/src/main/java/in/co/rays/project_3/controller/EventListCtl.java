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
import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.EventModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "EventListCtl", urlPatterns = { "/ctl/EventListCtl" })
public class EventListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EventListCtl.class);

	/**
	 * 🔥 Dynamic Preload Method
	 */
	protected void preload(HttpServletRequest request) {

		EventModelInt model = ModelFactory.getInstance().getEventModel();

		try {

			List<EventDTO> list = model.list();

			List<String> eventNameList = new ArrayList<>();
			List<String> organizerList = new ArrayList<>();
			List<String> eventDateList = new ArrayList<>();

			for (EventDTO dto : list) {

				// Event Name
				if (dto.getEventName() != null &&
					!eventNameList.contains(dto.getEventName())) {
					eventNameList.add(dto.getEventName());
				}

				// Organizer
				if (dto.getOrganizer() != null &&
					!organizerList.contains(dto.getOrganizer())) {
					organizerList.add(dto.getOrganizer());
				}

				// Event Date
				if (dto.getEventDate() != null) {

					String date =
						DataUtility.getDateString(dto.getEventDate());

					if (!eventDateList.contains(date)) {
						eventDateList.add(date);
					}
				}
			}

			request.setAttribute("eventNameList", eventNameList);
			request.setAttribute("organizerList", organizerList);
			request.setAttribute("eventDateList", eventDateList);

		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		EventDTO dto = new EventDTO();

		dto.setEventCode(request.getParameter("eventCode"));
		dto.setEventName(request.getParameter("eventName"));
		dto.setOrganizer(request.getParameter("organizer"));
		dto.setEventDate(
			DataUtility.getDate(request.getParameter("eventDate")));

		populateBean(dto, request);

		return dto;
	}

	/**
	 * Display Logic
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("EventListCtl doGet Start");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(
			PropertyReader.getValue("page.size"));

		EventDTO dto = (EventDTO) populateDTO(request);

		EventModelInt model =
			ModelFactory.getInstance().getEventModel();

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

		log.debug("EventListCtl doGet End");
	}

	/**
	 * Submit Logic
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("EventListCtl doPost Start");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ?
			DataUtility.getInt(PropertyReader.getValue("page.size"))
			: pageSize;

		EventDTO dto = (EventDTO) populateDTO(request);

		String op = DataUtility.getString(
			request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		EventModelInt model =
			ModelFactory.getInstance().getEventModel();

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
					ORSView.EVENT_CTL, request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(
					ORSView.EVENT_LIST_CTL,
					request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					EventDTO deleteDto = new EventDTO();

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
					ORSView.EVENT_LIST_CTL,
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

		log.debug("EventListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.EVENT_LIST_VIEW;
	}
}