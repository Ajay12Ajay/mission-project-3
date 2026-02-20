package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CertificateDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.CertificateModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/CertificateCtl" })
public class CertificateCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CertificateCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("certificateName"))) {
			request.setAttribute("certificateName", PropertyReader.getValue("error.require", "certificateName"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("issueTo"))) {
			request.setAttribute("issueTo", PropertyReader.getValue("error.require", "issueTo"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("issueTo"))) {
			request.setAttribute("issueTo", "Issue To must contain alphabets only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("issueDate"))) {
			request.setAttribute("issueDate", PropertyReader.getValue("error.require", "issueDate"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("certificateStatus"))) {
			request.setAttribute("certificateStatus", PropertyReader.getValue("error.require", "certificateStatus"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		CertificateDTO dto = new CertificateDTO();

		dto.setCertificateName(request.getParameter("certificateName"));
		dto.setIssueTo(request.getParameter("issueTo"));
		dto.setIssueDate(DataUtility.getDate(request.getParameter("issueDate")));
		dto.setCertificateStatus(request.getParameter("certificateStatus"));

		populateBean(dto, request);

		return dto;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		CertificateModelInt model = ModelFactory.getInstance().getCertificateModel();

		if (id > 0 || op != null) {

			CertificateDTO dto;

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

		CertificateModelInt model = ModelFactory.getInstance().getCertificateModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			CertificateDTO dto = (CertificateDTO) populateDTO(request);

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
				ServletUtility.setErrorMessage("Certificate Name Already Exists", request);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CERTIFICATE_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CERTIFICATE_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		return ORSView.CERTIFICATE_VIEW;
	}
}