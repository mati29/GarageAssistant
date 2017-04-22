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

    private SettingsService settingsService;
    private ClientService clientService;

    @Autowired
    public SettingsController(SettingsService settingsService,ClientService clientService,  ClientRepository clientRepository,AccountRepository accountRepository,RolesRepository rolesRepository,SettingsRepository settingsRepository) {
        this.settingsService = settingsService;
        this.clientService = clientService;

        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.rolesRepository = rolesRepository;
        this.settingsRepository = settingsRepository;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String getSettings(Map<String, Object> model,Principal principal){
        model.put("setting",clientService.getSettingsFromUsername(principal.getName()));
        return "SettingsView";
    }

    @Secured("ROLE_USER")
    @RequestMapping(method= RequestMethod.POST,params="clientSettingsAction=setOption")
    public String setOption(@ModelAttribute("setting")Settings settings, Principal principal,HttpServletRequest request){
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
