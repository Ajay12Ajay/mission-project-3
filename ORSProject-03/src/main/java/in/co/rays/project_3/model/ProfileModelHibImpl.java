package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ProfileDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Profile model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public class ProfileModelHibImpl implements ProfileModelInt {

	@Override
	public long add(ProfileDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long pk = 0;

		// Check duplicate
		ProfileDTO existDto = findByName(dto.getFullName());
		if (existDto != null) {
			throw new DuplicateRecordException("Profile Name already exists");
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
			throw new ApplicationException("Exception in Profile Add " + e.getMessage());

		} finally {
			session.close();
		}
		return pk;
	}

	@Override
	public void delete(ProfileDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in Profile Delete " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public void update(ProfileDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		ProfileDTO existDto = findByName(dto.getFullName());

		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Profile Name already exists");
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
			throw new ApplicationException("Exception in Profile Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public ProfileDTO findByPK(long pk) throws ApplicationException {

		Session session = HibDataSource.getSession();
		ProfileDTO dto = null;

		try {
			dto = (ProfileDTO) session.get(ProfileDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Profile by PK");

		} finally {
			session.close();
		}
		return dto;
	}

	@Override
	public ProfileDTO findByName(String name) throws ApplicationException {

		Session session = HibDataSource.getSession();
		ProfileDTO dto = null;

		try {
			Criteria criteria = session.createCriteria(ProfileDTO.class);
			criteria.add(Restrictions.eq("fullName", name));
			List list = criteria.list();

			if (list.size() == 1) {
				dto = (ProfileDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Profile by Name " + e.getMessage());

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
			Criteria criteria = session.createCriteria(ProfileDTO.class);

			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Profile list");

		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(ProfileDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	@Override
	public List search(ProfileDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ProfileDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getFullName() != null && dto.getFullName().length() > 0) {
					criteria.add(Restrictions.like("fullName", dto.getFullName() + "%"));
				}

				if (dto.getGender() != null && dto.getGender().length() > 0) {
					criteria.add(Restrictions.eq("gender", dto.getGender()));
				}

				if (dto.getDob() != null) {
					criteria.add(Restrictions.eq("dob", dto.getDob()));
				}

				if (dto.getProfileStatus() != null && dto.getProfileStatus().length() > 0) {
					criteria.add(Restrictions.like("profileStatus", dto.getProfileStatus() + "%"));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult(((pageNo - 1) * pageSize));
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Profile search");

		} finally {
			session.close();
		}

		return list;
	}
}
