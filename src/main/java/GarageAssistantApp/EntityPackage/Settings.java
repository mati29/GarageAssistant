package main.java.GarageAssistantApp.EntityPackage;

import main.java.GarageAssistantApp.EntityPackage.Client;

import javax.persistence.*;

/**
 * Created by Mati on 2017-03-11.
 */
@Entity(name="settings")
public class Settings{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="settings_id_seq")
    @SequenceGenerator(name="settings_id_seq", sequenceName="settings_id_seq", allocationSize=1)

    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private boolean autoMechanic = false;
    private boolean autoPart = false;
    //Client set this demand and when pay admin set additional service
    //attend to this service admin need to have some alert when log, client ABC want to...
    private boolean additionalServiceDemand= false;
    private boolean callExtraPartDemand =false;

    //Admin set this rights when client buy some additional effort
    private boolean additionalService = false;//add new repair by client to commission after job done
    private boolean callExtraPart =false;//add extra part to repair

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setAutoMechanic(boolean autoMechanic){
        this.autoMechanic = autoMechanic;
    }

    public boolean getAutoMechanic(){
        return this.autoMechanic;
    }

    public void setAutoPart(boolean autoPart){
        this.autoPart = autoPart;
    }

    public boolean getAutoPart(){
        return this.autoPart;
    }

    public void setAdditionalServiceDemand(boolean additionalServiceDemand){
        this.additionalServiceDemand = additionalServiceDemand;
    }

    public boolean getAdditionalServiceDemand(){
        return this.additionalServiceDemand;
    }

    public void setCallExtraPartDemand(boolean callExtraPartDemand){
        this.callExtraPartDemand= callExtraPartDemand;
    }

    public boolean getCallExtraPartDemand(){
        return this.callExtraPartDemand;
    }

    public void setAdditionalService(boolean additionalService){
        this.additionalService = additionalService;
    }

    public boolean getAdditionalService(){
        return this.additionalService;
    }

    public void setCallExtraPart(boolean callExtraPart){
        this.callExtraPart = callExtraPart;
    }

    public boolean getCallExtraPart(){
        return this.callExtraPart;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public Client getClient(){
        return this.client;
    }




}
