package sec.project.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Signup extends AbstractPersistable<Long> {

    private String username;
    private String name;
    private String address;
    private String bankAccount;

    public Signup() {
        super();
    }

    public Signup(String username, String name, String address, String bankAccount) {
        this();
        this.username = username;
        this.name = name;
        this.address = address;
        this.bankAccount = bankAccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    /*
    public List<String> asList() {
        String[] details = {username, name, address, bankAccount};
        ArrayList<String> detailsAsList = new ArrayList<>();
        detailsAsList.addAll(Arrays.asList(details));
        return detailsAsList;
    }
    */
}
