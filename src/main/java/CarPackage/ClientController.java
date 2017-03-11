package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

/**
 * Created by Mati on 2016-11-01.
 */
@Controller
public class ClientController {

    private ClientRepository clientRepository;//trzeba bedzie autowired user employee np.
    private AccountRepository accountRepository;
    private RolesRepository rolesRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository,AccountRepository accountRepository,RolesRepository rolesRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.rolesRepository = rolesRepository;
    }

    @RequestMapping(value="/registration",method= RequestMethod.POST,params="userRegistrationAction")
    public String addClient(Client client,Account account) {//ew.potem inne employee od role zalezne..
        Settings settings = new Settings();
        settings.setClient(client);
        account.setClient(client);
        client.setAccount(account);
        client.setSettings(settings);//new functionality
        clientRepository.save(client);
        rolesRepository.save(new Roles(account.getUsername()));
        return "redirect:/";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value="/clientDashboard",method=RequestMethod.GET)
    public String getClientDashboard(Model model) {
        return "ClientDashboard";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value="/clientDashboard",method=RequestMethod.POST, params="clientAction=addCommission")
    public String addCommission(RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("from","dashboard");
        return "redirect:/myCommission/addCommission";
    }

    @RequestMapping(value="/clientDashboard",method= RequestMethod.POST,params="clientAction=checkMyCommission")
    public String getCommission(Model model) {
        return "redirect:/myCommission";
    }

    @RequestMapping(value="/clientDashboard",method=RequestMethod.POST, params="clientAction=accountSettings")
    public String checkSettings(Model model) {
        return "redirect:/settings";
    }


}
