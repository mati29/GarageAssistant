package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Mati on 2017-04-22.
 */
@Service
public class RolesService {

    private RolesRepository rolesRepository;
    private AccountService accountService;

    @Autowired
    public void setRolesRepository(RolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    public void saveRole(Account account,String role){
        rolesRepository.save(new Roles(accountService.getUsernameFromAccount(account),role));
    }

}
