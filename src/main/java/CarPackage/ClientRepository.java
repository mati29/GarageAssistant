package main.java.CarPackage;

/**
 * Created by Mati on 2016-11-01.
 */
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client,Integer>{

    List<Client> findAll();

    //@Override
    //@Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?0", nativeQuery = true)
    //<S extends Client> S save(S s);
}
