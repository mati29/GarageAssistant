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
    private RepairService repairService;
    private EmployeeService employeeService;
    private PartService partService;

    @Autowired
    public RepairController(EmployeeService employeeService, RepairService repairService,PartService partService) {
        this.employeeService = employeeService;
        this.repairService = repairService;
        this.partService = partService;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String getRepairs(Map<String, Object> model, Principal principal) {
        model.put("repairs",repairService.getActiveRepairForEmployeeFromUsername(principal.getName()));
        return "RepairsView";
    }

    @RequestMapping(method=RequestMethod.POST,params="userBackAction")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String back() {
        return "redirect:/employeeDashboard";
    }

    @RequestMapping(value="/addRepair",method= RequestMethod.GET)
    public String repairs(Map<String, Object> model,Principal principal,RedirectAttributes redirectAttrs) {
        model.put("employees", employeeService.getAllWorkers());
        model.put("repairs",repairService.getActiveRepairForEmployeeFromUsername(principal.getName()));//check if not problematic change from employeeRepository.findOne(account.getEmployee().getId()) for account.getEmployee()
        return "NewRepair";
    }

    @RequestMapping(value="/addRepair", method= RequestMethod.POST,params="employeeAddAction=addRepair")
    public String addRepair(Long commissionId,String description, Long employeeId,HttpServletRequest request) {
        repairService.addRepair(commissionId,description,employeeId);
        String backToCalledFrom = backTo(request.getSession().getAttribute("from").toString());
        request.getSession().removeAttribute("from");
        return backToCalledFrom;
    }

    private String backTo(String from){
        if(from.equals("dashboard")) {
            return "redirect:/employeeDashboard";
        }
        else {
            return "redirect:/myRepairs";
        }
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSelectAction=selectRepair")
    public String selectCommission(@Valid @ModelAttribute("repairSended") Repair repair, BindingResult result, Model model) {
        model.addAttribute("repair",repairService.getRepairFromId(repairService.getRepairId(repair)));
        model.addAttribute("selectedRepairId", repairService.getRepairId(repair));
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
        model.put("listPartRepair",repairService.createListPartRepair(count));
        model.put("parts", Arrays.asList(Arrays.stream(TypePart.values()).map(TypePart::name).toArray(String[]::new)));
        model.put("repair",repair);
        return "EvaluateRepair";
    }

    @RequestMapping(value="/evaluate",method= RequestMethod.POST,params="employeeEvaluateAction=saveEvaluate")
    public String saveevaluate(ListImage picture, @ModelAttribute("listPartRepair") ListPartRepair listPartRepair,@ModelAttribute("selectedRepairId") Long selectedRepairId, Model model) {
        if(repairService.evaluateRepair(selectedRepairId,listPartRepair,picture)) {//saveRepair and return autoPart value
            List<Part> partList = repairService.getPartFromRepair(repairService.getRepairFromId(selectedRepairId));
            model.addAttribute("clientChoosePart",new ClientChoosenPart(partService.getChangePartList(partList)));
            model.addAttribute("stores",partService.allPartToChose(partList));
            model.addAttribute("repair",repairService.getRepairFromId(selectedRepairId));
            return "EmployeeEvaluation";
        }
        else
            model.addAttribute("repair",repairService.getRepairFromId(selectedRepairId));
            return "AutoRedirectSingleRepair";
    }

    @RequestMapping(value="/evaluate", method= RequestMethod.POST,params="EmployeeEvaluateAction=saveRepair")
    public String saveRepair(@ModelAttribute("clientChoosePart") ClientChoosenPart clientChoosePart, BindingResult result) {
        repairService.savePartInRepair(clientChoosePart.chosenPart);
        return "redirect:/myRepairs";
    }

    @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=makeRepair")
    public String makeRepair(Map<String, Object> model,@ModelAttribute("selectedRepairId") Long selectedRepairId,@ModelAttribute("part") Part repairPart) {
        if(partService.isReadyForRepair(repairPart) && !partService.isSavePartProperly(repairPart)){
            model.put("storeEmpty","Y");
        }
        List<Part> partsToRepair = partService.partToRepair(repairService.getPartFromRepair(repairService.getRepairFromId(selectedRepairId)));
        if(repairService.isRepairNeeded(partsToRepair)) {
            model.put("NeedRepair", "Y");
            model.put("parts", partsToRepair);

        }
        else
            model.put("NeedRepair", "N");

        model.put("repair",repairService.getRepairFromId(selectedRepairId));
        return "RepairInProgress";
    }

    @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=saveUnique")
    public String saveUniquePart(Map<String, Object> model,@ModelAttribute("selectedRepairId") Long selectedRepairId,@ModelAttribute("part") Part repairPart) {
        model.put("part",repairPart);
        model.put("repair",repairService.getRepairFromId(selectedRepairId));
        return "UniquePartOrder";
    }

        @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=repairDone")
    public String doneRepair(Map<String, Object> model,@ModelAttribute("repair") Repair selectedRepair,boolean noPart) {
        if(repairService.repairCouldBeFinished(repairService.getPartFromRepair(repairService.getRepairFromId(repairService.getRepairId(selectedRepair))),partService.inResolvingPart(repairService.getRepairId(selectedRepair)),noPart)){
            model.put("repair",repairService.getRepairFromId(repairService.getRepairId(selectedRepair)));
            return "TimeRepairCost";
        }
        else{
            model.put("repair",repairService.getRepairFromId(repairService.getRepairId(selectedRepair)));
            model.put("selectedRepairId",repairService.getRepairId(selectedRepair));
            model.put("complete","notAllPartComplete");
            return "RepairSingleView";
        }
    }

    @RequestMapping(value="/repair",method= RequestMethod.POST,params="employeeRepairAction=timeEvaluate")
    public String calculateRepair(Map<String, Object> model,@ModelAttribute("repair") Repair selectedRepair){//,@ModelAttribute("repair") int hours) {
        repairService.calculateRepair(selectedRepair);
        return "redirect:/myRepairs";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/setEmployee",method= RequestMethod.POST,params="adminRepairAction=setEmployee")
    public String setEmployee(Map<String, Object> model,@ModelAttribute("repairs") ListRepair repairs){
        repairService.setEmployeesForFreeRepairs(repairs);
        return "redirect:/adminDashboard";
    }

}
