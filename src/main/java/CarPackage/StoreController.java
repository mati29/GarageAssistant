package main.java.CarPackage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mati on 2016-12-23.
 */
@Controller
@RequestMapping("/store")
public class StoreController {
    private StoreRepository storeRepository;

    public StoreController(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    @RequestMapping(value="/addPart",method= RequestMethod.GET)
    public String parts(Map<String, Object> model) {
        //String[] toParts = Arrays.stream(TypePart.values()).map(TypePart::name).toArray(String[]::new);
        Set<String> parts = new HashSet<String>(Arrays.asList(Arrays.stream(TypePart.values()).map(TypePart::name).toArray(String[]::new)));
        model.put("parts", parts);
        return "NewPart";
    }

    @RequestMapping(value="/addPart", method= RequestMethod.POST,params="employeeAddAction=addPart")
    public String addCommission(Store store,Principal principal,HttpServletRequest request) {
        storeRepository.save(store);
        String from = request.getSession().getAttribute("from").toString();
        if(from.equals("dashboard")) {
            request.getSession().removeAttribute("from");
            return "redirect:/employeeDashboard";
        }
        else {
            request.getSession().removeAttribute("from");
            return "redirect:/store";
        }
    }

    @RequestMapping(method= RequestMethod.GET)
    public String getStore(Map<String, Object> model) {
        List<Store> stores = storeRepository.findAll();                         //unique not approve by Employee!
        stores = stores.stream().filter(s -> !s.getType().matches("[A-Z]*") && s.getPrice()!=0D).collect(Collectors.toList());
        stores.sort(new StoreComparator());
        ListStore storeList = new ListStore();
        storeList.setStoreList(stores);
        model.put("stores",storeList);
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
        stores.getStoreList().sort(new Comparator<Store>() {
            @Override
            public int compare(Store store1, Store store2) {
                if(store1.getId()>store2.getId())
                    return 1;
                if(store1.getId()<store2.getId())
                    return -1;
                else return 0;
            }
        });
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByModel")
    public String sortByModel(Map<String, Object> model,ListStore stores) {
        stores.getStoreList().sort(new Comparator<Store>() {
            @Override
            public int compare(Store store1, Store store2) {
                return store1.getModel().compareTo(store2.getModel());
            }
        });
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByBrand")
    public String sortByBrand(Map<String, Object> model,ListStore stores) {
        stores.getStoreList().sort(new Comparator<Store>() {
            @Override
            public int compare(Store store1, Store store2) {
                return store1.getBrand().compareTo(store2.getBrand());
            }
        });
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByPrice")
    public String sortByPrice(Map<String, Object> model,ListStore stores) {
        stores.getStoreList().sort(new Comparator<Store>() {
            @Override
            public int compare(Store store1, Store store2) {
                if(store1.getPrice()>store2.getPrice())
                    return 1;
                if(store1.getPrice()==store2.getPrice())
                    return 0;
                else
                    return -1;
            }
        });
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByAmount")
    public String sortByAmount(Map<String, Object> model,ListStore stores) {
        stores.getStoreList().sort(new Comparator<Store>() {
            @Override
            public int compare(Store store1, Store store2) {
                if(store1.getAmount()>store2.getAmount())
                    return 1;
                if(store1.getAmount()==store2.getAmount())
                    return 0;
                else
                    return -1;
            }
        });
        model.put("stores",stores);
        return "StoresView";
    }

    @RequestMapping(method= RequestMethod.POST,params="employeeSortAction=sortByType")
    public String sortByType(Map<String, Object> model,ListStore stores) {
        stores.getStoreList().sort(new StoreComparator());
        model.put("stores",stores);
        return "StoresView";
    }

}
