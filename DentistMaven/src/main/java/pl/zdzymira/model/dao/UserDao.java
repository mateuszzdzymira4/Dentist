package pl.zdzymira.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.zdzymira.model.user.UserP;

public interface UserDao extends CrudRepository<UserP, Integer>{

	public List<UserP> findAll();
	
	public UserP findById(int id);
	
	public UserP deleteById(int id);
	
	public UserP findByMail(String mail);

	
}

