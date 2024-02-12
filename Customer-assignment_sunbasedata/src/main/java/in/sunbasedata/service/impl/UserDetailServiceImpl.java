package in.sunbasedata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.sunbasedata.model.User;
import in.sunbasedata.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not found user");
        }
        return new UserDetailsImpl(user);
    }
}
