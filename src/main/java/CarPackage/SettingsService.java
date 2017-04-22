package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mati on 2017-04-22.
 */
@Service
public class SettingsService {

    private SettingsRepository settingsRepository;
    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    @Autowired
    public void setSettingsRepository(SettingsRepository settingsRepository){
        this.settingsRepository = settingsRepository;
    }

    public void setClient(Settings settings,Client client){
        settings.setClient(client);
    }


    public void setOptionForUser(Settings changedSettings,Settings clientSettings, HttpServletRequest request){
        if(getAdditionalServiceDemandFromSettings(changedSettings)!=getAdditionalServiceDemandFromSettings(clientSettings)) {
            setAdditionalServiceDemandForSettings(clientSettings,getAdditionalServiceDemandFromSettings(changedSettings));
            request.getSession().setAttribute("AS",getAdditionalServiceDemandFromSettings(clientSettings));
        }
        if(getCallExtraPartDemandFromSettings(changedSettings)!=getCallExtraPartDemandFromSettings(clientSettings)) {
            setCallExtraPartDemandForSettings(clientSettings,getCallExtraPartDemandFromSettings(changedSettings));
            request.getSession().setAttribute("EP",getCallExtraPartDemandFromSettings(clientSettings));
        }
        if(getAutoMechanicFromSettings(changedSettings)!=getAutoMechanicFromSettings(clientSettings)) {
            setAutoMechanicForSettings(clientSettings,getAutoMechanicFromSettings(changedSettings));
            request.getSession().setAttribute("AM",getAutoMechanicFromSettings(clientSettings));
        }
        if(getAutoPartFromSettings(changedSettings)!=getAutoPartFromSettings(clientSettings)) {
            setAutoPartForSettings(clientSettings,getAutoPartFromSettings(changedSettings));
            request.getSession().setAttribute("AP",getAutoPartFromSettings(clientSettings));
        }
        saveSettings(clientSettings);
    }

    public void rechangeDemandSettings(Settings settings){
        if(getAdditionalServiceFromSettings(settings))
            setAdditionalServiceDemandForSettings(settings,false);
        if(getCallExtraPartFromSettings(settings))
            setCallExtraPartDemandForSettings(settings,false);
    }

    public boolean getAutoPartFromSettings(Settings settings){
        return settings.getAutoPart();
    }

    public boolean getAutoMechanicFromSettings(Settings settings){
        return settings.getAutoMechanic();
    }

    public boolean getAdditionalServiceFromSettings(Settings settings){
        return settings.getAdditionalService();
    }

    public boolean getAdditionalServiceDemandFromSettings(Settings settings){
        return settings.getAdditionalServiceDemand();
    }

    public boolean getCallExtraPartFromSettings(Settings settings){
        return settings.getCallExtraPart();
    }

    public boolean getCallExtraPartDemandFromSettings(Settings settings){
        return settings.getCallExtraPartDemand();
    }

    public void setAutoPartForSettings(Settings settings,boolean status){
        settings.setAutoPart(status);
    }

    public void setAutoMechanicForSettings(Settings settings,boolean status){
        settings.setAutoMechanic(status);
    }

    public void setAdditionalServiceDemandForSettings(Settings settings,boolean status){
        settings.setAdditionalServiceDemand(status);
    }

    public void setAdditionalServiceForSettings(Settings settings,boolean status){
        settings.setAdditionalService(status);
    }


    public void setCallExtraPartDemandForSettings(Settings settings,boolean status){
        settings.setCallExtraPartDemand(status);
    }

    public void setCallExtraPartForSettings(Settings settings,boolean status){
        settings.setCallExtraPart(status);
    }

    public void saveSettings(Settings settings){
        settingsRepository.save(settings);
    }

}
