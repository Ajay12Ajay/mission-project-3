package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.AttendanceDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Attendance model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public class AttendanceModelHibImpl implements AttendanceModelInt {

	@Override
	public long add(AttendanceDTO dto)
			throws ApplicationException, DuplicateRecordException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long pk = 0;

		// Duplicate check by Name
		AttendanceDTO existDto = findByName(dto.getName());
		if (existDto != null) {
			throw new DuplicateRecordException(
					"Attendance Name already exists");
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

			throw new ApplicationException(
					"Exception in Attendance Add "
							+ e.getMessage());

		} finally {
			session.close();
		}

		return pk;
	}

	@Override
	public void delete(AttendanceDTO dto)
			throws ApplicationException {

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

			throw new ApplicationException(
					"Exception in Attendance Delete "
							+ e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public void update(AttendanceDTO dto)
			throws ApplicationException,
			DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		AttendanceDTO existDto =
				findByName(dto.getName());

		if (existDto != null
				&& existDto.getId() != dto.getId()) {

			throw new DuplicateRecordException(
					"Attendance Name already exists");
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

			throw new ApplicationException(
					"Exception in Attendance Update "
							+ e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public AttendanceDTO findByPK(long pk)
			throws ApplicationException {

		Session session = HibDataSource.getSession();
		AttendanceDTO dto = null;

		try {
			dto = (AttendanceDTO) session.get(
					AttendanceDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException(
					"Exception in getting Attendance by PK");

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public AttendanceDTO findByName(String name)
			throws ApplicationException {

		Session session = HibDataSource.getSession();
		AttendanceDTO dto = null;

		try {

			Criteria criteria =
					session.createCriteria(
							AttendanceDTO.class);

			criteria.add(
					Restrictions.eq("name", name));

			List list = criteria.list();

			if (list.size() == 1) {
				dto = (AttendanceDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException(
					"Exception in getting Attendance by Name "
							+ e.getMessage());

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
	public List list(int pageNo, int pageSize)
			throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria =
					session.createCriteria(
							AttendanceDTO.class);

			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException(
					"Exception in Attendance list");

		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(AttendanceDTO dto)
			throws ApplicationException {

		return search(dto, 0, 0);
	}

	@Override
	public List search(AttendanceDTO dto,
			int pageNo, int pageSize)
			throws ApplicationException {

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria =
					session.createCriteria(
							AttendanceDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(
							Restrictions.eq("id",
									dto.getId()));
				}

				if (dto.getName() != null
						&& dto.getName().length() > 0) {

					criteria.add(
							Restrictions.like("name",
									dto.getName() + "%"));
				}

				if (dto.getAttendanceDate() != null) {

					criteria.add(
							Restrictions.eq(
									"attendanceDate",
									dto.getAttendanceDate()));
				}

				if (dto.getAttendanceStatus() != null
						&& dto.getAttendanceStatus()
								.length() > 0) {

					criteria.add(
							Restrictions.like(
									"attendanceStatus",
									dto.getAttendanceStatus()
											+ "%"));
				}

				if (dto.getRemarks() != null
						&& dto.getRemarks().length() > 0) {

					criteria.add(
							Restrictions.like("remarks",
									dto.getRemarks() + "%"));
				}
			}

			if (pageSize > 0) {

				criteria.setFirstResult(
						((pageNo - 1) * pageSize));
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException(
					"Exception in Attendance search");

		} finally {
			session.close();
		}

		return list;
	}
}
