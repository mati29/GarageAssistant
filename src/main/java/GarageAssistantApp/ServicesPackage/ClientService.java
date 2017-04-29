package main.java.GarageAssistantApp.ServicesPackage;

import main.java.GarageAssistantApp.EntityPackage.Account;
import main.java.GarageAssistantApp.EntityPackage.Client;
import main.java.GarageAssistantApp.EntityPackage.Commission;
import main.java.GarageAssistantApp.EntityPackage.Settings;
import main.java.GarageAssistantApp.DTOPackage.ListDTO.ListClient;
import main.java.GarageAssistantApp.RepositoriesPackage.ClientRepository;
import main.java.GarageAssistantApp.StandardPackage.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class ClientService {

    private ClientRepository clientRepository;
    private AccountService accountService;
    private SettingsService settingsService;
    private RolesService rolesService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setClientRepository(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService){
        this.settingsService = settingsService;
    }

    @Autowired
    public void setRolesService(RolesService rolesService){
        this.rolesService = rolesService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public String getClientFirstName(Client client){
        return client.getFirstName();
    }

    public String getClientLastName(Client client){
        return client.getLastName();
    }

    public String getClientFullName(Client client){
        return getClientLastName(client) + getClientFirstName(client);
    }

    public List<Client> getAllClient(){
        return clientRepository.findAll();
    }

    public ListClient getUnconfirmedClient(){
        List<Client> clients = new ArrayList<>();
        accountService.getUnconfirmedAccount().stream().forEach(a-> clients.add(a.getClient()));
        return new ListClient(clients);
    }

    public Settings getSettingsFromUsername(String username){
        return getSettingsFromClient(accountService.getClientFromAccount(accountService.getAccountFromUsername(username)));
    }

    public Settings getSettingsFromClient(Client client){
        return client.getSettings();
    }

    public void setClientExtraRight(ListClient listClient){
        getClientsFromList(listClient)
                .forEach
                        (c->
                                {
                                    Client client = getClientFromId(c.getId());
                                    settingsService.rechangeDemandSettings(getSettingsFromClient(client));
                                    saveClient(client);
                                }
                        );
    }

    public Client getClientFromId(long id){
        return clientRepository.findOne(id);
    }

    public List<Client> getClientsFromList(ListClient listClient){
        return listClient.getClientList();
    }

    public List<Commission> getCommissionFromClient(Client client){
        return client.getCommissionList();
    }

    public void saveClient(Client client){
        clientRepository.save(client);
    }

    public void setAccount(Client client,Account account){
        client.setAccount(account);
    }

    public void setSettings(Client client,Settings settings){
        client.setSettings(settings);
    }

    public Account getAccountFromClient(Client client){
        return client.getAccount();
    }

    public void registerClient(Account account,Client client,boolean createByAdmin){
        if(createByAdmin)
            accountService.setEnabled(account,true);
        Settings settings = new Settings();
        settingsService.setClient(settings,client);
        accountService.setClient(account,client);
        accountService.setPassword(account,passwordEncoder.encode(accountService.getPassword(account)));
        setAccount(client,account);
        setSettings(client,settings);
        saveClient(client);
        rolesService.saveRole(account, UserType.Client.toString());
    }

    public void setClientsEnable(ListClient listClient){
        getClientsFromList(listClient)
                .stream()
                .filter(c -> accountService.getEnabled(getAccountFromClient(c))==true)
                .forEach(c -> {
                                Client client = getClientFromId(c.getId());
                                accountService.setEnabled(getAccountFromClient(client),true);
                                saveClient(client);
                              }
                        );
    }

}
