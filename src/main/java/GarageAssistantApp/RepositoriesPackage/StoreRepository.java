package main.java.GarageAssistantApp.RepositoriesPackage;

import main.java.GarageAssistantApp.EntityPackage.Store;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Mati on 2016-12-23.
 */
public interface StoreRepository extends CrudRepository<Store, Long> {
    public List<Store> findByType(String type);
    public List<Store> findAll();
}
