package pl.zdzymira.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import pl.zdzymira.model.dao.UserDao;
import pl.zdzymira.model.user.UserP;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	
    public UserDetails loadUserByUsername(String email)
    	      throws UsernameNotFoundException {
    	  
    	        UserP user = userDao.findByMail(email);
    	        
    	        if (user == null) {
    	            throw new UsernameNotFoundException(
    	              "Brak użytkownika z mailem: "+ email);
    	        }
    	        boolean enabled = true;
    	        boolean accountNonExpired = true;
    	        boolean credentialsNonExpired = true;
    	        boolean accountNonLocked = true;
    	        return new org.springframework.security.core.userdetails.User
    	          (user.getMail(), 
    	          user.getPassword(), enabled, accountNonExpired, 
    	          credentialsNonExpired, accountNonLocked, getAuthorities(Arrays.asList(user.getRole()))
    	          );
    	    }
    
    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
	
}
