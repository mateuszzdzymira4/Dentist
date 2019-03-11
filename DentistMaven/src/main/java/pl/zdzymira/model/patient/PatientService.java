package pl.zdzymira.model.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class PatientService {
	private static List <Patient> patients = new ArrayList<>(Arrays.asList(

				));
	
	public List<Patient> getAllPatients()
	{
		return patients;
	}
	
	public static Patient getPatient(Integer id) {
		return patients.stream().filter(t -> (t.getId() == id)).findFirst().get();
	}

	public void addPatient(Patient patient) {
		patients.add(patient);
		
	}

	public void updatePatient(Integer id, Patient patient) {
		for (int i=0; i < patients.size(); i++){
			Patient t = patients.get(i);
			if (t.getId() == id) {
				patients.set(i, patient);
				return;
			}
		}
		
	}

	public void deletePatient(Integer id) {
		patients.removeIf(t -> (t.getId() == id));
	}


}
