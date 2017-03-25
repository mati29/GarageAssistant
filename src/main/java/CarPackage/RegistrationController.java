package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mati on 2017-03-23.
 */
@Controller
public class RegistrationController {

    private ClientRepository clientRepository;//trzeba bedzie autowired user employee np.
    private AccountRepository accountRepository;
    private RolesRepository rolesRepository;
    private EmployeeRepository employeeRepository;
    private AdminRepository adminRepository;

    @Autowired
    public RegistrationController(ClientRepository clientRepository,AccountRepository accountRepository,RolesRepository rolesRepository,EmployeeRepository employeeRepository,AdminRepository adminRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.rolesRepository = rolesRepository;
        this.employeeRepository = employeeRepository;
        this.adminRepository = adminRepository;
    }

    @RequestMapping(value="/registration",method= RequestMethod.POST,params="userRegistrationAction")
    public String addClient(HttpServletRequest request, Client client, Account account) {
        Settings settings = new Settings();
        settings.setClient(client);
        if (request.isUserInRole("ROLE_ADMIN")) {
            account.setEnabled(true);
        }
        account.setClient(client);
        client.setAccount(account);
        client.setSettings(settings);//new functionality
        clientRepository.save(client);
        rolesRepository.save(new Roles(account.getUsername(),UserType.Client.toString()));
        return "redirect:/";
    }

    @RequestMapping(value="/registration",method= RequestMethod.POST,params="adminRegistrationAction=registerEmployee")
    public String addEmployee(Employee employee,Account account) {
        account.setEnabled(true);
        account.setEmployee(employee);
        employee.setAccount(account);
        employeeRepository.save(employee);
        rolesRepository.save(new Roles(account.getUsername(),UserType.Employee.toString()));
        return "redirect:/";
    }

    @RequestMapping(value="/registration",method= RequestMethod.POST,params="adminRegistrationAction=registerAdmin")
    public String addAdmin(Admin admin,Account account) {
        account.setEnabled(true);
        account.setAdmin(admin);
        admin.setAccount(account);
        adminRepository.save(admin);
        rolesRepository.save(new Roles(account.getUsername(),UserType.Admin.toString()));
        return "redirect:/";
    }

    @RequestMapping(value="/registration",method= RequestMethod.POST,params="adminConfirmAction=setEnable")
    public String setEnable(ListClient clients) {
        clients.getClientList().stream().filter(c -> c.getAccount().getEnabled()==true)
                .forEach(c -> {Client client = clientRepository.findOne(c.getId());
                               client.getAccount().setEnabled(true);
                               clientRepository.save(client);});
        return "redirect:/";
    }
}