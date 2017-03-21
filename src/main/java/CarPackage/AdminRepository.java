package main.java.CarPackage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mati on 2017-03-21.
 */
@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {
}
