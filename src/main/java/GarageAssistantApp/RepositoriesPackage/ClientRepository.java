package main.java.GarageAssistantApp.RepositoriesPackage;

/**
 * Created by Mati on 2016-11-01.
 */
import main.java.GarageAssistantApp.EntityPackage.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ClientRepository extends CrudRepository<Client,Long>{

    ArrayList<Client> findAll();
}
