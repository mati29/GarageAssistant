package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private AccountService accountService;
    private PasswordEncoder passwordEncoder;
    private RolesService rolesService;

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    @Autowired
    public void setRolesService(RolesService rolesService){
        this.rolesService = rolesService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> getAllWorkers(){
        return employeeRepository.findAll();
    }

    public void setAccount(Employee employee,Account account){
        employee.setAccount(account);
    }

    public void saveEmployee(Employee employee){
        employeeRepository.save(employee);
    }

    public List<Employee> getMechanicList(){
        return employeeRepository.findByPost("mechanic");
    }

    public void registerEmployee(Account account,Employee employee){
        accountService.setEnabled(account,true);
        accountService.setEmployee(account,employee);
        accountService.setPassword(account,passwordEncoder.encode(accountService.getPassword(account)));
        setAccount(employee,account);
        saveEmployee(employee);
        rolesService.saveRole(account,UserType.Employee.toString());
    }

    public List<Repair> getRepairsFromEmployee(Employee employee){
        return employee.getRepairList();
    }

    public Employee getEmployeeFromId(long id){
        return employeeRepository.findOne(id);
    }

    public long getEmployeeId(Employee employee){
        return employee.getId();
    }

    public boolean checkDefaultEmployee(Employee employee){
        return getEmployeeId(employee)==1L?true:false;
    }

    public Employee getDefaultEmployee(){
        return employeeRepository.findOne(1L);
    }
}
