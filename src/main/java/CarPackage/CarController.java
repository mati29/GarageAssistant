package main.java.CarPackage;

/**
 * Created by Mati on 2016-10-23.
 */
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/myCars")
public class CarController {
    private CarRepository carRepository;//trzeba bedzie autowired user employee np.

    @Autowired
    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String getCars(Map<String, Object> model) {
        List<Car> cars = carRepository.findAll();
        model.put("cars", cars);
        return "CarView";
    }

    @RequestMapping(method=RequestMethod.POST,params="userAction=save")
    public String submit(Car car) {
        carRepository.save(car);
        return "redirect:/myCars";
    }

    @RequestMapping(method=RequestMethod.POST,params="userAction=back")//w zaleznosci czy admin itd. do ktorego ma dostep zabl metod wedlug role!
    public String back() {
        return "redirect:/employeeDashboard";
    }
}
