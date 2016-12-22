package main.java.CarPackage;

/**
 * Created by Mati on 2016-10-23.
 */
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/myCars")
public class CarController {
    private CarRepository carRepository;//trzeba bedzie autowired user employee np.
    private AccountRepository accountRepository;
    private EmployeeRepository employeeRepository;
    private RepairRepository repairRepository;
    @Autowired
    public CarController(CarRepository carRepository,AccountRepository accountRepository,EmployeeRepository employeeRepository,RepairRepository repairRepository) {
        this.carRepository = carRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.repairRepository = repairRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String getRepairs(Map<String, Object> model,Principal principal) {
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

    @RequestMapping(value="/addCar",method=RequestMethod.GET)
    public String addCar(Model model) {
        return "NewCar";
    }

    @RequestMapping(value="/addCar",method=RequestMethod.POST,params="userSaveAction")
    public String submit(Car car) {
        carRepository.save(car);
        return "redirect:/myCars";
    }

    @RequestMapping(value="/addCar",method=RequestMethod.POST,params="userBackAction=toOverview")
    public String backToCar() {
        return "redirect:/myCars";
    }

    @RequestMapping(value="/addCar",method=RequestMethod.POST,params="userBackAction=toDashboard")
    public String backToDashboard() {
        return "redirect:/employeeDashboard";
    }

    @RequestMapping(method=RequestMethod.POST,params="userAddAction")
    public String addNewCar(RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("from","overview");
        return "redirect:/myCars/addCar";
    }
    @RequestMapping(method=RequestMethod.POST,params="userBackAction")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String back() {
        return "redirect:/employeeDashboard";
    }
}
