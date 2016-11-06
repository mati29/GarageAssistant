package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Mati on 2016-11-01.
 */
@Controller
public class ClientController {

    private ClientRepository clientRepository;//trzeba bedzie autowired user employee np.
    private AccountRepository accountRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository,AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value="/registration",method= RequestMethod.POST,params="userRegistrationAction")
    public String addClient(Client client,Account account) {//ew.potem inne employee od role zalezne..
        Client clientRes = clientRepository.save(client);
        account.setClient(clientRes);
        accountRepository.save(account);
        return "redirect:/";
    }

}
