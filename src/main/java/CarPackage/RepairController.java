package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
        List<Repair> repairToComplete = new ArrayList<>();
        repairSet.stream().filter(r -> r.getAccomplish() == false).forEach(r->repairToComplete.add(r));//to avoid complete repair again
        repairToComplete.sort((Repair r1, Repair r2)->(int)(r1.getId()-r2.getId()));
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
    public String repairs(Map<String, Object> model,Principal principal,RedirectAttributes redirectAttrs) {
        List<Employee> employees = employeeRepository.findAll();
        employees.sort((Employee e1, Employee e2)->(int)(e1.getId()-e2.getId()));
        model.put("employees", employees);
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username);
        Employee employee = employeeRepository.findOne(account.getEmployee().getId());
        Set<Repair> repairSet = employee.getRepairSet();
        List<Repair> repairList = new ArrayList<>(repairSet);
        repairList.sort((Repair r1, Repair r2)->(int)(r1.getId()-r2.getId()));
        model.put("repairs",repairList);
        return "NewRepair";
    }

    @RequestMapping(value="/addRepair", method= RequestMethod.POST,params="employeeAddAction=addRepair")
    public String addRepair(Long commissionId,String description, Long employeeId,Principal principal,HttpServletRequest request) {
        Commission commissionNeedRepair = commissionRepository.findOne(commissionId);
        if(null != commissionNeedRepair.getBill())
            commissionNeedRepair.setAfterCheck(true);
        Employee employeeToRepair = employeeRepository.findOne(employeeId);
        Repair newRepair = new Repair(employeeToRepair,commissionNeedRepair,description);
        repairRepository.save(newRepair);
        String from = request.getSession().getAttribute("from").toString();
        if(from.equals("dashboard")) {
            request.getSession().removeAttribute("from");
            return "redirect:/employeeDashboard";
        }
        else {
            request.getSession().removeAttribute("from");
            return "redirect:/myRepairs";
        }
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
    public String addNewRepair(RedirectAttributes redirectAttrs,HttpServletRequest request) {
        redirectAttrs.addFlashAttribute("from","overview");
        request.getSession().setAttribute("from","dashboard");
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

    @RequestMapping(method=RequestMethod.POST,params="employeeBackAction=toMyStore")
    public String backToStore() {
        return "redirect:/store";
    }

    @RequestMapping(value="/evaluate",method= RequestMethod.POST,params="employeeEvaluateAction=startEvaluate")
    public String startevaluate(Map<String, Object> model,Repair repair) {
        model.put("repair",repair);
        return "StartEvaluateRepair";
    }

    @RequestMapping(value="/evaluate",method= RequestMethod.POST,params="employeeEvaluateAction=continueEvaluate")
    public String continueevaluate(Map<String, Object> model,int count,Repair repair) {
        ArrayList<SinglePart> fieldToFill = new ArrayList<SinglePart>();
        //String[] fieldToFill = new String[count];
        for(int i=0;i<count;i++)fieldToFill.add(new SinglePart("",i));
        ListPartRepair listPartRepair = new ListPartRepair();
        listPartRepair.setPartRepair(fieldToFill);
        model.put("listPartRepair",listPartRepair);
        ArrayList<String> parts = new ArrayList<String>(Arrays.asList(Arrays.stream(TypePart.values()).map(TypePart::name).toArray(String[]::new)));
        model.put("parts", parts);
        model.put("repair",repair);
        return "EvaluateRepair";
    }

    @RequestMapping(value="/evaluate",method= RequestMethod.POST,params="employeeEvaluateAction=saveEvaluate")
    public String saveevaluate(Repair repairSended,ListImage picture, @ModelAttribute("listPartRepair") ListPartRepair listPartRepair/*,BindingResult result*/,@ModelAttribute("selectedRepairId") Long selectedRepairId, Model model) {
        Repair repair = repairRepository.findOne(selectedRepairId);//TODO to delete selectedRepairId
        Set<Part> partSet = new HashSet<Part>();
        Set<Image> imageSet = new HashSet<Image>();
        for(int i=0;i<listPartRepair.getPartRepair().size();i++) {
            Store store;
            switch(listPartRepair.getPartRepair().get(i).value){
                //case "Empty": store = storeRepository.findOne((long)1);break; bzdura
                case "Engine": store = storeRepository.findOne((long)2);break;
                case "Transmission": store = storeRepository.findOne((long)3);break;
                case "Tires": store = storeRepository.findOne((long)4);break;
                case "Body": store = storeRepository.findOne((long)5);break;
                case "Lights": store = storeRepository.findOne((long)6);break;
                case "Equipment": store = storeRepository.findOne((long)7);break;
                case "Brakes": store = storeRepository.findOne((long)8);break;
                default: store = null;
            }
            Image newImage = new Image(repair,store);
            newImage.setPath(ImageSaver.saveImage(picture.getImageList().get(i),newImage,i));
            imageSet.add(newImage);
            Part newPart = new Part(repair,store);
            partSet.add(newPart);
        }
        repair.setImageSet(imageSet);
        repair.setPartSet(partSet);
        repairRepository.save(repair);
        //jeszcze wydobyc id z repaira a jego przesylac jakos albo autowired i juz
        if(true==repair.getCommission().getClient().getSettings().getAutoPart()) {
            Set<Set<Store>> allToChoose = new HashSet<>();
            for(Part singlePart : repair.getPartSet()){
                Set<Store> storeSet = new HashSet<>();
                switch(singlePart.getStore().getType()){
                    //case "EMPTY": storeSet = storeRepository.findByType("Empty");break;
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
                allToChoose.add(storeSet);
            }
            ArrayList<ChangePart> changeParts = new ArrayList<>();
            for(Part part : repair.getPartSet()) {
                ChangePart partToChange = new ChangePart();
                partToChange.setPartId(part.getId());
                changeParts.add(partToChange);
            }
            ClientChoosenPart clientChoosePart = new ClientChoosenPart();
            clientChoosePart.setChosenPart(changeParts);
            model.addAttribute("clientChoosePart",clientChoosePart);
            model.addAttribute("stores",allToChoose);
            model.addAttribute("repair",repair);
            return "EmployeeEvaluation";
        }
        else
            model.addAttribute("repair",repair);
            return "AutoRedirectSingleRepair";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="EmployeeEvaluateAction=saveRepair")
    public String saveRepair(@ModelAttribute("clientChoosePart") ClientChoosenPart clientChoosePart, BindingResult result) {
        for(ChangePart part:clientChoosePart.chosenPart){
            if(part.getStoreId()!=1) {//obsługa emptowego/defaultowego
                Part partToSave = partRepository.findOne(part.getPartId());
                Store storeToChange = storeRepository.findOne(part.getStoreId());
                partToSave.setStore(storeToChange);
                partRepository.save(partToSave);
            }
        }
        return "redirect:/myRepairs";
    }

    @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=makeRepair")
    public String makeRepair(Map<String, Object> model,@ModelAttribute("selectedRepairId") Long selectedRepairId,@ModelAttribute("part") Part repairPart) {
        if(null!=repairPart.getId()){
            Part repairedPart = partRepository.findOne(repairPart.getId());
            if(repairedPart.getStore().getType().matches("[A-Z]+")) {//it's unique then
                //repairedPart.getStore().setPrice(repairedPart.getStore().getPrice()); already set in front end
                repairedPart.getStore().setAmount(1);
            }
            if(repairedPart.getStore().getAmount()>=1) {
                repairedPart.getStore().setAmount(repairedPart.getStore().getAmount()-1);
                repairedPart.setResolved(true);
                partRepository.save(repairedPart);
            }
            else model.put("storeEmpty","Y");
        }
        Repair repair = repairRepository.findOne(selectedRepairId);
        Set<Part> parts = repair.getPartSet();
        List<Part> partToRepair = new ArrayList<>();
        parts.stream().filter(p -> p.getStore().getId()>8 && p.getResolved()==false).forEach(p->partToRepair.add(p));
        if(partToRepair.isEmpty())
            model.put("NeedRepair","N");
        else{
            model.put("NeedRepair","Y");
            model.put("parts", partToRepair);
        }
        model.put("repair",repair);
        return "RepairInProgress";
    }

    @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=saveUnique")
    public String saveUniquePart(Map<String, Object> model,@ModelAttribute("selectedRepairId") Long selectedRepairId,@ModelAttribute("part") Part repairPart) {
        model.put("part",repairPart);
        model.put("repair",repairRepository.findOne(selectedRepairId));
        return "UniquePartOrder";
    }

        @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=repairDone")
    public String doneRepair(Map<String, Object> model,@ModelAttribute("repair") Repair selectedRepair,boolean noPart) {
        Repair repairedPart = repairRepository.findOne(selectedRepair.getId());
        List<Part> anyToComplete = new ArrayList<>();
        repairedPart.getPartSet().stream().filter(p -> p.getResolved() == false).forEach(p -> anyToComplete.add(p));
        if((!repairedPart.getPartSet().isEmpty() && anyToComplete.isEmpty()) || (repairedPart.getPartSet().isEmpty() && noPart)) {
            model.put("repair",repairedPart);
            return "TimeRepairCost";
            //repairedPart.setAccomplish(true);
            //repairRepository.save(repairedPart);
            //return "redirect:/myRepairs";
        }
        else{
            model.put("repair",repairedPart);
            model.put("selectedRepairId", repairedPart.getId());
            model.put("complete","notAllPartComplete");
            return "RepairSingleView";
        }
    }

    @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=timeEvaluate")
    public String calculateRepair(Map<String, Object> model,@ModelAttribute("repair") Repair selectedRepair){//,@ModelAttribute("repair") int hours) {
        Repair repairedPart = repairRepository.findOne(selectedRepair.getId());
        repairedPart.setAccomplish(true);
        repairedPart.setHours(selectedRepair.getHours());
        repairRepository.save(repairedPart);
        return "redirect:/myRepairs";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/setEmployee",method= RequestMethod.POST,params="adminRepairAction=setEmployee")
    public String setEmployee(Map<String, Object> model,@ModelAttribute("repairs") ListRepair repairs){
        repairs.getRepairList().forEach(r->    {
            Repair repair = repairRepository.findOne(r.getId());
            if(r.getEmployee().getId()!=1L) {
                repair.setEmployee(employeeRepository.findOne(r.getEmployee().getId()));
                repairRepository.save(repair);
            }
        });
        return "redirect:/adminDashboard";
    }

}
