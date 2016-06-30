package net.msonic.out2;


public interface UserRepository {

	User findByLogin(String login);
}
