package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.CertificateDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of Certificate model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public interface CertificateModelInt {

	public long add(CertificateDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(CertificateDTO dto) throws ApplicationException;

	public void update(CertificateDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(CertificateDTO dto) throws ApplicationException;

	public List search(CertificateDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public CertificateDTO findByPK(long pk) throws ApplicationException;

	// Duplicate check based on certificate name
	public CertificateDTO findByCertificateName(String certificateName) throws ApplicationException;

}