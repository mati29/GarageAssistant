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
    public String registration() {
        return "Register";
    }

    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String login() {
        return "Login";
    }

    @RequestMapping(method= RequestMethod.GET)
    public String mainView() {
        return "MainView";
    }
    @RequestMapping(value="/",method=RequestMethod.POST,params="userBackAction")
    public String back() {
        return "redirect:/";
    }

}
