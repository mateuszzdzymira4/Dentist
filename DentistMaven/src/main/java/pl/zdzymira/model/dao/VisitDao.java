package pl.zdzymira.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.zdzymira.model.doctor.Doctor;
import pl.zdzymira.model.visit.Visit;

public interface VisitDao extends CrudRepository<Visit, Integer>{

	public List<Visit> findAll();
	
	public Visit findById(int id);
	

	
}

