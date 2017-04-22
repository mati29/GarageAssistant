package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class BillService {

    private CommissionService commissionService;
    private BillRepository billRepository;

    @Autowired
    public BillService(CommissionService commissionService,BillRepository billRepository){
        this.commissionService = commissionService;
        this.billRepository = billRepository;
    }

    public ResponseEntity<byte[]> getBillEntity(Commission commission){
        Bill bill = getBillFromCommission(commission);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));                                                                //na jego osobiste id
        String filename = getBillFileName(commission);
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<byte[]>(bill.getPdf(), headers, HttpStatus.OK);

    }

    public String getBillFileName(Commission commission){
        return commissionService.getFileName(commission)+"Bill.pdf";
    }

    public Bill getBillById(long id){
        return billRepository.findOne(id);
    }

    public Bill getBillFromCommission(Commission commission){
        return commissionService.getOneBill(commission);
    }

    public byte[] getPdf(Bill bill){
        return bill.getPdf();
    }

    public void createBill(Commission commission){
        Bill billToSave = newOrExist(commission);
        billToSave.setCommission(commission);
        billToSave.setPdf(ClientBill.makeBill(billToSave));
        billRepository.save(billToSave);
    }

    public Bill newOrExist(Commission commission){
        Bill billToBeSave;
        if(commissionService.checkBill(commission)) {
            billToBeSave = getBillFromCommission(commission);
            commissionService.setAfterCheck(commission);
        }
        else
            billToBeSave = new Bill();
        return billToBeSave;
    }
}
