package main.java.CarPackage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mati on 2017-03-11.
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {

    @RequestMapping(method= RequestMethod.GET)
    public String getCommission(Map<String, Object> model){
        return "SettingsView";
    }
}
