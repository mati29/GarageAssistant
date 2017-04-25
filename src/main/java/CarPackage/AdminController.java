package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mati on 2017-03-21.
 */
@Controller
@RequestMapping("/adminDashboard")
public class AdminController {

    private AdminService adminService;
    private EmployeeService employeeService;
    private ClientService clientService;
    private RepairService repairService;
    private CommissionService commissionService;

    @Autowired
    public AdminController(AdminService adminService,EmployeeService employeeService,ClientService clientService,RepairService repairService,CommissionService commissionService) {
        this.adminService = adminService;
        this.employeeService = employeeService;
        this.clientService = clientService;
        this.repairService = repairService;
        this.commissionService = commissionService;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method= {RequestMethod.GET,RequestMethod.POST})
    public String getEmployeeDashboard() {
        return "AdminDashboard";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=addExtraRight")
    public String addExtraRight(Model model) {
        model.addAttribute("clients",adminService.getExtraRightClient());
        return "ExtraRightPanel";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=setFreeRepair")
    public String setFreeCommission(Model model) {
        model.addAttribute("repairs" , repairService.getReadyRepair());
        model.addAttribute("employees" , employeeService.getMechanicList());
        return "EmployeeAssignRepair";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=addAccount")
    public String addAccount(Model model) {
        model.addAttribute("addedByAdmin",true);
        return "Register";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=confirmAccount")
    public String confirmAccount(Model model) {
        model.addAttribute("clients" , clientService.getUnconfirmedClient());
        return "ConfirmPanel";
    }

    @RequestMapping(method = RequestMethod.POST,params="adminAction=calculateCommission")
    public String calculateCommission(Model model) {
        model.addAttribute("commissions",commissionService.getCompletedCommission());
        return "WriteBillOut";
    }

    @RequestMapping(value="/toBill",method = RequestMethod.GET)
    public String getCommissionToCalculate(Model model) {
        model.addAttribute("commissions",commissionService.getCompletedCommission());
        return "WriteBillOut";
    }

}
