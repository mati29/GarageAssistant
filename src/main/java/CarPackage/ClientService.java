package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class ClientService {

    ClientRepository clientRepository;
    AccountService accountService;
    SettingsService settingsService;

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

    public void saveClient(Client client){
        clientRepository.save(client);
    }

}
