package main.java.CarPackage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.*;

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
    public String addCommission(Store store,Principal principal) {
        storeRepository.save(store);
        return "redirect:/employeeDashboard";
    }

}
