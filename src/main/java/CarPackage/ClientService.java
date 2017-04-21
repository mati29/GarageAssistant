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

    @Autowired
    public ClientService(ClientRepository clientRepository,AccountService accountService){
        this.clientRepository = clientRepository;
        this.accountService = accountService;
    }

    public List<Client> getAllClient(){
        return clientRepository.findAll();
    }

    public ListClient getUnconfirmedClient(){
        List<Client> clients = new ArrayList<>();
        accountService.getUnconfirmedAccount().stream().forEach(a-> clients.add(a.getClient()));
        return new ListClient(clients);
    }
}
