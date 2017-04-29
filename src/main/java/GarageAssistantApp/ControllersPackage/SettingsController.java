package main.java.GarageAssistantApp.ControllersPackage;

import main.java.GarageAssistantApp.EntityPackage.Settings;
import main.java.GarageAssistantApp.DTOPackage.ListDTO.ListClient;
import main.java.GarageAssistantApp.ServicesPackage.ClientService;
import main.java.GarageAssistantApp.ServicesPackage.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * Created by Mati on 2017-03-11.
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {

    private SettingsService settingsService;
    private ClientService clientService;

    @Autowired
    public SettingsController(SettingsService settingsService,ClientService clientService) {
        this.settingsService = settingsService;
        this.clientService = clientService;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String getSettings(Map<String, Object> model,Principal principal){
        model.put("setting",clientService.getSettingsFromUsername(principal.getName()));
        return "SettingsView";
    }

    @Secured("ROLE_USER")
    @RequestMapping(method= RequestMethod.POST,params="clientSettingsAction=setOption")
    public String setOption(@ModelAttribute("setting")Settings settings, Principal principal, HttpServletRequest request){
        settingsService.setOptionForUser(settings,clientService.getSettingsFromUsername(principal.getName()),request);
        return "redirect:/settings";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method= RequestMethod.POST,params="adminSettingsAction=setExtraRight")
    public String setExtraRight(@ModelAttribute("clients") ListClient clients) {
        clientService.setClientExtraRight(clients);
        return "redirect:/adminDashboard";
    }
}
