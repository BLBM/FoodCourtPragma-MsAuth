package co.com.foodcourt.usecase.createuser;

import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.gateways.UserRepository;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.usecase.util.ValidateUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;

    public User saveOwner(User user) {
        user.setRole(Rol.OWNER);
        ValidateUser.validateUser(user);
        return userRepository.saveUser(user);
    }

    public User getUser(Long userId){
        return userRepository.findByIdWithRole(userId);
    }
}
