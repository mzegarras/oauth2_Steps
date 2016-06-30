package net.msonic.out2;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Override
	public User findByLogin(String login) {
		// TODO Auto-generated method stub
		
		User user = new User();
		
		
		return user;
	}

}
