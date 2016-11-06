package main.java.CarPackage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mati on 2016-11-05.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
}
