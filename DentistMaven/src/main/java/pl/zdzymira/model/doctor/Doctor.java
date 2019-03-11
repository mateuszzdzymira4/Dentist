package pl.zdzymira.model.doctor;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import pl.zdzymira.model.patient.Patient;
import pl.zdzymira.model.user.UserP;
import pl.zdzymira.model.visit.Visit;

@Entity
@Table(name= "doctor")
public class Doctor {
	
	@Id //primary key
	@SequenceGenerator(name="d_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="d_id")
	@NotNull
	@Column(name = "id", unique = true)
	private int id;
	@Column(name = "fname")
	private String fname;
	@Column(name = "lname")
	private String lname;
	@Column(name = "mail")
	private String mail;
	@Column(name = "phone")
	private String phone;
	@OneToMany(mappedBy = "doctor", targetEntity=Visit.class)
    private List<Visit> visits;
	@OneToOne(mappedBy = "doctoracc", targetEntity=UserP.class)
    private UserP user;
	
	public Doctor()
	{
		
	}
	
	public Doctor(String fname, String sname, String mail, String phone) {
		super();
		this.fname = fname;
		this.lname = sname;
		this.mail = mail;
		this.phone = phone;
	}
	
	  @Override
	  public String toString() {
	    return "Imię i nazwisko: "+fname+" "+lname+" Mail: "+mail+" Telefon: "+phone+".";
	  }
	
	  
		public void setAll(String fname, String lname, String mail, String phone) {
			this.fname = fname;
			this.lname = lname;
			this.mail = mail;
			this.phone = phone;
		}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String sname) {
		this.lname = sname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

	public UserP getUser() {
		return user;
	}

	public void setUser(UserP user) {
		this.user = user;
	}

	
}
