package pl.apibooks.books.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import pl.apibooks.security.authorization.SecurityUserDetails;
import pl.apibooks.security.client_resources.service.ClientResourceService;
import pl.apibooks.security.helpers.LoggedUserDetails;
import pl.apibooks.security.oauth.dto.Domains;
import pl.apibooks.security.oauth.dto.OAuth2UserDetails;

import java.util.List;

@Slf4j
@Service
public class SecurityUserDetailsImpl extends SecurityUserDetails {
    @Autowired
    ClientResourceService clientResourceService;

    @Override
    public LoggedUserDetails loadUserByUsernameAndDomain(String userName, Domains domains) {
        log.info(userName);
        OAuth2UserDetails details = clientResourceService.getUserDetailsByUsernameAndDomain(userName, domains);
        return new LoggedUser(details.getFirstName(), details.getLastName(), userName, domains.getValue(), loadAuthoritiesByUserNameAndDomain(userName, domains));
    }

    @Override
    public List<GrantedAuthority> loadAuthoritiesByUserNameAndDomain(String userName, Domains domain) {
        log.info(userName);
        return clientResourceService.getGlobalAuthoritiesByUsernameAndDomain(userName, domain);
    }
}
