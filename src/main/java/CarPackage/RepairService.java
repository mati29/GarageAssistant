package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class RepairService {

    private RepairRepository repairRepository;

    @Autowired
    public RepairService(RepairRepository repairRepository){
        this.repairRepository = repairRepository;
    }

    public List<Repair> getFreeRepair(){
        return repairRepository.findByEmployeeId(1L);
    }

    public ListRepair getReadyRepair(){
        return new ListRepair(getFreeRepair());
    }

}
