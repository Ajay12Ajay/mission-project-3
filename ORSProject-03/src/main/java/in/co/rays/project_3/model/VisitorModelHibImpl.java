package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.VisitorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Visitor model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public class VisitorModelHibImpl implements VisitorModelInt {

	@Override
	public long add(VisitorDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long pk = 0;

		// Duplicate check based on Visitor Pass Code
		VisitorDTO existDto = findByVisitorPassCode(dto.getVisitorPassCode());

		if (existDto != null) {
			throw new DuplicateRecordException("Visitor Pass Code already exists");
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

			throw new ApplicationException("Exception in Visitor Add " + e.getMessage());

		} finally {
			session.close();
		}

		return pk;
	}

	@Override
	public void delete(VisitorDTO dto) throws ApplicationException {

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

			throw new ApplicationException("Exception in Visitor Delete " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public void update(VisitorDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		VisitorDTO existDto = findByVisitorPassCode(dto.getVisitorPassCode());

		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Visitor Pass Code already exists");
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

			throw new ApplicationException("Exception in Visitor Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public VisitorDTO findByPK(long pk) throws ApplicationException {

		Session session = HibDataSource.getSession();
		VisitorDTO dto = null;

		try {

			dto = (VisitorDTO) session.get(VisitorDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Visitor by PK");

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public VisitorDTO findByVisitorPassCode(String visitorPassCode) throws ApplicationException {

		Session session = HibDataSource.getSession();
		VisitorDTO dto = null;

		try {

			Criteria criteria = session.createCriteria(VisitorDTO.class);
			criteria.add(Restrictions.eq("visitorPassCode", visitorPassCode));

			List list = criteria.list();

			if (list.size() == 1) {
				dto = (VisitorDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Visitor by Pass Code " + e.getMessage());

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
			Criteria criteria = session.createCriteria(VisitorDTO.class);

			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Visitor list");

		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(VisitorDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	@Override
	public List search(VisitorDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(VisitorDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getVisitorPassCode() != null && dto.getVisitorPassCode().length() > 0) {
					criteria.add(Restrictions.like("visitorPassCode", dto.getVisitorPassCode() + "%"));
				}

				if (dto.getVisitorName() != null && dto.getVisitorName().length() > 0) {
					criteria.add(Restrictions.like("visitorName", dto.getVisitorName() + "%"));
				}

				if (dto.getVisitorPurpose() != null && dto.getVisitorPurpose().length() > 0) {
					criteria.add(Restrictions.like("visitorPurpose", dto.getVisitorPurpose() + "%"));
				}

				if (dto.getVisitDate() != null) {
					criteria.add(Restrictions.eq("visitDate", dto.getVisitDate()));
				}

				if (dto.getVisitStatus() != null && dto.getVisitStatus().length() > 0) {
					criteria.add(Restrictions.like("visitStatus", dto.getVisitStatus() + "%"));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult(((pageNo - 1) * pageSize));
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Visitor search");

		} finally {
			session.close();
		}

		return list;
	}
}