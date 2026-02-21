package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.VisitorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.VisitorModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/VisitorCtl" })
public class VisitorCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(VisitorCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("visitorPassCode"))) {
			request.setAttribute("visitorPassCode",
					PropertyReader.getValue("error.require", "visitorPassCode"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("visitorName"))) {
			request.setAttribute("visitorName",
					PropertyReader.getValue("error.require", "visitorName"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("visitorName"))) {
			request.setAttribute("visitorName",
					"Visitor Name must contain alphabets only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("visitorPurpose"))) {
			request.setAttribute("visitorPurpose",
					PropertyReader.getValue("error.require", "visitorPurpose"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("visitDate"))) {
			request.setAttribute("visitDate",
					PropertyReader.getValue("error.require", "visitDate"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("visitStatus"))) {
			request.setAttribute("visitStatus",
					PropertyReader.getValue("error.require", "visitStatus"));
			pass = false;
		}

		return pass;
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

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		VisitorModelInt model = ModelFactory.getInstance().getVisitorModel();

		if (id > 0 || op != null) {

			VisitorDTO dto;

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
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		VisitorModelInt model = ModelFactory.getInstance().getVisitorModel();

		if (OP_SAVE.equalsIgnoreCase(op)
				|| OP_UPDATE.equalsIgnoreCase(op)) {

			VisitorDTO dto = (VisitorDTO) populateDTO(request);

			try {

				if (id > 0) {

					dto.setId(id);
					model.update(dto);
					ServletUtility.setDto(dto, request);
					ServletUtility.setSuccessMessage(
							"Record Successfully Updated", request);

				} else {

					model.add(dto);
					ServletUtility.setSuccessMessage(
							"Record Successfully Saved", request);
				}

			} catch (ApplicationException e) {

				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;

			} catch (DuplicateRecordException e) {

				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage(
						"Visitor Pass Code Already Exists", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.VISITOR_CTL,
					request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.VISITOR_LIST_CTL,
					request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.VISITOR_VIEW;
	}
}