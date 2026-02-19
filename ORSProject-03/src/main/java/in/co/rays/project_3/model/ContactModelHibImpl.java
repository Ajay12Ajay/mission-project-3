package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ContactDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Contact model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public class ContactModelHibImpl implements ContactModelInt {

	@Override
	public long add(ContactDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long pk = 0;

		// Duplicate check based on Name
		ContactDTO existDto = findByName(dto.getName());
		if (existDto != null) {
			throw new DuplicateRecordException("Contact Name already exists");
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
			throw new ApplicationException("Exception in Contact Add " + e.getMessage());

		} finally {
			session.close();
		}
		return pk;
	}

	@Override
	public void delete(ContactDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in Contact Delete " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public void update(ContactDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		ContactDTO existDto = findByName(dto.getName());

		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Contact Name already exists");
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
			throw new ApplicationException("Exception in Contact Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public ContactDTO findByPK(long pk) throws ApplicationException {

		Session session = HibDataSource.getSession();
		ContactDTO dto = null;

		try {
			dto = (ContactDTO) session.get(ContactDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Contact by PK");

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public ContactDTO findByName(String name) throws ApplicationException {

		Session session = HibDataSource.getSession();
		ContactDTO dto = null;

		try {
			Criteria criteria = session.createCriteria(ContactDTO.class);
			criteria.add(Restrictions.eq("name", name));
			List list = criteria.list();

			if (list.size() == 1) {
				dto = (ContactDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Contact by Name " + e.getMessage());

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
			Criteria criteria = session.createCriteria(ContactDTO.class);

			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Contact list");

		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(ContactDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	@Override
	public List search(ContactDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ContactDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getName() != null && dto.getName().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getName() + "%"));
				}

				if (dto.getEmail() != null && dto.getEmail().length() > 0) {
					criteria.add(Restrictions.like("email", dto.getEmail() + "%"));
				}

				if (dto.getMobileNo() != null && dto.getMobileNo().length() > 0) {
					criteria.add(Restrictions.like("mobileNo", dto.getMobileNo() + "%"));
				}

				if (dto.getMessage() != null && dto.getMessage().length() > 0) {
					criteria.add(Restrictions.like("message", dto.getMessage() + "%"));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult(((pageNo - 1) * pageSize));
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Contact search");

		} finally {
			session.close();
		}

		return list;
	}
}
