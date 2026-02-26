package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.IssueBookDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of IssueBook model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public interface IssueBookModelInt {

	public long add(IssueBookDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(IssueBookDTO dto) throws ApplicationException;

	public void update(IssueBookDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(IssueBookDTO dto) throws ApplicationException;

	public List search(IssueBookDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public IssueBookDTO findByPK(long pk) throws ApplicationException;

	// Duplicate check based on Issue Code (Unique)
	public IssueBookDTO findByIssueCode(String issueCode) throws ApplicationException;

}