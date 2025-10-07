package co.com.foodcourt.jpa.useradapter;


import co.com.foodcourt.model.user.gateways.PasswordEncoderRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderAdapter implements PasswordEncoderRepository {

    private final PasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword){
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword,encodedPassword);
    }


}
