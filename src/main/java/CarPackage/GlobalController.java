package main.java.CarPackage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Mati on 2016-11-01.
 */
@Controller
@RequestMapping("/")
public class GlobalController {
    @RequestMapping(value="/registration",method= RequestMethod.GET)
    public String registration(Model model) {
        return "Register";
    }

    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String login(Model model) {
        return "Login";
    }

    @RequestMapping(method= RequestMethod.GET)
    public String mainView(Model model) {
        return "MainView";
    }

}
