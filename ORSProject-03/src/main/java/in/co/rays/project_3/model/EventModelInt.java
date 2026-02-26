package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.EventDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of Event model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public interface EventModelInt {

	public long add(EventDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(EventDTO dto) throws ApplicationException;

	public void update(EventDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(EventDTO dto) throws ApplicationException;

	public List search(EventDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public EventDTO findByPK(long pk) throws ApplicationException;

	// Duplicate check based on Event Code (UNIQUE)
	public EventDTO findByEventCode(String eventCode) throws ApplicationException;

}