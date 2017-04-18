package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mati on 2017-03-11.
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {

    private ClientRepository clientRepository;
    private AccountRepository accountRepository;
    private RolesRepository rolesRepository;
    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsController(ClientRepository clientRepository,AccountRepository accountRepository,RolesRepository rolesRepository,SettingsRepository settingsRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.rolesRepository = rolesRepository;
        this.settingsRepository = settingsRepository;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String getSettings(Map<String, Object> model,Principal principal){
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Settings settings = account.getClient().getSettings();
        /*model.put("autoMechanic",settings.getAutoMechanic());
        model.put("autoPart",settings.getAutoPart());
        if(settings.getAdditionalService()==true)
            model.put("additionalService","true");
        else {
            model.put("additionalService","false");
            model.put("additionalServiceDemand", settings.getAdditionalServiceDemand());
        }
        if(settings.getCallExtraPart()==true)
            model.put("callExtraPart","true");
        else {
            model.put("callExtraPart","false");
            model.put("callExtraPartDemand", settings.getCallExtraPartDemand());
        }*/
        model.put("setting",settings);
        return "SettingsView";
    }

    @Secured("ROLE_USER")
    @RequestMapping(method= RequestMethod.POST,params="clientSettingsAction=setOption")
    public String setOption(@ModelAttribute("setting")Settings settings, Principal principal,HttpServletRequest request){
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Settings settingClient = account.getClient().getSettings();
        if(settings.getAdditionalServiceDemand()!=settingClient.getAdditionalServiceDemand()) {
            settingClient.setAdditionalServiceDemand(settings.getAdditionalServiceDemand());
            request.getSession().setAttribute("AS",settingClient.getAdditionalServiceDemand());
        }
        if(settings.getCallExtraPartDemand()!=settingClient.getCallExtraPartDemand()) {
            settingClient.setCallExtraPartDemand(settings.getCallExtraPartDemand());
            request.getSession().setAttribute("EP",settingClient.getCallExtraPartDemand());
        }
        if(settings.getAutoMechanic()!=settingClient.getAutoMechanic()) {
            settingClient.setAutoMechanic(settings.getAutoMechanic());
            request.getSession().setAttribute("AM",settingClient.getAutoMechanic());
        }
        if(settings.getAutoPart()!=settingClient.getAutoPart()) {
            settingClient.setAutoPart(settings.getAutoPart());
            request.getSession().setAttribute("AP",settingClient.getAutoPart());
        }
        settingsRepository.save(settingClient);
        return "redirect:/settings";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method= RequestMethod.POST,params="adminSettingsAction=setExtraRight")
    public String setExtraRight(@ModelAttribute("clients") ListClient clients/*, BindingResult bindingResult*/) {
        clients.getClientList().forEach(c->    {
                                            Client client = clientRepository.findOne(c.getId());
                                            if(client.getSettings().getAdditionalService())
                                                client.getSettings().setAdditionalServiceDemand(false);
                                            if(client.getSettings().getCallExtraPart())
                                                client.getSettings().setCallExtraPartDemand(false);
                                            clientRepository.save(client);
                                            });
        return "redirect:/adminDashboard";
    }
}
