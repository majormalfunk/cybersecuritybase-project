package sec.project.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sec.project.domain.Account;
import static sec.project.domain.Account.*;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @RequestMapping("*")
    public String defaultMapping(Authentication authentication) {

        if (authentication == null) {
            System.out.println("Not authenticated");
            return "redirect:/login";
        }

        for (GrantedAuthority ga : authentication.getAuthorities()) {
            if (ga.getAuthority().equals(ADMINISTRATOR)) {
                return "redirect:/admin";
            }
        }

        Signup signup = signupRepository.findByUsername(authentication.getName());
        if (signup == null) {
            return "redirect:/form";
        }
        return "redirect:/done/" + signup.getUsername();
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {
        return "redirect:/";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String loadAdmin(Model model) {
        // What??? No authentication here?? Anybody could just
        // navigate to this URL and see sensitive user information.
        // Who designed this poor excuse for a piece of crap?
        List<Account> accounts = new ArrayList<>();
        accounts.addAll(accountRepository.findAll());
        model.addAttribute("accounts", accounts);
        return "admin";
    }

    @RequestMapping(value = "/updateuser/{username}", method = RequestMethod.POST)
    @ResponseBody
    public void updatePrivileges(@PathVariable String username, @RequestParam Integer isadmin) {
        // And again!?! No authentication at all? Whoever can gain elevation of privilege.
        // What were you thinking???
        Account account = accountRepository.findByUsername(username);
        account.setAdministrator(isadmin == 1);
        // You should at least log to file when administrator privileges are changed.
        // Why did you comment the following part out anyway?
        // System.out.println("Administrator privileges " + (isadmin == 1 ? "given to" : "taken from") + " user " + username);
        accountRepository.save(account);
    }

    @RequestMapping(value = "/newaccount", method = RequestMethod.POST)
    public String newAccount(Model model, @RequestParam String username, @RequestParam String password) {
        // And again!?! No authentication at all? Whoever can create new accounts.
        // You're clearly not thinking at all.
        List<String> errors = new ArrayList<>();
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            account = new Account(username, passwordEncoder.encode(password), false);
            accountRepository.save(account);
            return "redirect:/admin";
        }
        errors.add("Username already exists");
        model.addAttribute("errors", errors);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Authentication authentication,
            @RequestParam String name, @RequestParam String address, @RequestParam String bankAccount) {
        System.out.println("Signing up " + authentication.getName());
        signupRepository.save(new Signup(authentication.getName(), name, address, bankAccount));
        return "redirect:/done/" + authentication.getName();
    }

    @RequestMapping(value = "/done/{user}", method = RequestMethod.GET)
    public String showRegistration(Authentication authentication, Model model, @PathVariable String user) {
        // You're not checking that the user is who he claims he is???
        // You're exposing sensitive data here!!
        Account account = accountRepository.findByUsername(user);
        List<Signup> signups = new ArrayList<>();
        if (account.isAdministrator()) {
            signups.addAll(signupRepository.findAll());
        } else {
            Signup signup = signupRepository.findByUsername(user);
            if (signup == null) {
                System.out.println("No signup yet");
                return "redirect:/form";
            }
            signups.add(signup);
        }
        model.addAttribute("signups", signups);
        return "done";
    }

}
