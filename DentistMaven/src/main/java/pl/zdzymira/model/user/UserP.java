package pl.zdzymira.model.user;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import pl.zdzymira.model.doctor.Doctor;
import pl.zdzymira.model.patient.Patient;

@Entity
@Table(name= "users")
public class UserP {

	@Id //primary key
	@SequenceGenerator(name="user_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_id")
	@NotNull
	@Column(name = "id", unique = true)
	private int id;
	@Column(name = "name")
	private String name;
	@NotNull
	@Column(name = "password")
	private String password;
	@NotNull
	@Column(name = "mail")
	private String mail;
	@Column(name = "role")
	private String role;
	@NotNull
	@Column(name = "creationdate")
	private Date creationDate;
	@Column(name = "verified")
	private boolean verified;
	@OneToOne(fetch = FetchType.EAGER, targetEntity=Patient.class)
    @JoinColumn(name = "patient_id")
	private Patient patientacc;
	@OneToOne(fetch = FetchType.EAGER, targetEntity=Doctor.class)
    @JoinColumn(name = "doctor_id")
	private Doctor doctoracc;
	
	
	public UserP()
	{
		
	}
	
	public UserP(String password, String mail, String role, Patient patient, Doctor doctor) {
		super();
		this.password = password;
		this.mail = mail;
		this.creationDate = new Date();
		this.verified = false;
		this.role = role;
		this.patientacc = patient;
		this.doctoracc = doctor;
	}
	
	
	
	
	public Patient getPatientacc() {
		return patientacc;
	}

	public void setPatientacc(Patient patientacc) {
		this.patientacc = patientacc;
	}

	public Doctor getDoctoracc() {
		return doctoracc;
	}

	public void setDoctoracc(Doctor doctoracc) {
		this.doctoracc = doctoracc;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
