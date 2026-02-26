package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.IssueBookDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.IssueBookModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/IssueBookCtl" })
public class IssueBookCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(IssueBookCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("issueCode"))) {
			request.setAttribute("issueCode",
					PropertyReader.getValue("error.require", "issueCode"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("studentName"))) {
			request.setAttribute("studentName",
					PropertyReader.getValue("error.require", "studentName"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("studentName"))) {
			request.setAttribute("studentName",
					"Student Name must contain alphabets only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("issueDate"))) {
			request.setAttribute("issueDate",
					PropertyReader.getValue("error.require", "issueDate"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("returnDate"))) {
			request.setAttribute("returnDate",
					PropertyReader.getValue("error.require", "returnDate"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		IssueBookDTO dto = new IssueBookDTO();

		dto.setIssueCode(request.getParameter("issueCode"));
		dto.setStudentName(request.getParameter("studentName"));
		dto.setIssueDate(DataUtility.getDate(request.getParameter("issueDate")));
		dto.setReturnDate(DataUtility.getDate(request.getParameter("returnDate")));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		IssueBookModelInt model =
				ModelFactory.getInstance().getIssueBookModel();

		if (id > 0 || op != null) {

			IssueBookDTO dto;

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

		IssueBookModelInt model =
				ModelFactory.getInstance().getIssueBookModel();

		if (OP_SAVE.equalsIgnoreCase(op)
				|| OP_UPDATE.equalsIgnoreCase(op)) {

			IssueBookDTO dto = (IssueBookDTO) populateDTO(request);

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
						"Issue Code Already Exists", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ISSUEBOOK_CTL,
					request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ISSUEBOOK_LIST_CTL,
					request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.ISSUEBOOK_VIEW;
	}
}