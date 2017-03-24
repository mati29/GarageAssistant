package main.java.CarPackage;

/**
 * Created by Mati on 2016-11-01.
 */
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public interface ClientRepository extends CrudRepository<Client,Long>{

    ArrayList<Client> findAll();
}
