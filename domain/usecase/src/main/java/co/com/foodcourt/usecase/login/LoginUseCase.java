package co.com.foodcourt.usecase.login;

import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.exception.AuthException;
import co.com.foodcourt.model.user.gateways.PasswordEncoderRepository;
import co.com.foodcourt.model.user.gateways.TokenProvider;
import co.com.foodcourt.model.user.gateways.UserRepository;
import co.com.foodcourt.usecase.common.ValidationMessages;
import co.com.foodcourt.usecase.exception.ValidationException;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoderRepository passwordEncoderRepository;

    public String login(String email, String password){
        User user= userRepository.findByEmail(email);

        if(!passwordEncoderRepository.matches(password,user.getPassword())){
            throw new AuthException(ValidationMessages.INVALID_AUTH.getMessage());
        }

        return tokenProvider.generateToken(user.getUserId(),user.getRole().name(), user.getEmail());
    }
}
