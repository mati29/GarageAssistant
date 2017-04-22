package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mati on 2017-03-23.
 */
@Controller
public class RegistrationController {

    private ClientService clientService;
    private EmployeeService employeeService;
    private AdminService adminService;

    @Autowired
    public RegistrationController(ClientService clientService,EmployeeService employeeService,AdminService adminService) {
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.adminService = adminService;
    }

    @RequestMapping(value="/registration",method= RequestMethod.POST,params="userRegistrationAction")
    public String addClient(HttpServletRequest request, Client client, Account account) {
        clientService.registerClient(account,client,request.isUserInRole("ROLE_ADMIN"));
        return "redirect:/adminDashboard";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/registration",method= RequestMethod.POST,params="adminRegistrationAction=registerEmployee")
    public String addEmployee(Employee employee,Account account) {
        employeeService.registerEmployee(account,employee);
        return "redirect:/adminDashboard";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/registration",method= RequestMethod.POST,params="adminRegistrationAction=registerAdmin")
    public String addAdmin(Admin admin,Account account) {
        adminService.registerAdmin(account,admin);
        return "redirect:/adminDashboard";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/registration",method= RequestMethod.POST,params="adminConfirmAction=setEnable")
    public String setEnable(ListClient clients) {
        clientService.setClientsEnable(clients);
        return "redirect:/adminDashboard";
    }
}
