package net.msonic.out2;

import java.util.HashSet;
import java.util.Set;


import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	
	private Integer id;

	@NotEmpty
	private String name;

	@JsonIgnore
	
	private Set<User> users = new HashSet<User>();

	@Override
	public String getAuthority() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}