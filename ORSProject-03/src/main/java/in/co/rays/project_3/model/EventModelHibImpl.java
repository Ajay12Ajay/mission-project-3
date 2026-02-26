package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Event model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public class EventModelHibImpl implements EventModelInt {

	/**
	 * Add Event
	 */
	@Override
	public long add(EventDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long pk = 0;

		// 🔥 Duplicate check based on Event Code (UNIQUE)
		EventDTO existDto = findByEventCode(dto.getEventCode());

		if (existDto != null) {
			throw new DuplicateRecordException("Event Code already exists");
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

			throw new ApplicationException("Exception in Event Add " + e.getMessage());

		} finally {
			session.close();
		}

		return pk;
	}

	/**
	 * Delete Event
	 */
	@Override
	public void delete(EventDTO dto) throws ApplicationException {

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

			throw new ApplicationException("Exception in Event Delete " + e.getMessage());

		} finally {
			session.close();
		}
	}

	/**
	 * Update Event
	 */
	@Override
	public void update(EventDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		EventDTO existDto = findByEventCode(dto.getEventCode());

		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Event Code already exists");
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

			throw new ApplicationException("Exception in Event Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	/**
	 * Find By PK
	 */
	@Override
	public EventDTO findByPK(long pk) throws ApplicationException {

		Session session = HibDataSource.getSession();
		EventDTO dto = null;

		try {

			dto = (EventDTO) session.get(EventDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Event by PK");

		} finally {
			session.close();
		}

		return dto;
	}

	/**
	 * Find By Event Code (UNIQUE)
	 */
	@Override
	public EventDTO findByEventCode(String eventCode) throws ApplicationException {

		Session session = HibDataSource.getSession();
		EventDTO dto = null;

		try {

			Criteria criteria = session.createCriteria(EventDTO.class);

			criteria.add(Restrictions.eq("eventCode", eventCode));

			List list = criteria.list();

			if (list.size() == 1) {
				dto = (EventDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Event by Code " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * List with Pagination
	 */
	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EventDTO.class);

			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Event list");

		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(EventDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	/**
	 * Search with Filters
	 */
	@Override
	public List search(EventDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EventDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getEventCode() != null && dto.getEventCode().length() > 0) {

					criteria.add(Restrictions.like("eventCode", dto.getEventCode() + "%"));
				}

				if (dto.getEventName() != null && dto.getEventName().length() > 0) {

					criteria.add(Restrictions.like("eventName", dto.getEventName() + "%"));
				}

				if (dto.getOrganizer() != null && dto.getOrganizer().length() > 0) {

					criteria.add(Restrictions.like("organizer", dto.getOrganizer() + "%"));
				}

				if (dto.getEventDate() != null) {

					criteria.add(Restrictions.eq("eventDate", dto.getEventDate()));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult(((pageNo - 1) * pageSize));
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Event search");

		} finally {
			session.close();
		}

		return list;
	}
}