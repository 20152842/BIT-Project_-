package lastcoder.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lastcoder.dao.UserRepository;
import lastcoder.dto.PrincipalDetails;
import lastcoder.dto.SiteUser;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
//            return new PrincipalDetails((user)userEntity);
        }
        return null;
    }
}