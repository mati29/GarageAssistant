package main.java.GarageAssistantApp.ServicesPackage;

import main.java.GarageAssistantApp.DTOPackage.ListDTO.ListClient;
import main.java.GarageAssistantApp.EntityPackage.Account;
import main.java.GarageAssistantApp.EntityPackage.Admin;
import main.java.GarageAssistantApp.RepositoriesPackage.AdminRepository;
import main.java.GarageAssistantApp.StandardPackage.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class AdminService {

    private ClientService clientService;
    private AdminRepository adminRepository;
    private AccountService accountService;
    private PasswordEncoder passwordEncoder;
    private RolesService rolesService;
    private SettingsService settingsService;

    @Autowired
    public void setClientService(ClientService clientService){
        this.clientService = clientService;
    }

    @Autowired
    public void setAdminRepository(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    @Autowired
    public void setRolesService(RolesService rolesService){
        this.rolesService = rolesService;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService){
        this.settingsService = settingsService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public ListClient getExtraRightClient(){
        return new ListClient(
                new ArrayList<>(
                        clientService
                                .getAllClient()
                                .stream()
                                .filter(c ->
                                            settingsService.getAdditionalServiceDemandFromSettings(clientService.getSettingsFromClient(c))
                                         || settingsService.getCallExtraPartDemandFromSettings(clientService.getSettingsFromClient(c))
                                         || (!settingsService.getAdditionalServiceDemandFromSettings(clientService.getSettingsFromClient(c)) && settingsService.getAdditionalServiceFromSettings(clientService.getSettingsFromClient(c)))
                                         || (!settingsService.getCallExtraPartDemandFromSettings(clientService.getSettingsFromClient(c)) && settingsService.getCallExtraPartFromSettings(clientService.getSettingsFromClient(c)))
                                )
                                .collect(Collectors.toList())
                                )
                             );
    }

    public void setAccount(Admin admin, Account account){
        admin.setAccount(account);
    }

    public void saveAdmin(Admin admin){
        adminRepository.save(admin);
    }

    public void registerAdmin(Account account,Admin admin){
        accountService.setEnabled(account,true);
        accountService.setAdmin(account,admin);
        accountService.setPassword(account,passwordEncoder.encode(accountService.getPassword(account)));
        setAccount(admin,account);
        saveAdmin(admin);
        rolesService.saveRole(account, UserType.Admin.toString());
    }

}
