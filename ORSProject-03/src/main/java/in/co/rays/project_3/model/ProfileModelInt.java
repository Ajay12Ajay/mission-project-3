package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.ProfileDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

/**
 * Interface of Profile model
 * 
 * @author Ajay Pratap Kerketta
 *
 */
public interface ProfileModelInt {

	public long add(ProfileDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(ProfileDTO dto) throws ApplicationException;

	public void update(ProfileDTO dto) throws ApplicationException, DuplicateRecordException;

	public List list() throws ApplicationException;

	public List list(int pageNo, int pageSize) throws ApplicationException;

	public List search(ProfileDTO dto) throws ApplicationException;

	public List search(ProfileDTO dto, int pageNo, int pageSize) throws ApplicationException;

	public ProfileDTO findByPK(long pk) throws ApplicationException;

	public ProfileDTO findByName(String name) throws ApplicationException;

}
