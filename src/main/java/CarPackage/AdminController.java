package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
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
    @RequestMapping(method= {RequestMethod.GET,RequestMethod.POST})
    public String getEmployeeDashboard(Model model) {
        return "AdminDashboard";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=addExtraRight")
    public String addExtraRight(Model model) {
        List<Client> clients = clientRepository.findAll();//mozna by na ilosc kupic itd. rozwijanie
        clients = clients.stream().filter(c -> c.getSettings().getAdditionalServiceDemand() || c.getSettings().getCallExtraPartDemand()).collect(Collectors.toList());
        clients.sort((Client c1, Client c2)->(int)(c1.getId()-c2.getId()));
        ListClient clientsList = new ListClient();
        clientsList.setClientList(clients);
        model.addAttribute("clients",clientsList);
        return "ExtraRightPanel";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=setFreeRepair")
    public String setFreeCommission(Model model) {
        List<Repair> repairsToAssign = repairRepository.findByEmployeeId(1L);
        List<Employee> readyEmployees = employeeRepository.findByPost("mechanic");
        readyEmployees.sort((Employee e1, Employee e2)->(int)(e1.getId()-e2.getId()));
        ListRepair repairList = new ListRepair();
        repairList.setRepairList(repairsToAssign);
        model.addAttribute("repairs", repairList);
        model.addAttribute("employees", readyEmployees);
        return "EmployeeAssignRepair";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=addAccount")
    public String addAccount(Model model) {
        model.addAttribute("addedByAdmin",true);
        return "Register";
        //return "ImageTry";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=confirmAccount")
    public String confirmAccount(Model model) {
        List<Account> accounts = accountRepository.findByEnabled(false);
        List<Client> clients = new ArrayList<>();
        ListClient clientList = new ListClient();
        accounts.stream().forEach(a-> clients.add(a.getClient()));
        clients.sort((Client c1, Client c2)->(int)(c1.getId()-c2.getId()));
        clientList.setClientList(clients);
        model.addAttribute("clients",clientList);
        return "ConfirmPanel";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=calculateCommission")
    public String calculateCommission(Model model) {
        List<Commission> allCommission = commissionRepository.findAll();
        List<Commission> completedCommission = new ArrayList<>();
        allCommission.forEach(c -> {                                                                                                                                      //just for test delete after release version
            if(c.getRepairSet().stream().filter(r -> r.getAccomplish() == true).count() == c.getRepairSet().stream().count() && (null== c.getBill() || c.getAfterCheck()) && !c.getRepairSet().isEmpty())
                completedCommission.add(c);
        });
        completedCommission.sort((Commission c1, Commission c2)->(int)(c1.getId()-c2.getId()));
        model.addAttribute("commissions",completedCommission);
        return "WriteBillOut";
    }
    //załatwi service będzie jeden ciul
    @RequestMapping(value="/toBill",method = RequestMethod.GET)
    public String getCommissionToCalculate(Model model) {
        List<Commission> allCommission = commissionRepository.findAll();
        List<Commission> completedCommission = new ArrayList<>();
        allCommission.forEach(c -> {                                                                                                                                      //just for test delete after release version
            if(c.getRepairSet().stream().filter(r -> r.getAccomplish() == true).count() == c.getRepairSet().stream().count() && (null== c.getBill() || c.getAfterCheck()) && !c.getRepairSet().isEmpty())
                completedCommission.add(c);
        });
        completedCommission.sort((Commission c1, Commission c2)->(int)(c1.getId()-c2.getId()));
        model.addAttribute("commissions",completedCommission);
        return "WriteBillOut";
    }

}
