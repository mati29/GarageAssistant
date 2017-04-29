package main.java.GarageAssistantApp.ControllersPackage;

import main.java.GarageAssistantApp.DTOPackage.ListDTO.ClientChoosenPart;
import main.java.GarageAssistantApp.DTOPackage.ListDTO.UniquePartList;
import main.java.GarageAssistantApp.DTOPackage.SimpleDTO.UniquePart;
import main.java.GarageAssistantApp.EntityPackage.Car;
import main.java.GarageAssistantApp.EntityPackage.Commission;
import main.java.GarageAssistantApp.EntityPackage.Part;
import main.java.GarageAssistantApp.ServicesPackage.*;
import main.java.GarageAssistantApp.StandardPackage.ImageSaver;
import main.java.GarageAssistantApp.StandardPackage.TypePart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * Created by Mati on 2016-11-21.
 */
@Controller
@RequestMapping("/myCommission")
public class CommissionController {

    private CommissionService commissionService;
    private EmployeeService employeeService;
    private ImageService imageService;
    private StoreService storeService;
    private PartService partService;
    private ImageSaver imageSaver;
    private RepairService repairService;
    private AccountService accountService;

    @Autowired
    public CommissionController(PartService partService,CommissionService commissionService,AccountService accountService,RepairService repairService,ImageSaver imageSaver,EmployeeService employeeService,ImageService imageService,StoreService storeService) {
        this.partService = partService;
        this.commissionService = commissionService;
        this.employeeService = employeeService;
        this.imageService = imageService;
        this.storeService = storeService;
        this.imageSaver = imageSaver;
        this.repairService = repairService;
        this.accountService = accountService;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String getCommission(Map<String, Object> model, Principal principal){
        model.put("commissions",commissionService.getCommissionListByUsername(principal.getName()));
        return "CommissionsView";
    }

    @RequestMapping(value="/addCommission", method= RequestMethod.POST,params="clientAddAction=addCommission")
    public String addCommission(Car newCar, String description, String specialService, Long employeeId, Principal principal, HttpServletRequest request) {
        commissionService.addCommission(new Commission(accountService.getClientFromAccount(accountService.getAccountFromUsername(principal.getName())),newCar,Calendar.getInstance().getTime()),(boolean)request.getSession().getAttribute("AM"),description,specialService,employeeId);
        String backToCalledFrom = backTo(request.getSession().getAttribute("from").toString());
        request.getSession().removeAttribute("from");
        return backToCalledFrom;
    }

    private String backTo(String from){
        if(from.equals("dashboard")) {
            return "redirect:/clientDashboard";
        }
        else {
            return "redirect:/myRepairs";
        }
    }

    @RequestMapping(value="/addCommission",method= RequestMethod.GET)
    public String commissions(Map<String, Object> model,HttpServletRequest request) {
        boolean autoMechanic = (boolean)request.getSession().getAttribute("AM");
        if(autoMechanic){
            model.put("AM", autoMechanic);
        }
        else {
            model.put("employees", employeeService.getMechanicList());
        }
        boolean additionalService = (boolean) request.getSession().getAttribute("AS");
            model.put("AS",additionalService);
        return "NewCommission";
    }

    @RequestMapping(/*value="/addCommission", */method= RequestMethod.POST,params="clientSelectAction=selectCommission")
    public String selectCommission(@Valid @ModelAttribute("commissionSended") Commission commission,BindingResult result, Model model,HttpServletRequest request) {
        model.addAttribute("commission",commissionService.getCommissionById(commissionService.getId(commission)));
        if(repairService.anyPartFromRepairsNeedEvaluation(commissionService.getRepairListFromCommission(commissionService.getCommissionById(commissionService.getId(commission)))))
            model.addAttribute("evaluateNeeded","true");
        boolean autoPart = (boolean)request.getSession().getAttribute("AP");
        if(autoPart)
            model.addAttribute("AP",autoPart);
        boolean additionalService = (boolean)request.getSession().getAttribute("AS");
        if(additionalService)
            model.addAttribute("AS",additionalService);
        if(commissionService.checkBill(commissionService.getCommissionById(commissionService.getId(commission))))
            model.addAttribute("BillExpose",true);
        if(!imageService.getAllImageFromCommission(commissionService.getCommissionById(commissionService.getId(commission))).isEmpty()){
            model.addAttribute("ImagesExpose",true);
        }
        return "CommissionSingleView";
    }

    @RequestMapping(method=RequestMethod.POST,params="clientBackAction=toMyCommission")
    public String backToCommission() {
        return "redirect:/myCommission";
    }

    @RequestMapping(method=RequestMethod.POST,params="clientBackAction=toMyDashboard")
    public String backToDashboard() {
        return "redirect:/clientDashboard";
    }


    @RequestMapping(method=RequestMethod.POST,params="clientAddAction")
    public String addNewCommission(RedirectAttributes redirectAttrs,HttpServletRequest request) {
        redirectAttrs.addFlashAttribute("from","overview");
        request.getSession().setAttribute("from","overview");
        return "redirect:/myCommission/addCommission";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=needToRepair")
    public String needToRepair(@Valid @ModelAttribute Commission commission, Model model) {
        model.addAttribute("images",imageService.getAllImageFromCommission(commissionService.getCommissionById(commissionService.getId(commission))));
        model.addAttribute("commission",commission);
        return "ImagesOfDamage";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=evaluateCommission")
    public String evaluateCommission(@Valid @ModelAttribute Commission commission,Model model,HttpServletRequest request) {
        List<Part> neededParts = repairService.partFromRepairNeedEvaluation(commissionService.getRepairListFromCommission(commissionService.getCommissionById(commissionService.getId(commission))));
        boolean extraPart = (boolean)request.getSession().getAttribute("EP");
        if(extraPart)
            model.addAttribute("stores",partService.allPartToChoseAndUnique(neededParts));
        else
            model.addAttribute("stores",partService.allPartToChose(neededParts));
        model.addAttribute("clientChoosePart",new ClientChoosenPart(partService.getChangePartList(neededParts)));
        model.addAttribute("commission",commission);
        return "ClientEvaluation";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=saveEvaluate")
    public String saveEvaluate(Commission commission,@ModelAttribute("clientChoosePart") ClientChoosenPart clientChoosePart,Model model) {
        ArrayList<UniquePart> uniqueParts = partService.partCanSaveOrUnique(partService.getChangePartListFromClientChoosenPart(clientChoosePart));
        if(!uniqueParts.isEmpty()){
            model.addAttribute("uniquePartList",new UniquePartList(uniqueParts));
            model.addAttribute("commission",commission);
            return "UniquePartRequirement";
        }
        model.addAttribute("commission",commission);
        return "AutoRedirect";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="clientEvaluateAction=addUnique")
    public String addUnique(@ModelAttribute("uniquePartList") UniquePartList uniquePartList) {
        storeService.addUniquePart(partService.getUniquePartsFromUniquePartList(uniquePartList));
        return "redirect:/myCommission";
    }

    @RequestMapping(value="/specialService", method= RequestMethod.POST,params="clientSpecialAction=specialService")
    public String additionalService(@Valid @ModelAttribute Commission commission,Model model) {
        model.addAttribute("partsType",Arrays.asList(Arrays.stream(TypePart.values()).map(TypePart::name).toArray(String[]::new)));
        model.addAttribute("commission",commission);
        return "AdditionalService";
    }

    @RequestMapping(value="/specialService", method= RequestMethod.POST,params="clientSpecialAction=saveSpecialService")
    public String specialSave(@Valid @ModelAttribute Commission commission,String partString) {
        commissionService.addCustomRepair(commissionService.getCommissionById(commissionService.getId(commission)),partString);
        return "redirect:/myCommission";
    }

    @RequestMapping(value = "/images/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {
        return imageSaver.getImageInByteFromImageName(imageName);
    }


}
