package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mati on 2016-11-21.
 */
@Controller
@RequestMapping("/myCommission")
public class CommissionController {

    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private CommissionRepository commissionRepository;
    private AccountRepository accountRepository;
    private EmployeeRepository employeeRepository;
    private RepairRepository repairRepository;
    private StoreRepository storeRepository;
    private PartRepository partRepository;

    @Autowired
    public CommissionController(AccountRepository accountRepository,CarRepository carRepository,ClientRepository clientRepository,CommissionRepository commissionRepository,EmployeeRepository employeeRepository,RepairRepository repairRepository,StoreRepository storeRepository,PartRepository partRepository) {
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.commissionRepository = commissionRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.repairRepository = repairRepository;
        this.storeRepository = storeRepository;
        this.partRepository = partRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String getCommission(Map<String, Object> model, Principal principal){
        String username = principal.getName();//to chyba autowired dać
        Account account = accountRepository.findByUsername(username);
        Client client = clientRepository.findOne(account.getClient().getId());//to całe
        Set<Commission> clientCommissionSet = client.getCommissionSet();
        model.put("commissions", clientCommissionSet);
        return "CommissionsView";
    }

    @RequestMapping(value="/addCommission", method= RequestMethod.POST,params="clientAddAction=addCommission")
    public String addCommission(Car newCar,String description,String specialService, Long employeeId,Principal principal,HttpServletRequest request) {
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Client client = clientRepository.findOne(account.getClient().getId());
        java.util.Date term = Calendar.getInstance().getTime();
        Commission newCommission = new Commission(client,newCar,term);
        newCar.setCommission(newCommission);
        boolean autoMechanic = (boolean)request.getSession().getAttribute("AM");
        Employee employeeToRepair;
        if(autoMechanic){
            employeeToRepair = employeeRepository.findOne(1L);
        }
        else {
            employeeToRepair = employeeRepository.findOne(employeeId);
        }
        Repair newRepair = new Repair(employeeToRepair,newCommission,description);
        newCommission.setRepairSet(new HashSet<Repair>(Arrays.asList(newRepair)));//bo na start 1 naprawa ogolna pracownik rozdzielie ew.
        if(specialService!=""){
            List<String> addPart = Arrays.asList(specialService.split(","));
            Set<Part> partSet = new HashSet<>();
            for(String stringPart:addPart){
                Part part = new Part();
                    Store store = new Store();
                    String[] brandModel = stringPart.split(":");
                if(brandModel.length==2) {
                    store.setBrand(brandModel[0]);
                    store.setModel(brandModel[1]);
                    storeRepository.save(store);
                    part.setStore(store);
                    part.setRepair(newRepair);
                    partSet.add(part);
                }
            }
            newRepair.setPartSet(partSet);
        }
        carRepository.save(newCar);
        return "redirect:/myCommission";
    }

    @RequestMapping(value="/addCommission",method= RequestMethod.GET)
    public String commissions(Map<String, Object> model,HttpServletRequest request) {
        boolean autoMechanic = (boolean)request.getSession().getAttribute("AM");
        if(autoMechanic){
            model.put("AM", autoMechanic);
        }
        else {
            Set<Employee> employees = employeeRepository.findByPost("mechanic");
            model.put("employees", employees);
        }
        boolean additionalService = (boolean) request.getSession().getAttribute("AS");
            model.put("AS",additionalService);
        return "NewCommission";
    }

    @RequestMapping(/*value="/addCommission", */method= RequestMethod.POST,params="clientSelectAction=selectCommission")
    public String selectCommission(@Valid @ModelAttribute("commissionSended") Commission commission, BindingResult result, Principal principal , Model model,HttpServletRequest request) {
        //String username = principal.getName();
       // Account account = accountRepository.findByUsername(username);
        //Client client = clientRepository.findOne(account.getClient().getId());
        Commission singleCommission = commissionRepository.findOne(commission.getId());
        model.addAttribute("commission",singleCommission);
        //need refactor number of part in variable no 1-8
        Set<Part> partToEvaluate = new HashSet<>();
        singleCommission.getRepairSet().stream().forEach(r -> r.getPartSet().stream().forEach((p) -> {if(p.getStore().getId()<=8 && p.getStore().getId()>=1){partToEvaluate.add(p);} }));
        if(!partToEvaluate.isEmpty())
            model.addAttribute("evaluateNeeded","true");
        boolean autoPart = (boolean)request.getSession().getAttribute("AP");
        if(autoPart)
            model.addAttribute("AP",autoPart);
        boolean additionalService = (boolean)request.getSession().getAttribute("AS");
        if(additionalService)
            model.addAttribute("AS",additionalService);
        return "CommissionSingleView";
    }

    @RequestMapping(method=RequestMethod.POST,params="clientBackAction=toMyCommission")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String backToCommission() {
        return "redirect:/myCommission";
    }

    @RequestMapping(method=RequestMethod.POST,params="clientBackAction=toMyDashboard")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String backToDashboard() {
        return "redirect:/clientDashboard";
    }


    @RequestMapping(method=RequestMethod.POST,params="clientAddAction")
    public String addNewCommission(RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("from","overview");
        return "redirect:/myCommission/addCommission";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=needToRepair")
    public String needToRepair(@Valid @ModelAttribute Commission commission, BindingResult result, Principal principal , Model model,HttpServletRequest request) {
        Commission singleCommission = commissionRepository.findOne(commission.getId());
        Set<Repair> repairSet = singleCommission.getRepairSet();
        Set<Image> images = new HashSet<Image>();
        //repairSet.stream().forEach(r -> images.addAll(r.getImageSet()));
        for(Repair r:repairSet){
            for(Image i:r.getImageSet()){
                images.add(i);
            }
        }
        model.addAttribute("images",images);
        return "ImagesOfDamage";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=evaluateCommission")
    public String evaluateCommission(@Valid @ModelAttribute Commission commission, BindingResult result, Principal principal , Model model,HttpServletRequest request) {
        //String username = principal.getName();
        // Account account = accountRepository.findByUsername(username);
        //Client client = clientRepository.findOne(account.getClient().getId());
        Commission singleCommission = commissionRepository.findOne(commission.getId());
        Set<Repair> repairSet = singleCommission.getRepairSet();
        Set<Part> neededParts = new HashSet<>();
        Set<Set<Store>> allToChoose = new HashSet<>();
        for(Repair repair : repairSet){
            //neededParts.addAll(repair.getPartSet());
            repair.getPartSet().stream().forEach((p) -> {if(p.getStore().getId()<=8 && p.getStore().getId()>=1){neededParts.add(p);} });
        }//odejmuje amount -1 i gdy 0 kasuje z bazy dla store
        for(Part part : neededParts){
            Set<Store> storeSet = new HashSet<>();
            switch(part.getStore().getType()){
                //case "EMPTY": storeSet = storeRepository.findByType("Empty");break; głupota to dodatkowe
                case "ENGINE": storeSet = storeRepository.findByType("Engine");break;
                case "TRANSMISSION": storeSet = storeRepository.findByType("Transmission");break;
                case "TIRES": storeSet = storeRepository.findByType("Tires");break;
                case "BODY": storeSet = storeRepository.findByType("Body");break;
                case "LIGHTS": storeSet = storeRepository.findByType("Lights");break;
                case "EQUIPMENT": storeSet = storeRepository.findByType("Equipment");break;
                case "BRAKES": storeSet = storeRepository.findByType("Brakes");break;
                default: storeSet = null;
            }
            storeSet.add(storeRepository.findByType("EMPTY").iterator().next());//na rzecz obslugi pustego
            boolean extraPart = (boolean)request.getSession().getAttribute("EP");
            if(extraPart)
                storeSet.add(storeRepository.findByType("UNIQUE").iterator().next());
            allToChoose.add(storeSet);
        }
        //if(allToChoose.isEmpty()) logika dla późniejszego zastosowania gdy dodatkowe czesci do wybrania
        //ale wkladam nulle wiec moze nie zadziala
        //plus przemyslec jak oznaczyc te wybrane czesci od strony pracowniczej
        ArrayList<ChangePart> changeParts = new ArrayList<>();
        for(Part part : neededParts) {
            ChangePart partToChange = new ChangePart();
            partToChange.setPartId(part.getId());
            changeParts.add(partToChange);
        }
        ClientChoosenPart clientChoosePart = new ClientChoosenPart();
        clientChoosePart.setChosenPart(changeParts);
        model.addAttribute("clientChoosePart",clientChoosePart);
        model.addAttribute("stores",allToChoose);
        return "ClientEvaluation";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=saveEvaluate")
    public String saveEvaluate(@ModelAttribute("clientChoosePart") ClientChoosenPart clientChoosePart, BindingResult result,Model model) {
        //WAZNE TERAZ ZMIANA TYPU NA UNIQUEPARTLIST I UNIQUEPART DZIEKI TEMU NIE ZGUBIE PARTID I MODEL I BRAND LATWIEJ
        //DODAC PARTID W HIDDEN TEZ ZMIANA NAZW WSZYSTKICH W FRONTENDZIE
        UniquePartList uniquePartList = new UniquePartList();
        ArrayList<UniquePart> uniqueParts = new ArrayList<>();
        for(ChangePart part:clientChoosePart.chosenPart){
            if(part.getStoreId()!=1 && part.getStoreId()!=0) {//obsługa emptowego/defaultowego
                Part partToSave = partRepository.findOne(part.getPartId());
                Store storeToChange = storeRepository.findOne(part.getStoreId());
                partToSave.setStore(storeToChange);
                partRepository.save(partToSave);
            }
            if(part.getStoreId()==0){
                Part partToSearch = partRepository.findOne(part.getPartId());
                UniquePart uniquePart = new UniquePart();
                uniquePart.setTypeOfStore(partToSearch.getStore().getId().intValue());
                uniquePart.setPartId(part.getPartId().intValue());
                uniqueParts.add(uniquePart);
            }
        }
        if(!uniqueParts.isEmpty()){
            uniquePartList.setUniqueParts(uniqueParts);
            model.addAttribute("uniquePartList",uniquePartList);
            return "UniquePartRequirement";
        }
        return "redirect:/myCommission";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=addUnique")
    public String addUnique(@ModelAttribute("uniquePartList") UniquePartList uniquePartList, BindingResult result,Model model) {
        for(UniquePart uniquePart:uniquePartList.getUniqueParts()){
            Part partToUnification = partRepository.findOne((long)uniquePart.getPartId());
            Store uniqueStoreToAdd = new Store();
            uniqueStoreToAdd.setBrand(uniquePart.getBrand());
            uniqueStoreToAdd.setModel(uniquePart.model);
            uniqueStoreToAdd.setType(storeRepository.findOne((long)uniquePart.getTypeOfStore()).getType());
            partToUnification.setStore(uniqueStoreToAdd);
            storeRepository.save(uniqueStoreToAdd);
        }
        return "redirect:/myCommission";
    }

    @RequestMapping(value="/specialService", method= RequestMethod.POST,params="clientSpecialAction=specialService")
    public String additionalService(@Valid @ModelAttribute Commission commission,Model model,Principal principal) {
        Set<String> parts = new HashSet<String>(Arrays.asList(Arrays.stream(TypePart.values()).map(TypePart::name).toArray(String[]::new)));
        model.addAttribute("partsType", parts);
        model.addAttribute("commission",commission);
        return "AdditionalService";
    }

    @RequestMapping(value="/specialService", method= RequestMethod.POST,params="clientSpecialAction=saveSpecialService")
    public String specialSave(@Valid @ModelAttribute Commission commission,String partString,Model model,Principal principal) {
        //Narazie może dorobie wybór pracownika i opisu co i jak zrobione
        Commission commissionToDo = commissionRepository.findOne(commission.getId());
        Repair newRepair = new Repair(employeeRepository.findOne(1L),commissionToDo,"Additional Work Needed");
        commissionToDo.getRepairSet().add(newRepair);//styka?
        List<String> addPart = Arrays.asList(partString.split(","));
        Set<Part> partSet = new HashSet<>();
        for(String stringPart:addPart){
            Part part = new Part();
            Store store = new Store();
            List<String> brandModel = new ArrayList<String>(Arrays.asList(stringPart.split(":")));
            brandModel.get(0).replace(" ","");
            if(brandModel.size()==3) {
                store.setType(brandModel.get(0));
                store.setModel(brandModel.get(1));
                store.setBrand(brandModel.get(2));
                storeRepository.save(store);
                part.setStore(store);
                part.setRepair(newRepair);
                partSet.add(part);
            }
        }
        newRepair.setPartSet(partSet);
        repairRepository.save(newRepair);
        return "redirect:/myCommission";
    }

    @RequestMapping(value = "/images/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {

        String directory = new File("images").getAbsolutePath();
        String[] fileandFileType = imageName.split("`");
        File serverFile = new File(directory+File.separator+fileandFileType[0]+"."+fileandFileType[1]);

        return Files.readAllBytes(serverFile.toPath());
    }


}
