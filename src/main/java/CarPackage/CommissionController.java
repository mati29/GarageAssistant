package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    private EmployeeRepository employeeRepository;
    private RepairRepository repairRepository;

    @Autowired
    public CommissionController(AccountRepository accountRepository,CarRepository carRepository,ClientRepository clientRepository,CommissionRepository commissionRepository,EmployeeRepository employeeRepository,RepairRepository repairRepository) {
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.commissionRepository = commissionRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.repairRepository = repairRepository;
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

    @RequestMapping(value="/addCommission", method= RequestMethod.POST,params="clientAddAction=addCommission")
    public String addCommission(Car newCar,String description, Long employeeId,Principal principal ) {
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Client client = clientRepository.findOne(account.getClient().getId());
        Car car = carRepository.save(newCar);
        java.util.Date term = Calendar.getInstance().getTime();
        Commission newCommission = new Commission(client,car,term,description);
        commissionRepository.save(newCommission);
        if(employeeId!=1){
            Employee employeeToRepair = employeeRepository.findOne(employeeId);
            //employeeToRepair.addRepair()
            Repair newRepair = new Repair();
            newRepair.setEmployee(employeeToRepair);
            newRepair.setCommission(newCommission);
            repairRepository.save(newRepair);
        }
        return "redirect:/myCommission";
    }

   @RequestMapping(value="/addCommission",method= RequestMethod.GET)
    public String commissions(Map<String, Object> model) {
        Set<Employee> employees = employeeRepository.findByPost("mechanic");
        model.put("employees", employees);
        return "NewCommission";
    }

    @RequestMapping(/*value="/addCommission", */method= RequestMethod.POST,params="clientSelectAction=selectCommission")
    public String selectCommission(@Valid @ModelAttribute("commissionSended") Commission commission, BindingResult result, Principal principal , Model model) {
        //String username = principal.getName();
       // Account account = accountRepository.findByUsername(username);
        //Client client = clientRepository.findOne(account.getClient().getId());
        Commission singleCommission = commissionRepository.findOne(commission.getId());
        model.addAttribute("commission",singleCommission);
        return "CommissionSingleView";
    }

    @RequestMapping(method=RequestMethod.POST,params="clientBackAction=toMyCommission")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String backToCommission() {
        return "redirect:/myCommission";
    }

    @RequestMapping(method=RequestMethod.POST,params="clientBackAction=toMyDashboard")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String backToDashboard() {
        return "redirect:/clientDashboard";
    }


    @RequestMapping(method=RequestMethod.POST,params="clientAddAction")
    public String addNewCar(RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("from","overview");
        return "redirect:/myCommission/addCommission";
    }
}
