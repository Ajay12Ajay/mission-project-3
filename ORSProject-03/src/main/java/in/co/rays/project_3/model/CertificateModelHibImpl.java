package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.CertificateDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Certificate model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public class CertificateModelHibImpl implements CertificateModelInt {

	@Override
	public long add(CertificateDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long pk = 0;

		/*
		 * // Duplicate check based on Certificate Name CertificateDTO existDto =
		 * findByCertificateName(dto.getCertificateName());
		 * 
		 * if (existDto != null) { throw new
		 * DuplicateRecordException("Certificate Name already exists"); }
		 */

		try {
			tx = session.beginTransaction();
			session.save(dto);
			pk = dto.getId();
			tx.commit();

		} catch (HibernateException e) {

			if (tx != null) {
				tx.rollback();
			}

			throw new ApplicationException("Exception in Certificate Add " + e.getMessage());

		} finally {
			session.close();
		}

		return pk;
	}

	@Override
	public void delete(CertificateDTO dto) throws ApplicationException {

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

			throw new ApplicationException("Exception in Certificate Delete " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public void update(CertificateDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		CertificateDTO existDto = findByCertificateName(dto.getCertificateName());

		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Certificate Name already exists");
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

			throw new ApplicationException("Exception in Certificate Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	@Override
	public CertificateDTO findByPK(long pk) throws ApplicationException {

		Session session = HibDataSource.getSession();
		CertificateDTO dto = null;

		try {

			dto = (CertificateDTO) session.get(CertificateDTO.class, pk);

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Certificate by PK");

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public CertificateDTO findByCertificateName(String certificateName) throws ApplicationException {

		Session session = HibDataSource.getSession();
		CertificateDTO dto = null;

		try {

			Criteria criteria = session.createCriteria(CertificateDTO.class);
			criteria.add(Restrictions.eq("certificateName", certificateName));

			List list = criteria.list();

			if (list.size() == 1) {
				dto = (CertificateDTO) list.get(0);
			}

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting Certificate by Name " + e.getMessage());

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
			Criteria criteria = session.createCriteria(CertificateDTO.class);

			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize);
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Certificate list");

		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(CertificateDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	@Override
	public List search(CertificateDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;

		try {

			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CertificateDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}

				if (dto.getCertificateName() != null && dto.getCertificateName().length() > 0) {
					criteria.add(Restrictions.like("certificateName", dto.getCertificateName() + "%"));
				}

				if (dto.getIssueTo() != null && dto.getIssueTo().length() > 0) {
					criteria.add(Restrictions.like("issueTo", dto.getIssueTo() + "%"));
				}

				if (dto.getIssueDate() != null) {
					criteria.add(Restrictions.eq("issueDate", dto.getIssueDate()));
				}

				if (dto.getCertificateStatus() != null && dto.getCertificateStatus().length() > 0) {
					criteria.add(Restrictions.like("certificateStatus", dto.getCertificateStatus() + "%"));
				}
			}

			if (pageSize > 0) {
				criteria.setFirstResult(((pageNo - 1) * pageSize));
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception in Certificate search");

		} finally {
			session.close();
		}

		return list;
	}
}