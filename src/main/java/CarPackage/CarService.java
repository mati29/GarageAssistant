package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Mati on 2017-04-25.
 */
@Service
public class CarService {

    private CarRepository carRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    public void setCommission(Car car,Commission commission){
        car.setCommission(commission);
    }

    public void saveCar(Car car){
        carRepository.save(car);
    }
}
