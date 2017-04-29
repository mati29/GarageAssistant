package main.java.GarageAssistantApp.ControllersPackage;

import main.java.GarageAssistantApp.EntityPackage.Commission;
import main.java.GarageAssistantApp.ServicesPackage.BillService;
import main.java.GarageAssistantApp.ServicesPackage.CommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Mati on 2017-04-02.
 */
@Controller
@RequestMapping("/bills")
public class BillController {

    private BillService billService;
    private CommissionService commissionService;

    @Autowired
    public BillController(BillService billService,CommissionService commissionService) {
        this.billService = billService;
        this.commissionService = commissionService;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/newBill",method= RequestMethod.POST,params="adminBillAction=exposeBill")
    public String exposeBill(Commission commission) {
        billService.createBill(commissionService.getCommissionById(commission.getId()));
        return "redirect:/adminDashboard/toBill";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/myBill", method = RequestMethod.POST,params="clientBillAction=getBill")
    public ResponseEntity<byte[]> getRegistryReport(Commission commission) {
        return billService.getBillEntity(commissionService.getCommissionById(commission.getId()));
    }


}
