package main.java.CarPackage;

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

    public List<Account> getUnconfirmedAccount(){
        return accountRepository.findByEnabled(false);
    }

    public Account getAccountFromUsername(String username){
        return accountRepository.findByUsername(username);
    }

    public Client getClientFromAccount(Account account){
        return account.getClient();
    }
}
