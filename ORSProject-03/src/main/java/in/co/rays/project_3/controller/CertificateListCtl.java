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
import in.co.rays.project_3.dto.CertificateDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.CertificateModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "CertificateListCtl", urlPatterns = { "/ctl/CertificateListCtl" })
public class CertificateListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CertificateListCtl.class);

	protected void preload(HttpServletRequest request) {

	    CertificateModelInt model = ModelFactory.getInstance().getCertificateModel();

	    try {

	        List<CertificateDTO> list = model.list();

	        List<String> issueToList = new ArrayList<>();

	        for (CertificateDTO dto : list) {

	            if (dto.getIssueTo() != null && !issueToList.contains(dto.getIssueTo())) {
	                issueToList.add(dto.getIssueTo());
	            }
	        }

	        request.setAttribute("issueToList", issueToList);

	    } catch (Exception e) {
	        log.error(e);
	    }
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

	/**
	 * Display Logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CertificateListCtl doGet Start");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		CertificateDTO dto = (CertificateDTO) populateDTO(request);

		CertificateModelInt model = ModelFactory.getInstance().getCertificateModel();

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

		log.debug("CertificateListCtl doGet End");
	}

	/**
	 * Submit Logic
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CertificateListCtl doPost Start");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		CertificateDTO dto = (CertificateDTO) populateDTO(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		CertificateModelInt model = ModelFactory.getInstance().getCertificateModel();

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

				ServletUtility.redirect(ORSView.CERTIFICATE_CTL, request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.CERTIFICATE_LIST_CTL, request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					CertificateDTO deleteDto = new CertificateDTO();

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

				ServletUtility.redirect(ORSView.CERTIFICATE_LIST_CTL, request, response);
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

		log.debug("CertificateListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.CERTIFICATE_LIST_VIEW;
	}
}