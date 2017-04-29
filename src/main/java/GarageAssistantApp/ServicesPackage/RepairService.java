package main.java.GarageAssistantApp.ServicesPackage;

import main.java.GarageAssistantApp.DTOPackage.ChangePart;
import main.java.GarageAssistantApp.DTOPackage.ListDTO.ListImage;
import main.java.GarageAssistantApp.DTOPackage.ListDTO.ListPartRepair;
import main.java.GarageAssistantApp.DTOPackage.ListDTO.ListRepair;
import main.java.GarageAssistantApp.DTOPackage.SimpleDTO.SinglePart;
import main.java.GarageAssistantApp.EntityPackage.*;
import main.java.GarageAssistantApp.RepositoriesPackage.RepairRepository;
import main.java.GarageAssistantApp.StandardPackage.ImageSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Mati on 2017-04-21.
 */
@Service
public class RepairService {

    private RepairRepository repairRepository;
    private AccountService accountService;
    private EmployeeService employeeService;
    private CommissionService commissionService;
    private StoreService storeService;
    private PartService partService;
    private ImageSaver imageSaver;
    private ImageService imageService;
    private SettingsService settingsService;
    private ClientService clientService;

    @Autowired
    public void setRepairRepository(RepairRepository repairRepository){
        this.repairRepository = repairRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @Autowired
    public void setCommissionService(CommissionService commissionService){
        this.commissionService = commissionService;
    }

    @Autowired
    public void setStoreService(StoreService storeService){
        this.storeService = storeService;
    }

    @Autowired
    public void setPartService(PartService partService){
        this.partService = partService;
    }

    @Autowired
    public void setImageSaver(ImageSaver imageSaver){
        this.imageSaver = imageSaver;
    }

    @Autowired
    public void setImageService(ImageService imageService){
        this.imageService = imageService;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService){
        this.settingsService = settingsService;
    }

    @Autowired
    public void setClientService(ClientService clientService){
        this.clientService = clientService;
    }


    public List<Repair> getFreeRepair(){
        return repairRepository.findByEmployeeId(1L);
    }

    public ListRepair getReadyRepair(){
        return new ListRepair(getFreeRepair());
    }

    public Repair getRepairFromImage(Image image){
        return image.getRepair();
    }

    public Long getRepairId(Repair repair){
        return repair.getId();
    }

    public List<Repair> getActiveRepairForEmployeeFromUsername(String username){
        List<Repair> activeRepairs = employeeService.getRepairsFromEmployee(accountService.getEmployeeFromAccount(accountService.getAccountFromUsername(username)));
        activeRepairs = activeRepairs.stream().filter(r -> r.getAccomplish() == false).collect(Collectors.toList());
        return activeRepairs;
    }

    public void saveRepair(Repair repair){
        repairRepository.save(repair);
    }

    public void addRepair(Long commissionId,String description, Long employeeId){
        saveRepair(new Repair(employeeService.getEmployeeFromId(employeeId),commissionService.getCommissionFromIdWithCheckBill(commissionId),description));
    }

    public Repair getRepairFromId(long id){
        return repairRepository.findOne(id);
    }

    public ListPartRepair createListPartRepair(int count){
        ArrayList<SinglePart> singlePartList = new ArrayList<SinglePart>();
        IntStream.range(0, count).forEach(number -> singlePartList.add(new SinglePart("",number)));
        return new ListPartRepair(singlePartList);
    }

    public void savePartInRepair(ArrayList<ChangePart> choosenPart){
        choosenPart
                .stream()
                .forEach(
                        part ->
                                {
                                    if(!storeService.checkDefault(part)) {
                                        Store store = storeService.getStoreFromChangePart(part);
                                        Part savingPart= partService.getPartFromChangePart(part);
                                        partService.setStore(savingPart,store);
                                        partService.savePart(savingPart);
                                    }

                                }
                        )
                ;
    }

    public List<Part> getPartFromRepair(Repair repair){
        return repair.getPartList();
    }


    public boolean repairCouldBeFinished(List<Part> partInRepair,List<Part> partToResolve,boolean noPart){
        return ((partToResolve.isEmpty() && !partInRepair.isEmpty()) || (partInRepair.isEmpty() && noPart))?true:false;
    }

    public void setAccomplish(Repair repair,boolean status){
        repair.setAccomplish(status);
    }

    public void setHours(Repair repair,int hours){
        repair.setHours(hours);
    }

    public int getHours(Repair repair){
        return repair.getHours();
    }

    public void calculateRepair(Repair repair){
        Repair repairedDone = getRepairFromId(getRepairId(repair));
        setAccomplish(repairedDone,true);
        setHours(repairedDone,getHours(repair));
        saveRepair(repairedDone);
    }

    public boolean anyPartFromRepairsNeedEvaluation(List<Repair> repairList){
        List<Part> partNeedToEvaluate = new ArrayList<>();
        repairList.forEach(repair -> partNeedToEvaluate.addAll(partService.partNeedEvaluation(getPartFromRepair(repair))));
        return !partNeedToEvaluate.isEmpty();
    }

    public List<Part> partFromRepairNeedEvaluation(List<Repair> repairList){
        List<Part> partNeedToEvaluate = new ArrayList<>();
        repairList.forEach(repair -> partNeedToEvaluate.addAll(partService.partNeedEvaluation(getPartFromRepair(repair))));
        return partNeedToEvaluate;
    }

    public Employee getEmployeeFromRepair(Repair repair){
        return repair.getEmployee();
    }

    public void setEmployee(Repair repair,Employee employee){
        repair.setEmployee(employee);
    }
    public void setEmployeesForFreeRepairs(ListRepair listRepair){
        listRepair
                .getRepairList()
                .forEach(r->
                            {
                                Repair repair = getRepairFromId(getRepairId(r));
                                if(!employeeService.checkDefaultEmployee(getEmployeeFromRepair(r))) {
                                    setEmployee(repair,employeeService.getEmployeeFromId(employeeService.getEmployeeId(getEmployeeFromRepair(r))));
                                    saveRepair(repair);
                                }
                            }
                        )
                ;
    }

    public boolean isRepairNeeded(List<Part> parts){
        return !parts.isEmpty();
    }

    public void setImageList(Repair repair,List<Image> imageList){
        repair.setImageList(imageList);
    }

    public void setPartList(Repair repair,List<Part> partList){
        repair.setPartList(partList);
    }

    public boolean evaluateRepair(Long id,ListPartRepair listPartRepair,ListImage picture){//autopart return
        Repair repair = getRepairFromId(id);
        List<Part> partList = new ArrayList<Part>();
        List<Image> imageList = new ArrayList<Image>();
        List<SinglePart> singleParts = partService.getSinglePartListFromListPartRepair(listPartRepair);
        singleParts
                .stream()
                .forEach(
                        singlePart ->
                                    {
                                        Store store = storeService.getStoreFromType(singlePart.getValue());
                                        Image newImage = new Image(repair,store);
                                        newImage.setPath(imageSaver.saveImage(imageService.getImageFileFromListById(picture,singlePart.getIndex()),newImage,singlePart.getIndex()));
                                        imageList.add(newImage);
                                        Part newPart = new Part(repair,store);
                                        partList.add(newPart);
                                    }
                        )
                ;
        setImageList(repair,imageList);
        setPartList(repair,partList);
        saveRepair(repair);
        return settingsService.getAutoPartFromSettings(clientService.getSettingsFromClient(commissionService.getClient(getCommissionFromRepair(repair))));
    }

    public Commission getCommissionFromRepair(Repair repair){
        return repair.getCommission();
    }

    public void customRepairSave(Repair repair,String codedPart){

        List<String> addPart = Arrays.asList(codedPart.split(","));
        List<Part> partList = new ArrayList<>();
        addPart
                .forEach(
                            stringPart->
                                    {
                                        Part part = new Part();
                                        Store store = new Store();
                                        List<String> brandModel = new ArrayList<String>(Arrays.asList(stringPart.split(":")));
                                        brandModel.set(0,brandModel.get(0).replace(" ",""));
                                        if(brandModel.size()==3) {
                                            partService.setStore(part,storeService.saveCustomPart(store,brandModel.get(0).substring(0, 1) + brandModel.get(0).substring(1).toUpperCase(),brandModel.get(1),brandModel.get(2)));
                                            partService.setRepair(part,repair);
                                            partList.add(part);
                                        }
                                    }
                        )
                ;
        setPartList(repair,partList);
        saveRepair(repair);
    }

}
