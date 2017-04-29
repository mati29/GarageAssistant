package main.java.GarageAssistantApp.RepositoriesPackage;

import main.java.GarageAssistantApp.EntityPackage.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mati on 2016-11-05.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByUsername(String username);
    List<Account> findByEnabled(boolean enabled);
}
