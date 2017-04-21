package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class CommissionService {

    private CommissionRepository commissionRepository;
    private ClientService clientService;

    @Autowired
    public CommissionService(CommissionRepository commissionRepository,ClientService clientService){
        this.commissionRepository = commissionRepository;
        this.clientService = clientService;
    }

    public long getId(Commission commission){
        return commission.getId();
    }

    public Client getClient(Commission commission){
        return commission.getClient();
    }

    public String getFileName(Commission commission){
        return clientService.getClientFullName(getClient(commission))+getId(commission);
    }

    public boolean checkBill(Commission commission){
        return null != commission.getBill();
    }

    public Bill getOneBill(Commission commission){
        return commission.getBill();
    }

    public void setAfterCheck(Commission commission){
        commission.setAfterCheck(false);
    }

    public Commission getCommissionById(long id){
        return commissionRepository.findOne(id);
    }

    public List<Commission> getAllCommission(){
        return commissionRepository.findAll();
    }

    public List<Commission> getCompletedCommission(){
        List<Commission> completedCommission = new ArrayList<>();
        getAllCommission().forEach(c -> {                                                                                                                                      //just for test delete after release version
                                        if(c.getRepairList()
                                                .stream()
                                                .filter(r -> r.getAccomplish() == true)
                                                .count() ==
                                           c.getRepairList()
                                                   .stream()
                                                   .count()
                                                &&
                                           (null== c.getBill()
                                                   ||
                                            c.getAfterCheck())
                                                &&
                                            !c.getRepairList().isEmpty()) //to delete in release version
                                                                            completedCommission.add(c);
                                        }
                                    );
        return completedCommission;
    }
}
