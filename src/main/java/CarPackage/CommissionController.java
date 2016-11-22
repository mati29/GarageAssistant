package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mati on 2016-11-21.
 */
@Controller
@RequestMapping("/myCommission")
public class CommissionController {

    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private CommissionRepository commissionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public CommissionController(AccountRepository accountRepository,CarRepository carRepository,ClientRepository clientRepository,CommissionRepository commissionRepository) {
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.commissionRepository = commissionRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String getCommission(Map<String, Object> model, Principal principal){
        String username = principal.getName();//to chyba autowired dać
        Account account = accountRepository.findByUsername(username);
        Client client = clientRepository.findOne(account.getClient().getId());//to całe
        Set<Commission> clientCommissionSet = client.getCommissionSet();
        model.put("commissions", clientCommissionSet);
        return "CommissionsView";
    }

    @RequestMapping(value="/addCommission", method= RequestMethod.POST,params="userAddCommissionAction")
    public String addCommission(Car newCar, Principal principal ) {
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Client client = clientRepository.findOne(account.getClient().getId());
        Car car = carRepository.save(newCar);
        java.util.Date term = Calendar.getInstance().getTime();
        commissionRepository.save(new Commission(client,car,term));
        return "redirect:/myCommission/addCommission";
    }

    @RequestMapping(value="/addCommission",method= RequestMethod.GET)
    public String commissions(Model model) {
        return "NewCommission";
    }

    @RequestMapping(value="/addCommission", method= RequestMethod.POST,params="userSelectAction=selectCommission")
    public String selectCommission(Commission commission, Principal principal , Model model) {
        //String username = principal.getName();
       // Account account = accountRepository.findByUsername(username);
        //Client client = clientRepository.findOne(account.getClient().getId());
        Commission singleCommission = commissionRepository.findOne(commission.getId());
        model.addAttribute("commission",singleCommission);
        return "CommissionSingleView";
    }
}
