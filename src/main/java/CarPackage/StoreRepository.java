package main.java.CarPackage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mati on 2016-12-23.
 */
public interface StoreRepository extends CrudRepository<Store, Long> {
    public Set<Store> findByType(String type);
}
