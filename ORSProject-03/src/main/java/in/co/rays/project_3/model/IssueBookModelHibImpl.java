package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.IssueBookDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of IssueBook model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public class IssueBookModelHibImpl implements IssueBookModelInt {

	@Override
	public long add(IssueBookDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long pk = 0;

		// Duplicate check based on Issue Code
		IssueBookDTO existDto = findByIssueCode(dto.getIssueCode());

		if (existDto != null) {
			throw new DuplicateRecordException("Issue Code already exists");
		}

		try {
			tx = session.beginTransaction();
			session.save(dto);
			pk = dto.getId();
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null) {
				tx.rollback();
			}

			throw new ApplicationException("Exception in IssueBook Add " + e.getMessage());

		} finally {
			session.close();
		}

		return pk;
	}

	@Override
	public void delete(IssueBookDTO dto) throws ApplicationException {

		Session session = null;
		Transaction tx = null;

		try {

			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null) {
				tx.rollback();
			}

			throw new ApplicationException("Exception in IssueBook Delete " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public void update(IssueBookDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		IssueBookDTO existDto = findByIssueCode(dto.getIssueCode());

		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Issue Code already exists");
		}

		try {

			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.update(dto);
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null) {
				tx.rollback();
			}

			throw new ApplicationException("Exception in IssueBook Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public IssueBookDTO findByPK(long pk) throws ApplicationException {

		Session session = HibDataSource.getSession();
		IssueBookDTO dto = null;

		try {

			dto = (IssueBookDTO) session.get(IssueBookDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting IssueBook by PK");

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public IssueBookDTO findByIssueCode(String issueCode) throws ApplicationException {

		Session session = HibDataSource.getSession();
		IssueBookDTO dto = null;

		try {

			Criteria criteria = session.createCriteria(IssueBookDTO.class);
			criteria.add(Restrictions.eq("issueCode", issueCode));

			List list = criteria.list();

			if (list.size() == 1) {
				dto = (IssueBookDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting IssueBook by Issue Code " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(IssueBookDTO.class);

			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in IssueBook list");

		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(IssueBookDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	@Override
	public List search(IssueBookDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(IssueBookDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getIssueCode() != null && dto.getIssueCode().length() > 0) {
					criteria.add(Restrictions.like("issueCode", dto.getIssueCode() + "%"));
				}

				if (dto.getStudentName() != null && dto.getStudentName().length() > 0) {
					criteria.add(Restrictions.like("studentName", dto.getStudentName() + "%"));
				}

				if (dto.getIssueDate() != null) {
					criteria.add(Restrictions.eq("issueDate", dto.getIssueDate()));
				}

				if (dto.getReturnDate() != null) {
					criteria.add(Restrictions.eq("returnDate", dto.getReturnDate()));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult(((pageNo - 1) * pageSize));
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in IssueBook search");

		} finally {
			session.close();
		}

		return list;
	}
}