package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.AttendanceDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of Attendance model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public interface AttendanceModelInt {

	public long add(AttendanceDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(AttendanceDTO dto) throws ApplicationException;

	public void update(AttendanceDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(AttendanceDTO dto) throws ApplicationException;

	public List search(AttendanceDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public AttendanceDTO findByPK(long pk) throws ApplicationException;

	public AttendanceDTO findByName(String name) throws ApplicationException;

}
