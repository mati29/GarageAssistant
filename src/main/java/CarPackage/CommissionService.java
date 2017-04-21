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

    @Autowired
    public CommissionService(CommissionRepository commissionRepository){
        this.commissionRepository = commissionRepository;
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
