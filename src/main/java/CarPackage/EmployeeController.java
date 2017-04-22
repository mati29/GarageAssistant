package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Mati on 2016-10-29.
 */
@Controller
@RequestMapping("/employeeDashboard")
public class EmployeeController {

    @Secured("ROLE_WORKER")
    @RequestMapping(method=RequestMethod.GET)
    public String getEmployeeDashboard(Model model) {
        return "EmployeeDashboard";
    }

    @RequestMapping(method=RequestMethod.POST, params="employeeAction=checkMyRepairs")
    public String checkMyCars(Model model) {
        return "redirect:/myRepairs";
    }


    @RequestMapping(method=RequestMethod.POST, params="employeeAction=addRepair")
    public String addRepair(RedirectAttributes redirectAttrs,HttpServletRequest request) {
        redirectAttrs.addFlashAttribute("from","dashboard");
        request.getSession().setAttribute("from","dashboard");
        return "redirect:/myRepairs/addRepair";
    }

    @RequestMapping(method=RequestMethod.POST, params="employeeAction=addPart")
    public String addPartStore(RedirectAttributes redirectAttrs,HttpServletRequest request) {
        redirectAttrs.addFlashAttribute("from","dashboard");
        request.getSession().setAttribute("from","dashboard");
        return "redirect:/store/addPart";
    }


    @RequestMapping(method=RequestMethod.POST, params="employeeAction=checkStore")
    public String checkStore(Model model) {
        return "redirect:/store";
    }
}
