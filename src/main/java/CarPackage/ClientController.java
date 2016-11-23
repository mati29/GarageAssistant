package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        Client clientRes = clientRepository.save(client);
        account.setClient(clientRes);
        accountRepository.save(account);
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
    public String addCar(Model model) {
        return "redirect:/myCommission/addCommission";
    }

    @RequestMapping(value="/clientDashboard",method= RequestMethod.POST,params="clientAction=checkMyCommission")
    public String getCommission(Model model) {
        return "redirect:/myCommission";
    }


}
