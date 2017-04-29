package main.java.GarageAssistantApp.ControllersPackage;

import main.java.GarageAssistantApp.EntityPackage.Store;
import main.java.GarageAssistantApp.DTOPackage.ListDTO.ListStore;
import main.java.GarageAssistantApp.ServicesPackage.StoreService;
import main.java.GarageAssistantApp.StandardPackage.TypePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Mati on 2016-12-23.
 */
@Controller
@RequestMapping("/store")
public class StoreController {
    private StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    @RequestMapping(value="/addPart",method= RequestMethod.GET)
    public String parts(Map<String, Object> model) {
        model.put("parts", Arrays.asList(Arrays.stream(TypePart.values()).map(TypePart::name).toArray(String[]::new)));
        return "NewPart";
    }

    @RequestMapping(value="/addPart", method= RequestMethod.POST,params="employeeAddAction=addPart")
    public String addCommission(Store store, HttpServletRequest request) {
        storeService.saveStore(store);
        String backToCalledFrom = backTo(request.getSession().getAttribute("from").toString());
        request.getSession().removeAttribute("from");
        return backToCalledFrom;
    }

    private String backTo(String from){
        if(from.equals("dashboard")) {
            return "redirect:/employeeDashboard";
        }
        else {
            return "redirect:/store";
        }
    }

    @RequestMapping(method= RequestMethod.GET)
    public String getStore(Map<String, Object> model) {
        model.put("stores",storeService.getListStore());
        return "StoresView";
    }

    @RequestMapping(method=RequestMethod.POST, params="employeeAction=addPart")
    public String addPartStore(RedirectAttributes redirectAttrs,HttpServletRequest request) {
        redirectAttrs.addFlashAttribute("from","overview");
        request.getSession().setAttribute("from","overview");
        return "redirect:/store/addPart";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortById")
    public String sortById(Map<String, Object> model,ListStore stores) {
        storeService.storeSortById(storeService.getStoreFromList(stores));//czy posortuje to czy tylko wewnatrz
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByModel")
    public String sortByModel(Map<String, Object> model,ListStore stores) {
        storeService.storeSortByModel(storeService.getStoreFromList(stores));
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByBrand")
    public String sortByBrand(Map<String, Object> model,ListStore stores) {
        storeService.storeSortByBrand(storeService.getStoreFromList(stores));
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByPrice")
    public String sortByPrice(Map<String, Object> model,ListStore stores) {
        storeService.storeSortByPrice(storeService.getStoreFromList(stores));
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByAmount")
    public String sortByAmount(Map<String, Object> model,ListStore stores) {
        storeService.storeSortByAmount(storeService.getStoreFromList(stores));
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByType")
    public String sortByType(Map<String, Object> model,ListStore stores) {
        storeService.storeSortByType(storeService.getStoreFromList(stores));
        model.put("stores",stores);
        return "StoresView";
    }

}
