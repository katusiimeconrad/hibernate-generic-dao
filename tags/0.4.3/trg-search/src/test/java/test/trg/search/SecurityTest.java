package test.trg.search;

import test.trg.shared.TestBase;
import test.trg.shared.model.Person;

import com.trg.search.Filter;
import com.trg.search.Search;
import com.trg.search.SearchFacade;

public class SecurityTest extends TestBase {
	protected SearchFacade target;

	public void testInjectionAttack() {
		Search s = new Search(Person.class);
		
		try {
			s.addField("address foo"); //spaces are not allowed
			target.search(s);
			fail("An exception should have been thrown.");
		} catch (IllegalArgumentException ex) {}
		
		try {
			s.setResultMode(Search.RESULT_ARRAY);
			s.addField("firstName + lastName");
			target.search(s);
			fail("An exception should have been thrown.");
		} catch (IllegalArgumentException ex) {}
		
		try {
			s.clear();
			s.addSortAsc("Mr. Friday");
			target.search(s);
			fail("An exception should have been thrown.");
		} catch (IllegalArgumentException ex) {}
		
		try {
			s.clear();
			s.addFilterGreaterThan("age-1", 44);
			target.search(s);
			fail("An exception should have been thrown.");
		} catch (IllegalArgumentException ex) {}
		
		try {
			s.clear();
			s.addFilterOr(Filter.equal("firstName", "Joe"), Filter.notEqual("age()", 44));
			target.search(s);
			fail("An exception should have been thrown.");
		} catch (IllegalArgumentException ex) {}
		
		//this shouldn't fail because property values are escaped
		s.clear();
		s.addFilterIn("firstName", "(select nonexistantProperty from Person)");
		target.search(s);
	}
}
