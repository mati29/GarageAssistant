package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

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
    public String addCommission(Car newCar,String description, Long employeeId,Principal principal ) {
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Client client = clientRepository.findOne(account.getClient().getId());
        Car car = carRepository.save(newCar);
        java.util.Date term = Calendar.getInstance().getTime();
        Commission newCommission = new Commission(client,car,term);
        commissionRepository.save(newCommission);
        //if(employeeId!=1){default roztrzygnie admin jednakze repair dodaje
            Employee employeeToRepair = employeeRepository.findOne(employeeId);
            //employeeToRepair.addRepair()
            Repair newRepair = new Repair(employeeToRepair,newCommission,description);
            repairRepository.save(newRepair);
        //}
        return "redirect:/myCommission";
    }

    @RequestMapping(value="/addCommission",method= RequestMethod.GET)
    public String commissions(Map<String, Object> model) {
        Set<Employee> employees = employeeRepository.findByPost("mechanic");
        model.put("employees", employees);
        return "NewCommission";
    }

    @RequestMapping(/*value="/addCommission", */method= RequestMethod.POST,params="clientSelectAction=selectCommission")
    public String selectCommission(@Valid @ModelAttribute("commissionSended") Commission commission, BindingResult result, Principal principal , Model model) {
        //String username = principal.getName();
       // Account account = accountRepository.findByUsername(username);
        //Client client = clientRepository.findOne(account.getClient().getId());
        Commission singleCommission = commissionRepository.findOne(commission.getId());
        model.addAttribute("commission",singleCommission);
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

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=evaluateCommission")
    public String evaluateCommission(@Valid @ModelAttribute Commission commission, BindingResult result, Principal principal , Model model) {
        //String username = principal.getName();
        // Account account = accountRepository.findByUsername(username);
        //Client client = clientRepository.findOne(account.getClient().getId());
        Commission singleCommission = commissionRepository.findOne(commission.getId());
        Set<Repair> repairSet = singleCommission.getRepairSet();
        Set<Part> neededParts = new HashSet<>();
        Set<Set<Store>> allToChoose = new HashSet<>();
        for(Repair repair : repairSet){
            neededParts.addAll(repair.getPartSet());
        }//odejmuje amount -1 i gdy 0 kasuje z bazy dla store
        for(Part part : neededParts){
            Set<Store> storeSet = new HashSet<>();
            switch(part.getStore().getType()){
                case "EMPTY": storeSet = storeRepository.findByType("Empty");break;
                case "ENGINE": storeSet = storeRepository.findByType("Engine");break;
                case "TRANSMISSION": storeSet = storeRepository.findByType("Transmission");break;
                case "TIRES": storeSet = storeRepository.findByType("Tires");break;
                case "BODY": storeSet = storeRepository.findByType("Body");break;
                case "LIGHTS": storeSet = storeRepository.findByType("Lights");break;
                case "EQUIPMENT": storeSet = storeRepository.findByType("Equipment");break;
                case "BRAKES": storeSet = storeRepository.findByType("Brakes");break;
                default: storeSet = null;
            }
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
    public String saveEvaluate(@ModelAttribute("clientChoosePart") ClientChoosenPart clientChoosePart, BindingResult result) {
        for(ChangePart part:clientChoosePart.chosenPart){
            Part partToSave = partRepository.findOne(part.getPartId());
            Store storeToChange = storeRepository.findOne(part.getStoreId());
            partToSave.setStore(storeToChange);
            partRepository.save(partToSave);
        }
        return "redirect:/myCommission";
    }

}
