package pl.zdzymira.model.patient;

import java.util.Date;
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

import pl.zdzymira.model.user.UserP;
import pl.zdzymira.model.visit.Visit;

@Entity
@Table(name= "patient")
public class Patient {

	@Id //primary key
	@SequenceGenerator(name="p_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="p_id")
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
	@NotNull
	@Column(name = "creationdate")
	private Date creationDate;
	@OneToMany(mappedBy = "patient", targetEntity=Visit.class)
    private List<Visit> visits;
	@OneToOne(mappedBy = "patientacc", targetEntity=UserP.class)
    private UserP user;
	/*@OneToMany(mappedBy = "patient", targetEntity=UserP.class)
    private List<UserP> user;*/

	public Patient()
	{
		
	}
	
	public Patient(String fname, String sname, String mail, String phone) {
		super();
		this.fname = fname;
		this.lname = sname;
		this.mail = mail;
		this.phone = phone;
		this.creationDate = new Date();
	}
	
	  @Override
	  public String toString() {
	    return "id: "+id+" Imię i nazwisko: "+fname+" "+lname+" Mail: "+mail+" Telefon: "+phone+".";
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
	
	public String getFullName() {
		return fname+" "+lname;
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


	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
