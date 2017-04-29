package main.java.GarageAssistantApp.ServicesPackage;

import main.java.GarageAssistantApp.EntityPackage.*;
import main.java.GarageAssistantApp.RepositoriesPackage.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class CommissionService {

    private CommissionRepository commissionRepository;
    private ClientService clientService;
    private AccountService accountService;
    private EmployeeService employeeService;
    private RepairService repairService;
    private StoreService storeService;
    private CarService carService;

    @Autowired
    public void setCommissionRepository(CommissionRepository commissionRepository){
        this.commissionRepository = commissionRepository;
    }

    @Autowired
    public void setCarService(CarService carService){
        this.carService = carService;
    }

    @Autowired
    public void setRepairService(RepairService repairService){
        this.repairService = repairService;
    }

    @Autowired
    public void setStoreService(StoreService storeService){
        this.storeService = storeService;
    }

    @Autowired
    public void setClientService(ClientService clientService){
        this.clientService = clientService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    public Commission getCommissionFromIdWithCheckBill(long id){
        Commission commissionToCheck= commissionRepository.findOne(id);
        if(checkBill(commissionToCheck))
            setAfterCheck(commissionToCheck,true);
        return commissionToCheck;
    }

    public Long getId(Commission commission){
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

    public void setAfterCheck(Commission commission,boolean status){
        commission.setAfterCheck(status);
    }

    public Commission getCommissionById(long id){
        return commissionRepository.findOne(id);
    }

    public List<Commission> getAllCommission(){
        return commissionRepository.findAll();
    }

    public List<Commission> getCommissionListByUsername(String username){
        return clientService.getCommissionFromClient(accountService.getClientFromAccount(accountService.getAccountFromUsername(username)));
    }

    public List<Repair> getRepairListFromCommission(Commission commission){
        return commission.getRepairList();
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

    public void addRepairToCommission(Commission commission,Repair repair){
        getRepairListFromCommission(commission).add(repair);
    }

    public void addCustomRepair(Commission commission,String codedPart){
        Repair newRepair = new Repair(employeeService.getDefaultEmployee(),commission,"Custom client repair");
        if(checkBill(commission))
            setAfterCheck(commission,true);
        addRepairToCommission(commission,newRepair);
        repairService.customRepairSave(newRepair,codedPart);
    }

    public void setRepairList(Commission commission,List<Repair> repairs){
        commission.setRepairList(repairs);
    }

    public void addCommission(Commission commission,boolean autoMechanic,String description,String specialService,Long employeeId){
        Employee employeeToRepair;
        if(autoMechanic){
            employeeToRepair = employeeService.getDefaultEmployee();
        }
        else {
            employeeToRepair = employeeService.getEmployeeFromId(employeeId);
        }
        Repair newRepair = new Repair(employeeToRepair,commission,description);
        setRepairList(commission,new ArrayList<Repair>(Arrays.asList(newRepair)));
        if(!specialService.isEmpty()){
            List<String> addPart = Arrays.asList(specialService.split(","));
            List<Part> partList = new ArrayList<>();
            addPart
                    .forEach(
                                stringPart->
                                                {
                                                    Part part = new Part();
                                                    Store store = new Store();
                                                    String[] brandModel = stringPart.split(":");
                                                    if(brandModel.length==2) {
                                                        storeService.setBrand(store,brandModel[0]);
                                                        storeService.setModel(store,brandModel[1]);
                                                        storeService.saveStore(store);
                                                        part.setStore(store);
                                                        part.setRepair(newRepair);
                                                        partList.add(part);
                                                    }
                                                }
                            )
                    ;
            newRepair.setPartList(partList);
        }
        carService.setCommission(getCarFromCommission(commission),commission);
        carService.saveCar(getCarFromCommission(commission));
    }

    public Car getCarFromCommission(Commission commission){
        return commission.getCar();
    }
}
