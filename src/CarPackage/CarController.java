package CarPackage;

/**
 * Created by Mati on 2016-10-23.
 */
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class CarController {
    private CarRepository carRepository;

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

    public String submit(Car car) {
        carRepository.save(car);
        return "redirect:/";
    }
}
