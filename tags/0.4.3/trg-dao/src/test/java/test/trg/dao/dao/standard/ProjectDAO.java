package test.trg.dao.dao.standard;

import java.util.List;

import test.trg.shared.model.Person;
import test.trg.shared.model.Project;

import com.trg.dao.dao.standard.GenericDAO;
import com.trg.search.Search;

public interface ProjectDAO extends GenericDAO<Project, Long> {
	/**
	 * Returns a list of all projects of which the given person is a member.
	 */
	public List<Project> findProjectsForMember(Person member);
	
	/**
	 * Returns a search that will find all the projects for which the given
	 * person is a member.
	 */
	public Search getProjectsForMemberSearch(Person member);
}
