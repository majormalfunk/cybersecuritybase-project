package sec.project.config;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import static sec.project.domain.Account.*;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SignupRepository signupRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // We'll create an administrator account and some test users to have data
        // because we are using an in-memory database which would be always empty
        // at start up.
        
        // But you shouldn't allow weak or well-known passwords!
        Account administrator = new Account("admin", passwordEncoder.encode("admin"), true);
        accountRepository.save(administrator);
        Account donald = new Account("donald", passwordEncoder.encode("donald"), false);
        accountRepository.save(donald);
        Signup donaldSignup = new Signup("donald", "Donald Duck", "Paradise Drive 13", "313313-313");
        signupRepository.save(donaldSignup);
        Account daisy = new Account("daisy", passwordEncoder.encode("123456"), false);
        accountRepository.save(daisy);
        Signup daisySignup = new Signup("daisy", "Daisy Duck", "Elm Street 666", "666666-666");
        signupRepository.save(daisySignup);
        Account newbie = new Account("newbie", passwordEncoder.encode("password"), false);
        accountRepository.save(newbie);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority(account.isAdministrator()? ADMINISTRATOR : USER)));
    }
}
