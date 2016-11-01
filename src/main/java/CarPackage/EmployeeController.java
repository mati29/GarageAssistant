package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;

/**
 * Created by Mati on 2016-10-29.
 */
@Controller
@RequestMapping("/employeeDashboard")
public class EmployeeController {

    //@Autowired
    //public CarController(CarRepository carRepository) {
        //this.carRepository = carRepository;
    //} potem bedzie autowired ale user

    @RequestMapping(method= RequestMethod.GET)
    public String getEmployeeDashboard(Model model) {
        return "EmployeeDashboard";
    }

    @RequestMapping(method=RequestMethod.POST, params="employeeAction=checkMyCars")
    public String checkMyCars(Model model) {
        return "redirect:/myCars";
    }


    @RequestMapping(method=RequestMethod.POST, params="employeeAction=addCar")
    public String addCar(RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("from","dashboard");
        return "redirect:/myCars/addCar";
    }

    @RequestMapping(method=RequestMethod.POST, params="employeeAction=archiveCars")
    public String archiveCars(Model model) {
        return "redirect:/";
    }


    @RequestMapping(method=RequestMethod.POST, params="employeeAction=checkStore")
    public String checkStore(Model model) {
        return "redirect:/";
    }
}
