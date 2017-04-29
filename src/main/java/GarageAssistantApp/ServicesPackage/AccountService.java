package main.java.GarageAssistantApp.ServicesPackage;

import main.java.GarageAssistantApp.EntityPackage.Account;
import main.java.GarageAssistantApp.EntityPackage.Admin;
import main.java.GarageAssistantApp.EntityPackage.Client;
import main.java.GarageAssistantApp.EntityPackage.Employee;
import main.java.GarageAssistantApp.RepositoriesPackage.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    public Employee getEmployeeFromAccount(Account account){
        return account.getEmployee();
    }

    public List<Account> getUnconfirmedAccount(){
        return accountRepository.findByEnabled(false);
    }

    public Account getAccountFromUsername(String username){
        return accountRepository.findByUsername(username);
    }

    public Client getClientFromAccount(Account account){
        return account.getClient();
    }

    public void setEnabled(Account account,boolean status){
        account.setEnabled(status);
    }

    public void setClient(Account account,Client client){
        account.setClient(client);
    }

    public void setEmployee(Account account,Employee employee){
        account.setEmployee(employee);
    }

    public void setAdmin(Account account,Admin admin){
        account.setAdmin(admin);
    }

    public void setPassword(Account account,String password){
        account.setPassword(password);
    }

    public String getPassword(Account account){
        return account.getPassword();
    }

    public String getUsernameFromAccount(Account account){
        return account.getUsername();
    }

    public boolean getEnabled(Account account){
        return account.getEnabled();
    }
}
