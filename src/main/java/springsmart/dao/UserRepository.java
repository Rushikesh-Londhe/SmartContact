package springsmart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springsmart.entity.Contact;
import springsmart.entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{
@Query("select u from User u where u.email=:email")
	public User getUserByUserName(@Param("email") String email);

	/*
	 * @Query("DELETE FROM Contact c WHERE c.cid = :conid") void
	 * deleteContactById(@Param("conid") Integer id);
	 */
}
