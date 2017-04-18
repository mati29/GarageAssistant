package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Mati on 2017-04-02.
 */
@Controller
@RequestMapping("/bills")
public class BillController {

    private CommissionRepository commissionRepository;
    private BillRepository billRepository;

    @Autowired
    public BillController(BillRepository billRepository,CommissionRepository commissionRepository) {
        this.billRepository = billRepository;
        this.commissionRepository = commissionRepository;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/newBill",method= RequestMethod.POST,params="adminBillAction=exposeBill")
    public String exposeBill(Commission commission) {
        Commission commissionToCheck =  commissionRepository.findOne(commission.getId());
        Bill billToSave;
        if(null != commissionToCheck.getBill()) {
            billToSave = commissionToCheck.getBill();
            commissionToCheck.setAfterCheck(false);
        }
        else
            billToSave = new Bill();
        billToSave.setCommission(commissionToCheck);
        billToSave.setPdf(ClientBill.makeBill(billToSave));
        billRepository.save(billToSave);//|| DATA NIE DZIALA
        return "redirect:/adminDashboard/toBill";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/myBill", method = RequestMethod.POST,params="clientBillAction=getBill")
    public ResponseEntity<byte[]> getRegistryReport(Commission commission) {
        Bill bill = billRepository.findOne(commissionRepository.findOne(commission.getId()).getBill().getId());
        byte[] pdfBill = bill.getPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));                                                                //na jego osobiste id
        String filename = bill.getCommission().getClient().getLastName()+bill.getCommission().getClient().getFirstName()+bill.getCommission().getId()+"Bill.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(pdfBill, headers, HttpStatus.OK);
        return response;
    }


}
