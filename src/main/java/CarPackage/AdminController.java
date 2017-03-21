package main.java.CarPackage;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Mati on 2017-03-21.
 */
@Controller
@RequestMapping("/adminDashboard")
public class AdminController {

    @Secured("ROLE_ADMIN")
    @RequestMapping(method= RequestMethod.GET)
    public String getEmployeeDashboard(Model model) {
        return "AdminDashboard";
    }

}
