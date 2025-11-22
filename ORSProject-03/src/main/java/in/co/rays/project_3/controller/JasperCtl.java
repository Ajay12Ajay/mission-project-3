package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.impl.SessionImpl;

import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.util.HibDataSource;
import in.co.rays.project_3.util.JDBCDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@WebServlet(name = "JasperCtl", urlPatterns = { "/ctl/JasperCtl" })
public class JasperCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.project_3.bundle.system");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println(">>> JasperCtl doGet() started");

		try {

			System.out.println("Step 1: Loading Jasper report file path");
			String jasperFile = System.getenv("JASPER_REPORT");

			if (jasperFile == null) {
				System.out.println("ENV variable not found, reading from ResourceBundle");
				jasperFile = rb.getString("JASPER_REPORT");
			}

			System.out.println("Jasper File Path = " + jasperFile);

			System.out.println("Step 2: Compiling Jasper Report");
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperFile);
			System.out.println("Jasper report compiled successfully");

			System.out.println("Step 3: Fetching User from Session");
			HttpSession session = request.getSession(true);
			UserDTO dto = (UserDTO) session.getAttribute("user");

			if (dto == null) {
				System.out.println("ERROR: UserDTO in session is NULL");
			} else {
				System.out.println("User found: " + dto.getFirstName() + " " + dto.getLastName());
			}

			System.out.println("Step 4: Preparing parameters");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", 1l);

			java.sql.Connection conn = null;

			ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.project_3.bundle.system");
			String Database = rb.getString("DATABASE");

			System.out.println("Database configured as: " + Database);

			if ("Hibernate".equalsIgnoreCase(Database)) {
				System.out.println("Getting Hibernate connection");
				conn = ((SessionImpl) HibDataSource.getSession()).connection();
			}

			if ("JDBC".equalsIgnoreCase(Database)) {
				System.out.println("Getting JDBC connection");
				conn = JDBCDataSource.getConnection();
			}

			if (conn == null) {
				System.out.println("ERROR: Database connection is NULL");
			} else {
				System.out.println("Database connection established");
			}

			System.out.println("Step 5: Filling Jasper Report");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, conn);
			System.out.println("Report filled successfully");

			System.out.println("Step 6: Exporting report to PDF");
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			System.out.println("PDF generated successfully. Size: " + pdf.length + " bytes");

			System.out.println("Step 7: Sending PDF to browser");
			response.setContentType("application/pdf");
			response.getOutputStream().write(pdf);
			response.getOutputStream().flush();
			System.out.println("PDF sent successfully");

		} catch (Exception e) {
			System.out.println("************* EXCEPTION IN JasperCtl *************");
			e.printStackTrace();
		}

		System.out.println("<<< JasperCtl doGet() completed");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(">>> JasperCtl doPost() triggered (not implemented)");
	}

	@Override
	protected String getView() {
		System.out.println("getView() called but returns null");
		return null;
	}
}
