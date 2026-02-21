package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.VisitorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of Visitor model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public interface VisitorModelInt {

	public long add(VisitorDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(VisitorDTO dto) throws ApplicationException;

	public void update(VisitorDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(VisitorDTO dto) throws ApplicationException;

	public List search(VisitorDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public VisitorDTO findByPK(long pk) throws ApplicationException;

	// Duplicate check based on Visitor Pass Code
	public VisitorDTO findByVisitorPassCode(String visitorPassCode) throws ApplicationException;

}