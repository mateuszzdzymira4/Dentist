package pl.zdzymira.model.visit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class VisitService {
	

	private static List <Visit> visits = new ArrayList<>(Arrays.asList(

				));

	public List<Visit> getAllVisits()
	{
		return visits;
	}
	
	public Visit getVisit(int id) {
		return visits.stream().filter(t -> (t.getId() == id)).findFirst().get();
	}

	public void addVisit(Visit visit) {
		visits.add(visit);
		
	}

	public void updateVisit(int id, Visit visit) {
		for (int i=0; i < visits.size(); i++){
			Visit t = visits.get(i);
			if (t.getId() == id) {
				visits.set(i, visit);
				return;
			}
		}
		
	}

	public void deleteVisit(int id) {
		visits.removeIf(t -> (t.getId() == id));
	}


}
