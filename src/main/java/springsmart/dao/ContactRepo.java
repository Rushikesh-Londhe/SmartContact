package springsmart.dao;

import java.util.List;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springsmart.entity.Contact;
import springsmart.entity.User;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

	//pagination
	
	@Query("from Contact as c where c.user.id=:userId")
	//pageable contains curent page and contact per page
	public Page<Contact> findContactsByUser(@Param("userId")int id,Pageable pageable);
		
	//search by name
	public List<Contact> findByNameContainingAndUser(String name,User user);
		
	
}
