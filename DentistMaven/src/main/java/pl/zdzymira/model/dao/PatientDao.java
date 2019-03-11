package pl.zdzymira.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.zdzymira.model.patient.Patient;
import pl.zdzymira.model.user.UserP;

public interface PatientDao extends CrudRepository<Patient, Integer>{

	public List<Patient> findAll();
	
	public Patient findById(int id);
	
	public Patient findByMail(String mail);
	
}

