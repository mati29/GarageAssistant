package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mati on 2017-03-21.
 */
@Controller
@RequestMapping("/adminDashboard")
public class AdminController {

    private AccountRepository accountRepository;
    private EmployeeRepository employeeRepository;
    private RepairRepository repairRepository;
    private CommissionRepository commissionRepository;
    private PartRepository partRepository;
    private StoreRepository storeRepository;
    private ClientRepository clientRepository;

    @Autowired
    public AdminController(CarRepository carRepository,AccountRepository accountRepository,EmployeeRepository employeeRepository,RepairRepository repairRepository,CommissionRepository commissionRepository,PartRepository partRepository,StoreRepository storeRepository,ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.repairRepository = repairRepository;
        this.commissionRepository = commissionRepository;
        this.partRepository = partRepository;
        this.storeRepository = storeRepository;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method= RequestMethod.GET)
    public String getEmployeeDashboard(Model model) {
        return "AdminDashboard";
    }

    @RequestMapping(method= RequestMethod.POST,params="adminAction=addExtraRight")
    public String addExtraRight(Model model) {
        List<Client> clients = clientRepository.findAll();//mozna by na ilosc kupic itd. rozwijanie
        clients = clients.stream().filter(c -> c.getSettings().getAdditionalServiceDemand() || c.getSettings().getCallExtraPartDemand()).collect(Collectors.toList());
        ListClient clientsList = new ListClient();
        clientsList.setClientList(clients);
        model.addAttribute("clients",clientsList);
        return "ExtraRightPanel";
    }

    @RequestMapping(method= RequestMethod.POST,params="adminAction=setFreeRepair")
    public String setFreeCommission(Model model) {
        List<Repair> repairsToAssign = repairRepository.findByEmployeeId(1L);
        Set<Employee> readyEmployees = employeeRepository.findByPost("mechanic");
        ListRepair repairList = new ListRepair();
        repairList.setRepairList(repairsToAssign);
        model.addAttribute("repairs", repairList);
        model.addAttribute("employees", readyEmployees);
        return "EmployeeAssignRepair";
    }

    @RequestMapping(method= RequestMethod.POST,params="adminAction=addAccount")
    public String addAccount(Model model) {
        model.addAttribute("addedByAdmin",true);
        return "Register";
    }

}
