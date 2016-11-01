package main.java.CarPackage;

/**
 * Created by Mati on 2016-11-01.
 */
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client,Integer>{

    List<Client> findAll();

}
