package in.co.rays.project_3.model;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * ModelFactory decides which model implementation run
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public final class ModelFactory {

	private static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.project_3.bundle.system");
	private static final String DATABASE = rb.getString("DATABASE");
	private static ModelFactory mFactory = null;
	private static HashMap modelCache = new HashMap();

	private ModelFactory() {

	}

	public static ModelFactory getInstance() {
		if (mFactory == null) {
			mFactory = new ModelFactory();
		}
		return mFactory;
	}

	public ProductModelInt getProductModel() {
		ProductModelInt productModel = (ProductModelInt) modelCache.get("productModel");
		if (productModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				productModel = new ProductModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				productModel = new ProductModelHibImp();
			}
			modelCache.put("productModel", productModel);
		}
		return productModel;
	}

	public MarksheetModelInt getMarksheetModel() {
		MarksheetModelInt marksheetModel = (MarksheetModelInt) modelCache.get("marksheetModel");
		if (marksheetModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				marksheetModel = new MarksheetModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				marksheetModel = new MarksheetModelJDBCImpl();
			}
			modelCache.put("marksheetModel", marksheetModel);
		}
		return marksheetModel;
	}

	public CollegeModelInt getCollegeModel() {
		CollegeModelInt collegeModel = (CollegeModelInt) modelCache.get("collegeModel");
		if (collegeModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				collegeModel = new CollegeModelHibImp();

			}
			if ("JDBC".equals(DATABASE)) {
				collegeModel = new CollegeModelJDBCImpl();
			}
			modelCache.put("collegeModel", collegeModel);
		}
		return collegeModel;
	}

	public RoleModelInt getRoleModel() {
		RoleModelInt roleModel = (RoleModelInt) modelCache.get("roleModel");
		if (roleModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				roleModel = new RoleModelHibImp();

			}
			if ("JDBC".equals(DATABASE)) {
				roleModel = new RoleModelJDBCImpl();
			}
			modelCache.put("roleModel", roleModel);
		}
		return roleModel;
	}

	public UserModelInt getUserModel() {

		UserModelInt userModel = (UserModelInt) modelCache.get("userModel");
		if (userModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				userModel = new UserModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				userModel = new UserModelJDBCImpl();
			}
			modelCache.put("userModel", userModel);
		}

		return userModel;
	}

	public StudentModelInt getStudentModel() {
		StudentModelInt studentModel = (StudentModelInt) modelCache.get("studentModel");
		if (studentModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				studentModel = new StudentModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				studentModel = new StudentModelJDBCImpl();
			}
			modelCache.put("studentModel", studentModel);
		}

		return studentModel;
	}

	public CourseModelInt getCourseModel() {
		CourseModelInt courseModel = (CourseModelInt) modelCache.get("courseModel");
		if (courseModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				courseModel = new CourseModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				courseModel = new CourseModelJDBCImpl();
			}
			modelCache.put("courseModel", courseModel);
		}

		return courseModel;
	}

	public TimetableModelInt getTimetableModel() {

		TimetableModelInt timetableModel = (TimetableModelInt) modelCache.get("timetableModel");

		if (timetableModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				timetableModel = new TimetableModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				timetableModel = new TimetableModelJDBCImpl();
			}
			modelCache.put("timetableModel", timetableModel);
		}

		return timetableModel;
	}

	public SubjectModelInt getSubjectModel() {
		SubjectModelInt subjectModel = (SubjectModelInt) modelCache.get("subjectModel");
		if (subjectModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				subjectModel = new SubjectModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				subjectModel = new SubjectModelJDBCImpl();
			}
			modelCache.put("subjectModel", subjectModel);
		}

		return subjectModel;
	}

	public FacultyModelInt getFacultyModel() {
		FacultyModelInt facultyModel = (FacultyModelInt) modelCache.get("facultyModel");
		if (facultyModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				facultyModel = new FacultyModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				facultyModel = new FacultyModelJDBCImpl();
			}
			modelCache.put("facultyModel", facultyModel);
		}

		return facultyModel;
	}

	public ProfileModelInt getProfileModel() {

		ProfileModelInt profileModel = (ProfileModelInt) modelCache.get("profileModel");

		if (profileModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				profileModel = new ProfileModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
				profileModel = new ProfileModelHibImpl();
			}

			modelCache.put("profileModel", profileModel);
		}

		return profileModel;
	}

	public ContactModelInt getContactModel() {

		ContactModelInt contactModel = (ContactModelInt) modelCache.get("contactModel");

		if (contactModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				contactModel = new ContactModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
				contactModel = new ContactModelHibImpl();
			}

			modelCache.put("contactModel", contactModel);
		}

		return contactModel;
	}

	public AttendanceModelInt getAttendanceModel() {

		AttendanceModelInt attendanceModel = (AttendanceModelInt) modelCache.get("attendanceModel");

		if (attendanceModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				attendanceModel = new AttendanceModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
				attendanceModel = new AttendanceModelHibImpl();
			}

			modelCache.put("attendanceModel", attendanceModel);
		}

		return attendanceModel;
	}

	public CertificateModelInt getCertificateModel() {

		CertificateModelInt certificateModel = (CertificateModelInt) modelCache.get("certificateModel");

		if (certificateModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				certificateModel = new CertificateModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
				certificateModel = new CertificateModelHibImpl();
			}

			modelCache.put("certificateModel", certificateModel);
		}

		return certificateModel;
	}

	public VisitorModelInt getVisitorModel() {

		VisitorModelInt visitorModel = (VisitorModelInt) modelCache.get("visitorModel");

		if (visitorModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				visitorModel = new VisitorModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
				visitorModel = new VisitorModelHibImpl();
			}

			modelCache.put("visitorModel", visitorModel);
		}

		return visitorModel;
	}

	public PaymentModelInt getPaymentModel() {

		PaymentModelInt paymentModel = (PaymentModelInt) modelCache.get("paymentModel");

		if (paymentModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				paymentModel = new PaymentModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
				paymentModel = new PaymentModelHibImpl();
			}

			modelCache.put("paymentModel", paymentModel);
		}

		return paymentModel;
	}

	public EventModelInt getEventModel() {

		EventModelInt eventModel = (EventModelInt) modelCache.get("eventModel");

		if (eventModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				eventModel = new EventModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
				eventModel = new EventModelHibImpl();
			}

			modelCache.put("eventModel", eventModel);
		}

		return eventModel;
	}

	public IssueBookModelInt getIssueBookModel() {

		IssueBookModelInt issueBookModel = (IssueBookModelInt) modelCache.get("issueBookModel");

		if (issueBookModel == null) {

			if ("Hibernate".equals(DATABASE)) {
				issueBookModel = new IssueBookModelHibImpl();
			}

			if ("JDBC".equals(DATABASE)) {
				issueBookModel = new IssueBookModelHibImpl();
				
			}

			modelCache.put("issueBookModel", issueBookModel);
		}

		return issueBookModel;
	}

}
