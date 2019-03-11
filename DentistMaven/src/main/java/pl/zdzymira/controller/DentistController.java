package pl.zdzymira.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.zdzymira.model.dao.DoctorDao;
import pl.zdzymira.model.dao.PatientDao;
import pl.zdzymira.model.dao.UserDao;
import pl.zdzymira.model.dao.VisitDao;
import pl.zdzymira.model.doctor.Doctor;
import pl.zdzymira.model.patient.Patient;
import pl.zdzymira.model.patient.PatientService;
import pl.zdzymira.model.user.UserP;
import pl.zdzymira.model.visit.Visit;
import pl.zdzymira.model.visit.VisitService;

@Controller
public class DentistController {
	
	int TempId=0;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private VisitDao visitDao;
	
	@Autowired
	private DoctorDao doctorDao;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private VisitService visitService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	 public String index()
	 {
		 return "index";
	 }
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	 public String login()
	 {
		
		 return "login";
	 }
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	 public String home()
	 {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			  Collection<? extends GrantedAuthority> authorities = ((UserDetails)principal).getAuthorities();
			  if(authorities.toString().equals("[ROLE_USER]")) {
				  return "redirect:/user/home";
			  }
			  else if (authorities.toString().equals("[ROLE_DOCTOR]"))
			  {	
				  return "redirect:/doctor/home";
			  }
			  else if (authorities.toString().equals("[ROLE_ADMIN]"))
			  {
				  return "redirect:/admin/home";
			  }
			  else {
				  return "login";
			  }
	   	} else {
	   		return "login";
			}
	 }
	
	@RequestMapping(value = "/doctormain", method = RequestMethod.GET)
	 public String showDoctorMain(HttpServletRequest request, Model model)
	 {
		
		 return "doctormain";
	 }
	
	@RequestMapping(value = "/calendar", method = RequestMethod.GET)
	 public String calendar()
	 {
		 return "calendar";
	 }
	
	@RequestMapping(value = "/patientdata", method = RequestMethod.GET)
	 public String getPatientData()
	 {
		 return "patientdata";
	 }
	
	
	@RequestMapping(value = "/patientdata", method = RequestMethod.POST)
	 public String addPatient(HttpServletRequest request, Model model)
	 {
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String mail = request.getParameter("mail");
		String phone = request.getParameter("phone");
		
		Patient patient = new Patient(fname, lname, mail, phone);
		patientService.addPatient(patient);

		 return "redirect:/visitdata/"+patient.getId();
	 }
	
	
	@RequestMapping(value = "/visitdata/{id}", method = RequestMethod.GET)
	 public String showVisitData(@PathVariable int id, Model model)
	 {
		List<Doctor> doctor = doctorDao.findAll();
		model.addAttribute("doctors", doctor);
	
		 return "visitdata";
	 }
	
	@RequestMapping(value = "/visitdata/{id}", method = RequestMethod.POST)
	 public String showSummary(@PathVariable int id, HttpServletRequest request, Model model)
	 {
		
		
		Patient patient = PatientService.getPatient(id);
		String doctorid = request.getParameter("doctor");
		String purpose = request.getParameter("purpose");
		String date = request.getParameter("date");
		String hour = request.getParameter("hour");
		String state = "Zarejestrowano";
		int doctoridint = Integer.parseInt(doctorid);
		
		System.out.println(doctoridint);
		
		Doctor doctor = doctorDao.findById(doctoridint);
		
		Visit visit = new Visit(patient, doctor, purpose, date, hour, state);
		
		visitService.addVisit(visit);
		
		 return "redirect:/visitsummary/"+visit.getId();
	 }
	
	@RequestMapping(value = "/visitsummary/{id}", method = RequestMethod.GET)
	 public String getSummary(@PathVariable int id, Model model)
	 {
		Visit visit = visitService.getVisit(id);
		Patient patient = visit.getPatient();
		
		model.addAttribute("patient", patient);	
		model.addAttribute("doctor", visit.getDoctor());
		model.addAttribute("purpose", visit.getPurpose());
		model.addAttribute("date", visit.getDate());
		model.addAttribute("hour", visit.getHour());
		
		
		 return "visitsummary";
	 }
	
	@RequestMapping(value = "/visitsummary/{id}", method = RequestMethod.POST)
	 public String submitVisit(@PathVariable int id, HttpServletRequest request, Model model)
	 {
		Visit visit = visitService.getVisit(id);
		Patient patient = visit.getPatient();
		
		patientDao.save(patient);
		visitDao.save(visit);
		
		 return "redirect:/requestsent/"+visit.getId();
	 }
	
	@RequestMapping(value = "/requestsent/{id}", method = RequestMethod.GET)
	 public String requestSent(@PathVariable int id, Model model)
	 {
		Visit visit = visitDao.findById(id);
		Patient patient = visit.getPatient();
		
		model.addAttribute("patient", patient);	
		model.addAttribute("doctor", visit.getDoctor());
		model.addAttribute("purpose", visit.getPurpose());
		model.addAttribute("date", visit.getDate());
		model.addAttribute("hour", visit.getHour());
		
		 return "requestsent";
	 }	

	
	@RequestMapping(value = "/{role}/verifyvisits", method = RequestMethod.GET)
	 public String showUnverifiedVisits(@PathVariable String role, Model model)
	 {
		List<Visit> visit = visitDao.findAll();
		model.addAttribute("visits", visit);
		model.addAttribute("role", role);
		
		
		 return "verifyvisits";
	 }
	
/*	@RequestMapping(value = "/verifyvisits", method = RequestMethod.POST)
	 public String showUnverifiedVisits(HttpServletRequest request, Model model)
	 {

		List<Visit> visit = visitDao.findAll();
		
		model.addAttribute("visits", visit);
		String target = "editvisit/"+request.getParameter("id");
		
		
		 return "redirect:/"+target;
	 }
	
*/
	@RequestMapping(value = "{role}/editvisit/{id}", method = RequestMethod.GET)
	 public String editVisit(@PathVariable int id, HttpServletRequest request, Model model)
	 {

		Visit visit = visitDao.findById(id);
		
		model.addAttribute("doctor", visit.getDoctor());
		model.addAttribute("purpose", visit.getPurpose());
		model.addAttribute("date", visit.getDate());
		model.addAttribute("hour", visit.getHour());
		model.addAttribute("state", visit.getState());
		model.addAttribute("comment", visit.getComment());
		
		return "editvisit";
		
	 }
	
	@RequestMapping(value = "{role}/editvisit/{id}", method = RequestMethod.POST)
	 public String submitUpdatedVisit(@PathVariable int id, HttpServletRequest request, Model model)
	 {
                
		Visit visit = visitDao.findById(id);
		
		String purpose = request.getParameter("purpose");
		String date = request.getParameter("date");
		String hour = request.getParameter("hour");
		String state = request.getParameter("state");
		String comment = request.getParameter("comment");;
		
		visit.setAll(visit.getPatient(), visit.getDoctor(), purpose, date, hour, state);
		visit.setComment(comment);
		
		visitDao.save(visit);
		visitService.updateVisit(id, visit);
		
		return "redirect:/{role}/visitinfo/{id}";
		
	 }
	
	@RequestMapping(value = "/dbstate")
	 public String dbState(Model model)
	 {

		List<Visit> visit = visitDao.findAll();
		List<Patient> patient = patientDao.findAll();
		
		model.addAttribute("patient", patient);	
		model.addAttribute("visit", visit);	
	/*	model.addAttribute("doctor", visit.getDoctor());
		model.addAttribute("purpose", visit.getPurpose());
		model.addAttribute("date", visit.getDate());
		model.addAttribute("hour", visit.getHour());*/
				
		
		return "dbstate";
		
	 }
	
	
	@RequestMapping(value = "{role}/patientlist", method = RequestMethod.GET)
	 public String showPatientList(@PathVariable String role, Model model)
	 {
		List<Patient> patient = patientDao.findAll();
		
		
		model.addAttribute("patients", patient);
		model.addAttribute("role", role);
		
		
		 return "patientlist";
	 }
	
	@RequestMapping(value = "{role}/visitinfo/{id}", method = RequestMethod.GET)
	 public String visitInfo(@PathVariable String role, @PathVariable int id, Model model)
	 {

		Visit visit = visitDao.findById(id);
		
		model.addAttribute("visit", visit);
		model.addAttribute("role", role);
		
		return "visitinfo";
		
	 }
	
	@RequestMapping(value = "user/cancelvisit/{id}", method = RequestMethod.GET)
	 public String visitInfo(@PathVariable int id, Model model)
	 {

		Visit visit = visitDao.findById(id);
		Patient patient = visit.getPatient();
		
		visit.setState("Anulowano");
		visitDao.save(visit);
		
		return "redirect:/user/yourvisits="+patient.getId();
		
	 }
	
	@RequestMapping(value = "{role}/patientinfo/{id}", method = RequestMethod.GET)
	 public String patientInfo(@PathVariable String role, @PathVariable int id, Model model)
	 {

		Patient patient = patientDao.findById(id);
		
		model.addAttribute("patient", patient);
		
		return "patientinfo";
		
	 }
	
	
	@RequestMapping(value = "admin/doctorlist", method = RequestMethod.GET)
	 public String showDoctorList(Model model)
	 {
		List<Doctor> doctor = doctorDao.findAll();
		model.addAttribute("doctors", doctor);
		
		
		 return "doctorlist";
	 }
	
	@RequestMapping(value = "doctor/doctorlist", method = RequestMethod.GET)
	 public String showDoctor(Model model)
	 {
		
		
		 return "redirect:/doctor/doctoroneinfo";
	 }
	
	@RequestMapping(value = "doctor/doctoroneinfo", method = RequestMethod.GET)
	 public String doctorInfo(Model model)
	 {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username = ((UserDetails)principal).getUsername();
		UserP user=userDao.findByMail(username);
		Doctor doctor = user.getDoctoracc();
		
		model.addAttribute("doctor", doctor);
		
		return "doctoroneinfo";
		
	 }
	
	@RequestMapping(value = "admin/doctorinfo/{id}", method = RequestMethod.GET)
	 public String adminDoctorInfo(@PathVariable int id, Model model)
	 {

		Doctor doctor = doctorDao.findById(id);
		
		model.addAttribute("doctor", doctor);
		
		return "doctorinfo";
		
	 }
	
	@RequestMapping(value = "admin/adddoctor", method = RequestMethod.GET)
	 public String addDoctor()
	 {
		
		
		return "adddoctor";
		
	 }
	
	@RequestMapping(value = "admin/adddoctor", method = RequestMethod.POST)
	 public String addDoctor(HttpServletRequest request, Model model)
	 {

		String fname = request.getParameter("docfname");
		String lname = request.getParameter("doclname");
		String mail = request.getParameter("docmail");
		String phone = request.getParameter("docphone");
		
		Doctor doctor = new Doctor(fname, lname, mail, phone);
		doctorDao.save(doctor);
		
		return "redirect:../admin/doctorinfo/"+doctor.getId();
		
	 }
	
	@RequestMapping(value = "admin/editdoctor/{id}", method = RequestMethod.GET)
	 public String editDoctor(@PathVariable int id, Model model)
	 {
		Doctor doctor = doctorDao.findById(id);
		
		model.addAttribute("doctor", doctor);
		
		return "editdoctor";
		
	 }
	
	@RequestMapping(value = "doctor/editmydata/{id}", method = RequestMethod.GET)
	 public String editMyDataDoc(@PathVariable int id, Model model)
	 {
		Doctor doctor = doctorDao.findById(id);
		
		model.addAttribute("doctor", doctor);
		
		return "editdoctor";
		
	 }
	
	@RequestMapping(value = "doctor/editmydata/{id}", method = RequestMethod.POST)
	 public String editMyDataDoc(@PathVariable int id, HttpServletRequest request, Model model)
	 {

		Doctor doctor = doctorDao.findById(id);
		
		String fname = request.getParameter("docfname");
		String lname = request.getParameter("doclname");
		String mail = request.getParameter("docmail");
		String phone = request.getParameter("docphone");
		
		doctor.setAll(fname, lname, mail, phone);
		doctorDao.save(doctor);
		
		return "redirect:/doctor/doctoroneinfo";
		
	 }
	
	@RequestMapping(value = "admin/editdoctor/{id}", method = RequestMethod.POST)
	 public String editDoctor(@PathVariable int id, HttpServletRequest request, Model model)
	 {

		Doctor doctor = doctorDao.findById(id);
		
		String fname = request.getParameter("docfname");
		String lname = request.getParameter("doclname");
		String mail = request.getParameter("docmail");
		String phone = request.getParameter("docphone");
		
		doctor.setAll(fname, lname, mail, phone);
		doctorDao.save(doctor);
		
		return "redirect:/admin/doctorinfo/"+doctor.getId();
		
	 }
	
	@RequestMapping(value = "admin/deletedoctor/{id}", method = RequestMethod.GET)
	 public String deleteDoctor(@PathVariable int id, HttpServletRequest request, Model model)
	 {
		Doctor doctor = doctorDao.findById(id);
		
		List<Visit> visitDocList = new ArrayList<>(Arrays.asList());
		List<Visit> visit = visitDao.findAll();
		
		for (int i=0; i < visit.size(); i++){
			Visit t = visit.get(i);
			if (t.getDoctor().getId() == doctor.getId()) {
				visitDocList.add(t);
			}
		}
		
		if (!visitDocList.equals(null))
		{
			for (int i=0; i < visitDocList.size(); i++){
				Visit t = visitDocList.get(i);
				visitDao.deleteById(t.getId());
				
			}
		}
		
		try {
			doctorDao.deleteById(doctor.getId());
		} catch (ConstraintViolationException e) {
		    return "redirect:../doctorlist";
		}
		
		
		return "redirect:../doctorlist";
		
	 }
	
	@RequestMapping(value = "{role}/addvisit", method = RequestMethod.GET)
	 public String addVisit(@PathVariable String role, Model model)
	 {
		System.out.println(role);
		
		if (role.equals("doctor"))
		{
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			String username = ((UserDetails)principal).getUsername();
			
		    Doctor doctor = doctorDao.findByMail(username);
		    
		    model.addAttribute("doctors", doctor);
		}
		else {
			List<Doctor> doctor = doctorDao.findAll();
			model.addAttribute("doctors", doctor);
		}
		
		
		
		List<Patient> patient = patientDao.findAll();
		model.addAttribute("patients", patient);
		
		return "addvisit";
		
	 }
	
	@RequestMapping(value = "{role}/addvisit", method = RequestMethod.POST)
	 public String addVisit(HttpServletRequest request, Model model)
	 {
		
		

		String doctorid = request.getParameter("doctor");
		String patientid = request.getParameter("patient");
		String purpose = request.getParameter("purpose");
		String date = request.getParameter("date");
		String hour = request.getParameter("hour");
		String state = "Zarejestrowano";
		int doctoridint = Integer.parseInt(doctorid);
		int patientidint = Integer.parseInt(patientid);
		
		Doctor doctor = doctorDao.findById(doctoridint);
		Patient patient = patientDao.findById(patientidint);
		
		Visit visit = new Visit(patient, doctor, purpose, date, hour, state);
		
		visitDao.save(visit);
		
		 return "redirect:/{role}/verifyvisits";
	 }
	
	
	@RequestMapping(value = "{role}/addpatient", method = RequestMethod.GET)
	 public String doctorAddPatient(@PathVariable String role, Model model)
	 {
		return "addpatient";
		
	 }
	
	@RequestMapping(value = "{role}/addpatient", method = RequestMethod.POST)
	 public String addPatientSubmit(@PathVariable String role, HttpServletRequest request, Model model)
	 {
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String mail = request.getParameter("mail");
		String phone = request.getParameter("phone");
		
		Patient patient = new Patient(fname, lname, mail, phone);
		patientDao.save(patient);

		 return "redirect:../{role}/patientlist";
	 }
	
	@RequestMapping(value = "{role}/editpatient/{id}", method = RequestMethod.GET)
	 public String editPatient(@PathVariable int id, Model model)
	 {
		Patient patient = patientDao.findById(id);
		
		model.addAttribute("patient", patient);
		
		return "editpatient";
		
	 }
	
	@RequestMapping(value = "user/editmydata/{id}", method = RequestMethod.GET)
	 public String editMyData(@PathVariable int id, Model model)
	 {
		Patient patient = patientDao.findById(id);
		
		model.addAttribute("patient", patient);
		
		return "editpatient";
		
	 }
	
	@RequestMapping(value = "user/editmydata/{id}", method = RequestMethod.POST)
	 public String editMyData(@PathVariable int id, HttpServletRequest request, Model model)
	 {

		Patient patient = patientDao.findById(id);
		
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String mail = request.getParameter("mail");
		String phone = request.getParameter("phone");
		
		patient.setAll(fname, lname, mail, phone);
		patientDao.save(patient);
		
		return "redirect:../yourdata="+patient.getId();
		
	 }
	
	@RequestMapping(value = "{role}/editpatient/{id}", method = RequestMethod.POST)
	 public String editPatient(@PathVariable int id, HttpServletRequest request, Model model)
	 {

		Patient patient = patientDao.findById(id);
		
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String mail = request.getParameter("mail");
		String phone = request.getParameter("phone");
		
		patient.setAll(fname, lname, mail, phone);
		patientDao.save(patient);
		
		return "redirect:../patientinfo/"+patient.getId();
		
	 }
	
	@RequestMapping(value = "{role}/deletepatient/{id}", method = RequestMethod.GET)
	 public String deletePatient(@PathVariable int id, HttpServletRequest request, Model model)
	 {
		Patient patient = patientDao.findById(id);
		
		patientDao.deleteById(patient.getId());
		
		return "redirect:../patientlist";
		
	 }
	
	@RequestMapping(value = "{role}/deletevisit/{id}", method = RequestMethod.GET)
	 public String deleteVisit(@PathVariable int id, HttpServletRequest request, Model model)
	 {
		Visit visit = visitDao.findById(id);
		
		visitDao.deleteById(visit.getId());
		
		return "redirect:../verifyvisits";
		
	 }
	
	@RequestMapping(value = "doctor/verifyvisits", method = RequestMethod.GET)
	 public String showDocVisits(Model model)
	 {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String username = ((UserDetails)principal).getUsername();
		 UserP user=userDao.findByMail(username);

	    Doctor doctor = user.getDoctoracc();
		
		List<Visit> visitDocList = new ArrayList<>(Arrays.asList());
		List<Visit> visit = visitDao.findAll();
		
		for (int i=0; i < visit.size(); i++){
			Visit t = visit.get(i);
			if (t.getDoctor().getId() == doctor.getId()) {
				visitDocList.add(t);
			}
		}

		model.addAttribute("visits", visitDocList);
		model.addAttribute("role", "doctor");
		
		
		 return "verifyvisits";
	 }
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	 public String registerPatient()
	 {
		
		return "register";
		
	 }
	
	@RequestMapping(value = "/register?={errorcode}", method = RequestMethod.GET)
	 public String registerPatient(@PathVariable String errorcode)
	 {
		
		return "register";
		
	 }
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	 public String registerPatient(HttpServletRequest request, Model model)
	 {
		
		String mail = request.getParameter("mail");
		String password = request.getParameter("password");
		String repassword = request.getParameter("repassword");
		
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String phone = request.getParameter("phone");
		
		UserP user = userDao.findByMail(mail);
        if (user == null) {
    		if (password.equals(repassword))
    		{
    			String encpass = passwordEncoder.encode(password);
    			Patient patient = new Patient(fname, lname, mail, phone);
    			//user = new UserP(encpass, mail, patient);
    			patientDao.save(patient);
    			user = new UserP(encpass, mail, "ROLE_USER", patientDao.findById(patient.getId()), null);
    			userDao.save(user);
    			return "index";
    		}

    		else
    		{
    			//hasla sie nie zgadzaja
    			return "redirect:/register?=pswdnotmatch";
    		}
        }
        else
        {
        	//mail istnieje w bazie
        	return "redirect:/register?=mailexists";
        }
		
	 }
	
	@RequestMapping(value = "/user/home", method = RequestMethod.GET)
	 public String homePatient(Model model)
	 {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		

		if (principal instanceof UserDetails) {
		  String username = ((UserDetails)principal).getUsername();
		  UserP user=userDao.findByMail(username);
		  model.addAttribute("patient", user.getPatientacc());	
   	} else {
		
		}
		
		
		return "usermain";
		
	 }
	
	@RequestMapping(value = "/doctor/home", method = RequestMethod.GET)
	 public String homeDoctor(Model model)
	 {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		

		if (principal instanceof UserDetails) {
		  String username = ((UserDetails)principal).getUsername();
		  UserP user=userDao.findByMail(username);
		  model.addAttribute("doctor", user.getDoctoracc());
  	} else {
		
		}
		
		
		return "doctormain";
		
	 }
	
	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	 public String homeAdmin(Model model)
	 {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		

		if (principal instanceof UserDetails) {
		  String username = ((UserDetails)principal).getUsername();
		  UserP user=userDao.findByMail(username);
		  model.addAttribute("username", user.getName());	
 	} else {
		
		}
		
		
		return "adminmain";
		
	 }
	
	
	@RequestMapping(value = "/user/yourvisits={patid}", method = RequestMethod.GET)
	 public String showPatVisits(@PathVariable int patid, Model model)
	 {
		List<Visit> visitPatList = new ArrayList<>(Arrays.asList());
		List<Visit> visit = visitDao.findAll();
		
		for (int i=0; i < visit.size(); i++){
			Visit t = visit.get(i);
			if (t.getPatient().getId() == patid) {
				visitPatList.add(t);
			}
		}

		model.addAttribute("visits", visitPatList);
		
		
		 return "verifyvisits";
	 }
	
	@RequestMapping(value = "/user/addvisit", method = RequestMethod.GET)
	 public String userAddVisit(Model model)
	 {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String username = ((UserDetails)principal).getUsername();
		
	    Patient patient = patientDao.findByMail(username);
		 
		List<Doctor> doctor = doctorDao.findAll();
		model.addAttribute("doctors", doctor);
		
		List<Patient> patientl = Arrays.asList(patient);
		model.addAttribute("patients", patientl);
		
		 return "addvisit";
	 }
	

	
	@RequestMapping(value = "user/addvisit", method = RequestMethod.POST)
	 public String userAddVisit(HttpServletRequest request, Model model)
	 {
		

		String doctorid = request.getParameter("doctor");
		String patientid = request.getParameter("patient");
		String purpose = request.getParameter("purpose");
		String date = request.getParameter("date");
		String hour = request.getParameter("hour");
		String state = "Zarejestrowano";
		int doctoridint = Integer.parseInt(doctorid);
		int patientidint = Integer.parseInt(patientid);
		
		Doctor doctor = doctorDao.findById(doctoridint);
		Patient patient = patientDao.findById(patientidint);
		
		Visit visit = new Visit(patient, doctor, purpose, date, hour, state);
		
		visitDao.save(visit);
		
		 return "redirect:/user/yourvisits="+patient.getId();
	 }
	
	@RequestMapping(value = "user/yourdata={id}", method = RequestMethod.GET)
	 public String userPatientInfo(@PathVariable int id, Model model)
	 {

		Patient patient = patientDao.findById(id);
		
		model.addAttribute("patient", patient);
		
		return "patientinfo";
		
	 }
	
	@RequestMapping(value = "admin/accountlist", method = RequestMethod.GET)
	 public String showAccountsList(Model model)
	 {
		List<UserP> users = userDao.findAll();
		model.addAttribute("users", users);
		
		
		 return "accountlist";
	 }
	
	@RequestMapping(value = "admin/accountinfo/{id}", method = RequestMethod.GET)
	 public String userInfo(@PathVariable int id, Model model)
	 {

		UserP user = userDao.findById(id);
		String name = "";
		if (user.getRole().equals("ROLE_ADMIN"))
		{
			name=user.getName();
		}
		else if (user.getRole().equals("ROLE_DOCTOR"))
		{
			name=user.getDoctoracc().getFname()+" "+user.getDoctoracc().getLname();
		}
		else
		{
			name=user.getPatientacc().getFname()+" "+user.getPatientacc().getLname();
		}
		
		model.addAttribute("user", user);
		model.addAttribute("name", name);
		
		return "accountinfo";
		
	 }
	
	@RequestMapping(value = "admin/addaccount", method = RequestMethod.GET)
	 public String addAccount(Model model)
	 {
		List<Patient> patient = patientDao.findAll();
		model.addAttribute("patients", patient);
		
		List<Doctor> doctor = doctorDao.findAll();
		model.addAttribute("doctors", doctor);
		
		return "addaccount";
		
	 }
	
	@RequestMapping(value = "admin/addaccount", method = RequestMethod.POST)
	 public String addAccount(HttpServletRequest request, Model model)
	 {

		//user = new UserP(encpass, mail, patient);
		

		String mail = request.getParameter("accmail");
		String pass = request.getParameter("accpass");
		String rpass = request.getParameter("accrpass");
		String role = request.getParameter("accrole");
		String pid = request.getParameter("acclinkp");
		String did = request.getParameter("acclinkd");
		String name = request.getParameter("accname");
		Patient patient = null;
		Doctor doctor = null;
		
		
		if (!role.equals("ROLE_ADMIN"))
		{
			if (role.equals("ROLE_USER"))
			{
				if (!pid.equals("")) {
					int patientidint = Integer.parseInt(pid);
					patient = patientDao.findById(patientidint);
				}
				else
				{
					//przypisanie doktora do usera
					return "redirect:/admin/addaccount";
				}
			}
			else
			{
				if (!did.equals("")) {
					int doctoridint = Integer.parseInt(did);
					doctor = doctorDao.findById(doctoridint);
					patient = null;
				}
				else
				{
					//przypisanie usera do doktora
					return "redirect:/admin/addaccount";
				}
			}
			
		}
		
		
			
		if (pass.equals(rpass))
		{
			UserP user = new UserP(passwordEncoder.encode(pass), mail, role, patient, doctor);
			user.setName(name);
			userDao.save(user);
			
			return "redirect:/admin/accountinfo/"+user.getId();
		}
		else
		{
			return "redirect:/admin/addaccount";
		}
		
		
		
	 }
	
	
	@RequestMapping(value = "admin/accountedit/{id}", method = RequestMethod.GET)
	 public String getUserInfo(@PathVariable int id, Model model)
	 {
		UserP user = userDao.findById(id);
		List<Doctor> doctors = doctorDao.findAll();
		List<Patient> patients = patientDao.findAll();
		model.addAttribute("doctors", doctors);
		model.addAttribute("patients", patients);
		model.addAttribute("user", user);
		
		return "accountedit";
		
	 }
	
	
	@RequestMapping(value = "admin/accountedit/{id}", method = RequestMethod.POST)
	 public String postUserInfo(@PathVariable int id, HttpServletRequest request, Model model)
	 {

		UserP user = userDao.findById(id);
		

		String mail = request.getParameter("accmail");
		
		String pass = request.getParameter("accpass");
		String rpass = request.getParameter("accrpass");
		String role = request.getParameter("accrole");
		String pid = request.getParameter("acclinkp");
		String did = request.getParameter("acclinkd");
		Patient patient = null;
		Doctor doctor = null;

		if (!role.equals("ROLE_ADMIN"))
		{
			if (role.equals("ROLE_USER"))
			{
				if (!pid.equals("")) {
					int patientidint = Integer.parseInt(pid);
					patient = patientDao.findById(patientidint);
				}
				else
				{
					//przypisanie doktora do usera
					return "redirect:/admin/accountedit/"+user.getId();
				}
			}
			else
			{
				if (!did.equals("")) {
					int doctoridint = Integer.parseInt(did);
					doctor = doctorDao.findById(doctoridint);
					patient = null;
				}
				else
				{
					//przypisanie usera do doktora
					return "redirect:/admin/accountedit/"+user.getId();
				}
			}
			
		}
		
		if (pass.equals(""))
		{
			user.setMail(mail);
			user.setRole(role);
			user.setDoctoracc(doctor);
			user.setPatientacc(patient);
			userDao.save(user);
			
			return "redirect:/admin/accountinfo/"+user.getId();
		}
		else if (pass.equals(rpass))
		{
			user.setMail(mail);
			user.setPassword(passwordEncoder.encode(pass));
			user.setRole(role);
			user.setDoctoracc(doctor);
			user.setPatientacc(patient);
			userDao.save(user);
			
			return "redirect:/admin/accountinfo/"+user.getId();
		}
		else
		{
			return "redirect:/admin/accountedit/"+user.getId();
		}
		
		
		
	 }
	
	@RequestMapping(value = "admin/deleteaccount/{id}", method = RequestMethod.GET)
	 public String deleteAccount(@PathVariable int id, Model model)
	 {
		UserP user = userDao.deleteById(id);
		
		
		return "redirect:/admin/accountlist";
		
	 }
	
}
