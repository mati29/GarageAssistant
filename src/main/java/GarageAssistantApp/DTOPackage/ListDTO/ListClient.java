package main.java.GarageAssistantApp.DTOPackage.ListDTO;

import main.java.GarageAssistantApp.EntityPackage.Client;

import java.util.List;

/**
 * Created by Mati on 2017-03-22.
 */
public class ListClient {
    public List<Client> clientList;//na privaty czy bedzie

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public ListClient(){}

    public ListClient(List<Client> clientList){
        this.clientList = clientList;
    }
}
