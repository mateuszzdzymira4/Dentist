package pl.zdzymira.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.zdzymira.model.doctor.Doctor;

public interface DoctorDao extends CrudRepository<Doctor, Integer>{

	public List<Doctor> findAll();
	
	public Doctor findById(int id);
	
	public Doctor findByMail(String mail);
	
	public Doctor deleteById(int id);
	
}

