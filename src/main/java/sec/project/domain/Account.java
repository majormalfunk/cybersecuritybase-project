package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Account extends AbstractPersistable<Long> {

    public static final String ADMINISTRATOR = "ADMIN";
    public static final String USER = "USER";
    
    @Column(unique = true)
    private String username;
    private String password;
    private boolean administrator;

    public Account() {
        super();
    }
    
    public Account(String username, String password, boolean administrator) {
        this.username = username;
        this.password = password;
        this.administrator = administrator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

}
