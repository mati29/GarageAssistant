package main.java.GarageAssistantApp.ConfigurationPackage;

import main.java.GarageAssistantApp.EntityPackage.Account;
import main.java.GarageAssistantApp.EntityPackage.Settings;
import main.java.GarageAssistantApp.RepositoriesPackage.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpSession;

/**
 * Created by Mati on 2017-03-11.
 */
public class AfterLogging implements ApplicationListener<AuthenticationSuccessEvent> {

        @Autowired
        private HttpSession session;
        @Autowired
        private AccountRepository accountRepository;
        @Override
        public void onApplicationEvent(AuthenticationSuccessEvent event) {
            SimpleGrantedAuthority authority = (SimpleGrantedAuthority)event.getAuthentication().getAuthorities().iterator().next();
            if("ROLE_USER".equals(authority.getAuthority())){
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
                Object principal = token.getPrincipal();
                if (principal instanceof User) {
                    User user = (User) token.getPrincipal();
                    Account account = accountRepository.findByUsername(user.getUsername());
                    Settings settings = account.getClient().getSettings();
                    session.setAttribute("AP", settings.getAutoPart());
                    session.setAttribute("AM", settings.getAutoMechanic());
                    session.setAttribute("AS", settings.getAdditionalService());
                    session.setAttribute("EP", settings.getCallExtraPart());
                }
            }
        }

}
