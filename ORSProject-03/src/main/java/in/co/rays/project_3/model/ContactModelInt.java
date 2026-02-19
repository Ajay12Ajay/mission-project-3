package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.ContactDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of Contact model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public interface ContactModelInt {

	public long add(ContactDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(ContactDTO dto) throws ApplicationException;

	public void update(ContactDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(ContactDTO dto) throws ApplicationException;

	public List search(ContactDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public ContactDTO findByPK(long pk) throws ApplicationException;

	public ContactDTO findByName(String name) throws ApplicationException;

}
