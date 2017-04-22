package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class AdminService {

    private ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService){
        this.clientService = clientService;
    }

    public ListClient getExtraRightClient(){
        return new ListClient(
                new ArrayList<>(
                        clientService
                                .getAllClient()
                                .stream()
                                .filter(c -> c.getSettings().getAdditionalServiceDemand() || c.getSettings().getCallExtraPartDemand())
                                .collect(Collectors.toList())
                                )
                             );
    }

}
