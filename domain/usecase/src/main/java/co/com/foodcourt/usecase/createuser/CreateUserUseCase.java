package co.com.foodcourt.usecase.createuser;

import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.gateways.UserRepository;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.usecase.util.ValidateUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;

    private User saveWithRole(User user, Rol role) {
        ValidateUser.validateUser(user);
        user.setRole(role);
        return userRepository.saveUser(user);
    }

    public User saveOwner(User user) {
        return saveWithRole(user, Rol.OWNER);
    }

    public User saveEmployee(User user) {
        return saveWithRole(user, Rol.EMPLOYEE);
    }

    public User getUser(Long userId){
        return userRepository.findByIdWithRole(userId);
    }
}
