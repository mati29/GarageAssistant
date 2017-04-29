package main.java.GarageAssistantApp.ControllersPackage;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mati on 2016-11-01.
 */
@Controller
public class ClientController {

    @Secured("ROLE_USER")
    @RequestMapping(value="/clientDashboard",method={RequestMethod.GET,RequestMethod.POST})
    public String getClientDashboard() {
        return "ClientDashboard";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value="/clientDashboard",method=RequestMethod.POST, params="clientAction=addCommission")
    public String addCommission(RedirectAttributes redirectAttrs,HttpServletRequest request) {
        redirectAttrs.addFlashAttribute("from","dashboard");
        request.getSession().setAttribute("from","dashboard");
        return "redirect:/myCommission/addCommission";
    }

    @RequestMapping(value="/clientDashboard",method= RequestMethod.POST,params="clientAction=checkMyCommission")
    public String getCommission(Model model) {
        return "redirect:/myCommission";
    }

    @RequestMapping(value="/clientDashboard",method=RequestMethod.POST, params="clientAction=accountSettings")
    public String checkSettings(Model model) {
        return "redirect:/settings";
    }

    @RequestMapping(value="/clientDashboard",method=RequestMethod.POST, params="clientAction=specialCommission")
    public String specialCommission(Model model) {
        return "redirect:/myCommission/specialCommission";
    }

}
