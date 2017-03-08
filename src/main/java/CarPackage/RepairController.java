package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

/**
 * Created by Mati on 2016-12-18.
 */
@Controller
@RequestMapping("/myRepairs")
@SessionAttributes("selectedRepairId")
public class RepairController {
    //private CarRepository carRepository;//trzeba bedzie autowired user employee np.
    private AccountRepository accountRepository;
    private EmployeeRepository employeeRepository;
    private RepairRepository repairRepository;
    private CommissionRepository commissionRepository;
    private PartRepository partRepository;
    private StoreRepository storeRepository;

    @Autowired
    public RepairController(CarRepository carRepository,AccountRepository accountRepository,EmployeeRepository employeeRepository,RepairRepository repairRepository,CommissionRepository commissionRepository,PartRepository partRepository,StoreRepository storeRepository) {
        //this.carRepository = carRepository;
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.repairRepository = repairRepository;
        this.commissionRepository = commissionRepository;
        this.partRepository = partRepository;
        this.storeRepository = storeRepository;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String getRepairs(Map<String, Object> model, Principal principal) {
        String username = principal.getName();//to chyba autowired dać
        Account account = accountRepository.findByUsername(username);
        Employee employee = employeeRepository.findOne(account.getEmployee().getId());
        Set<Repair> repairSet = employee.getRepairSet();//repairRepository.findByEmployee(employee.getId());
        Set<Repair> repairToComplete = new HashSet<>();
        repairSet.stream().filter(r -> r.getAccomplish() == false).forEach(r->repairToComplete.add(r));//to avoid complete repair again
        model.put("repairs",repairToComplete);
        //List<Car> cars = carRepository.findAll();
        //model.put("cars", cars);//rep nie car
        //return "CarView";
        return "RepairsView";
    }

    @RequestMapping(method=RequestMethod.POST,params="userBackAction")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String back() {
        return "redirect:/employeeDashboard";
    }

    @RequestMapping(value="/addRepair",method= RequestMethod.GET)
    public String repairs(Map<String, Object> model,Principal principal) {
        Set<Employee> employees = employeeRepository.findAll();
        model.put("employees", employees);
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Employee employee = employeeRepository.findOne(account.getEmployee().getId());
        Set<Repair> repairSet = employee.getRepairSet();
        model.put("repairs",repairSet);
        return "NewRepair";
    }

    @RequestMapping(value="/addRepair", method= RequestMethod.POST,params="employeeAddAction=addRepair")
    public String addRepair(Long commissionId,String description, Long employeeId,Principal principal) {
        Commission commissionNeedRepair = commissionRepository.findOne(commissionId);
        //if(employeeId!=1){inaczej gdy employee==1 admin przydzieli
        Employee employeeToRepair = employeeRepository.findOne(employeeId);
        Repair newRepair = new Repair(employeeToRepair,commissionNeedRepair,description);
        repairRepository.save(newRepair);
        return "redirect:/myRepairs";//zmienic tez od wejscia zaleznie
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSelectAction=selectRepair")
    public String selectCommission(@Valid @ModelAttribute("repairSended") Repair repair, BindingResult result, Principal principal , Model model) {
        //String username = principal.getName();
        // Account account = accountRepository.findByUsername(username);
        //Client client = clientRepository.findOne(account.getClient().getId());
        Repair singleRepair = repairRepository.findOne(repair.getId());
        model.addAttribute("repair",singleRepair);
        model.addAttribute("selectedRepairId", singleRepair.getId());
        return "RepairSingleView";
    }

    @RequestMapping(method=RequestMethod.POST,params="employeeAddAction")
    public String addNewRepair(RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("from","overview");
        return "redirect:/myRepairs/addRepair";
    }

    @RequestMapping(method=RequestMethod.POST,params="employeeBackAction=toMyRepairs")
    public String backToRepairs() {
        return "redirect:/myRepairs";
    }

    @RequestMapping(method=RequestMethod.POST,params="employeeBackAction=toMyDashboard")
    public String backToDashboard() {
        return "redirect:/employeeDashboard";
    }

    @RequestMapping(value="/evaluate",method= RequestMethod.POST,params="employeeEvaluateAction=startEvaluate")
    public String startevaluate(Map<String, Object> model) {
        return "StartEvaluateRepair";
    }

    @RequestMapping(value="/evaluate",method= RequestMethod.POST,params="employeeEvaluateAction=continueEvaluate")
    public String continueevaluate(Map<String, Object> model,int count) {
        ArrayList<SinglePart> fieldToFill = new ArrayList<SinglePart>();
        //String[] fieldToFill = new String[count];
        for(int i=0;i<count;i++)fieldToFill.add(new SinglePart("",i));
        ListPartRepair listPartRepair = new ListPartRepair();
        listPartRepair.setPartRepair(fieldToFill);
        model.put("listPartRepair",listPartRepair);
        Set<String> parts = new HashSet<String>(Arrays.asList(Arrays.stream(TypePart.values()).map(TypePart::name).toArray(String[]::new)));
        model.put("parts", parts);
        return "EvaluateRepair";
    }

    @RequestMapping(value="/evaluate",method= RequestMethod.POST,params="employeeEvaluateAction=saveEvaluate")
    public String saveevaluate(@ModelAttribute("listPartRepair") ListPartRepair listPartRepair,/*BindingResult result,*/@ModelAttribute("selectedRepairId") Long selectedRepairId) {
        Repair repair = repairRepository.findOne(selectedRepairId);
        Set<Part> partSet = new HashSet<Part>();
        for(int i=0;i<listPartRepair.getPartRepair().size();i++) {
            Store store;
            switch(listPartRepair.getPartRepair().get(i).value){
                case "Empty": store = storeRepository.findOne((long)1);break;
                case "Engine": store = storeRepository.findOne((long)2);break;
                case "Transmission": store = storeRepository.findOne((long)3);break;
                case "Tires": store = storeRepository.findOne((long)4);break;
                case "Body": store = storeRepository.findOne((long)5);break;
                case "Lights": store = storeRepository.findOne((long)6);break;
                case "Equipment": store = storeRepository.findOne((long)7);break;
                case "Brakes": store = storeRepository.findOne((long)8);break;
                default: store = null;
            }
            Part newPart = new Part(repair,store);
            //partRepository.save(newPart);//być może niekoniecznie potrzebne refactor
            partSet.add(newPart);
        }
        repair.setPartSet(partSet);
        repairRepository.save(repair);
        //jeszcze wydobyc id z repaira a jego przesylac jakos albo autowired i juz
        return "redirect:/myRepairs";
    }

    @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=makeRepair")
    public String makeRepair(Map<String, Object> model,@ModelAttribute("selectedRepairId") Long selectedRepairId,@ModelAttribute("part") Part repairPart) {
        if(null!=repairPart.getId()){
            Part repairedPart = partRepository.findOne(repairPart.getId());
            if(repairedPart.getStore().getAmount()>=1) {
                repairedPart.getStore().setAmount(repairedPart.getStore().getAmount()-1);
                repairedPart.setResolved(true);
                partRepository.save(repairedPart);
            }
            else model.put("storeEmpty","Y");
        }
        Repair repair = repairRepository.findOne(selectedRepairId);
        Set<Part> parts = repair.getPartSet();
        Set<Part> partToRepair = new HashSet<>();
        parts.stream().filter(p -> p.getStore().getId()>8 && p.getResolved()==false).forEach(p->partToRepair.add(p));
        if(partToRepair.isEmpty())
            model.put("NeedRepair","N");
        else{
            model.put("NeedRepair","Y");
            model.put("parts", partToRepair);
        }
        return "RepairInProgress";
    }

    @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=repairDone")
    public String doneRepair(Map<String, Object> model,@ModelAttribute("repair") Repair selectedRepair) {
        Repair repairedPart = repairRepository.findOne(selectedRepair.getId());
        Set<Part> anyToComplete = new HashSet<>();
        repairedPart.getPartSet().stream().filter(p -> p.getResolved() == false).forEach(p -> anyToComplete.add(p));
        if(anyToComplete.isEmpty()) {
            repairedPart.setAccomplish(true);
            repairRepository.save(repairedPart);
            return "redirect:/myRepairs";
        }
        else{
            model.put("repair",repairedPart);
            model.put("selectedRepairId", repairedPart.getId());
            model.put("complete","notAllPartComplete");
            return "RepairSingleView";
        }
    }

}
