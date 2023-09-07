package com.mindhub.Homebanking.configurations;

import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.Role;
import com.mindhub.Homebanking.repositories.ClientRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


//indicates that the class is going to make configurations
@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {
    //GlobalAuthenticationConfigurerAdapter is the object that Spring Security
    // uses to know how it will look up user detail
    @Autowired
    private ClientRepository clientRepository;


    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputName-> {

            Client client = clientRepository.findByEmail(inputName);


            if (client != null) {

                //.endsWith()
                //ensures that you are specifically checking if the desired domain
                // is present at the end of the email address and not in other parts of the chain.
                if(isAdminEmail(client.getEmail()) & client.getRole().equals(Role.ADMIN)){

                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("ADMIN"));

                }else {

                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("CLIENT"));
                }


            } else {

                throw new UsernameNotFoundException("Unknown client: " + inputName);

            }

        });

    }

    @Bean
    public PasswordEncoder passwordEncoder(){return PasswordEncoderFactories.createDelegatingPasswordEncoder();}

    // Method to check if the email belongs to an administrator
    public boolean isAdminEmail(String email) {

        return email.endsWith("admin@mindhubbank.com");

    }
}
