package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mati on 2016-12-18.
 */
@Controller
@RequestMapping("/myRepairs")
public class RepairController {
    //private CarRepository carRepository;//trzeba bedzie autowired user employee np.
    private AccountRepository accountRepository;
    private EmployeeRepository employeeRepository;
    private RepairRepository repairRepository;
    private CommissionRepository commissionRepository;
    @Autowired
    public RepairController(CarRepository carRepository,AccountRepository accountRepository,EmployeeRepository employeeRepository,RepairRepository repairRepository,CommissionRepository commissionRepository) {
        //this.carRepository = carRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.repairRepository = repairRepository;
        this.commissionRepository = commissionRepository;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String getRepairs(Map<String, Object> model, Principal principal) {
        String username = principal.getName();//to chyba autowired dać
        Account account = accountRepository.findByUsername(username);
        Employee employee = employeeRepository.findOne(account.getEmployee().getId());
        Set<Repair> repairSet = employee.getRepairSet();//repairRepository.findByEmployee(employee.getId());
        model.put("repairs",repairSet);
        //List<Car> cars = carRepository.findAll();
        //model.put("cars", cars);//rep nie car
        //return "CarView";
        return "RepairsView";
    }

    @RequestMapping(method=RequestMethod.POST,params="userBackAction")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String back() {
        return "redirect:/employeeDashboard";
    }

    @RequestMapping(value="/addRepair",method= RequestMethod.GET)
    public String repairs(Map<String, Object> model,Principal principal) {
        Set<Employee> employees = employeeRepository.findAll();
        model.put("employees", employees);
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Employee employee = employeeRepository.findOne(account.getEmployee().getId());
        Set<Repair> repairSet = employee.getRepairSet();
        model.put("repairs",repairSet);
        return "NewRepair";
    }

    @RequestMapping(value="/addRepair", method= RequestMethod.POST,params="employeeAddAction=addRepair")
    public String addRepair(Long commissionId,String description, Long employeeId,Principal principal) {
        Commission commissionNeedRepair = commissionRepository.findOne(commissionId);
        //if(employeeId!=1){inaczej gdy employee==1 admin przydzieli
        Employee employeeToRepair = employeeRepository.findOne(employeeId);
        Repair newRepair = new Repair(employeeToRepair,commissionNeedRepair,description);
        repairRepository.save(newRepair);
        return "redirect:/myRepairs";//zmienic tez od wejscia zaleznie
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSelectAction=selectRepair")
    public String selectCommission(@Valid @ModelAttribute("repairSended") Repair repair, BindingResult result, Principal principal , Model model) {
        //String username = principal.getName();
        // Account account = accountRepository.findByUsername(username);
        //Client client = clientRepository.findOne(account.getClient().getId());
        Repair singleRepair = repairRepository.findOne(repair.getId());
        model.addAttribute("repair",singleRepair);
        return "RepairSingleView";
    }

    @RequestMapping(method=RequestMethod.POST,params="employeeAddAction")
    public String addNewRepair(RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("from","overview");
        return "redirect:/myRepairs/addRepair";
    }

    @RequestMapping(method=RequestMethod.POST,params="employeeBackAction=toMyRepairs")
    public String backToRepairs() {
        return "redirect:/myRepairs";
    }

    @RequestMapping(method=RequestMethod.POST,params="employeeBackAction=toMyDashboard")
    public String backToDashboard() {
        return "redirect:/employeeDashboard";
    }
}
