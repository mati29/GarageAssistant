package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mati on 2016-12-18.
 */
@Controller
@RequestMapping("/myRepairs")
public class RepairController {
    private CarRepository carRepository;//trzeba bedzie autowired user employee np.
    private AccountRepository accountRepository;
    private EmployeeRepository employeeRepository;
    private RepairRepository repairRepository;
    @Autowired
    public RepairController(CarRepository carRepository,AccountRepository accountRepository,EmployeeRepository employeeRepository,RepairRepository repairRepository) {
        this.carRepository = carRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.repairRepository = repairRepository;
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
}
