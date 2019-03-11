package pl.zdzymira.model.visit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pl.zdzymira.model.doctor.Doctor;
import pl.zdzymira.model.patient.Patient;

@Entity
@Table(name= "visit")
public class Visit {

	@Id //primary key
	@SequenceGenerator(name="v_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="v_id")
	@NotNull
	@Column(name = "id", unique = true)
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity=Patient.class)
    @JoinColumn(name = "patient_id")
	private Patient patient;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity=Doctor.class)
    @JoinColumn(name = "doctor_id")
	@JsonIgnore
	private Doctor doctor;
	
	@Column(name = "purpose")
	private String purpose;
	@Column(name = "date")
	private String date;
	@Column(name = "hour")
	private String hour;
	@Column(name = "state")
	private String state;
	@Column(name = "comment")
	private String comment;
	

	public Visit()
	{
		
	}
	


	public Visit(Patient patient, Doctor doctor, String purpose, String date, String hour, String state) {
		super();
		this.patient = patient;
		this.doctor = doctor;
		this.purpose = purpose;
		this.date = date;
		this.hour = hour;
		this.state = state;
	}
	
	public void setAll(Patient patient, Doctor doctor, String purpose, String date, String hour, String state) {
		this.patient = patient;
		this.doctor = doctor;
		this.purpose = purpose;
		this.date = date;
		this.hour = hour;
		this.state = state;
	}

	@Override
	public String toString() {
	   return "Pacjent "+patient+" "+" u doktora "+doctor+" w celu: "+purpose+" Data: "+date+" "+ hour;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
