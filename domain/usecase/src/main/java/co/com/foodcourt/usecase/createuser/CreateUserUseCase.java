package co.com.foodcourt.usecase.createuser;

import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.gateways.UserRepository;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.usecase.util.ValidateUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository ownerRepository;

    public User saveOwner(User user) {
        ValidateUser.validateUser(user);
        user.setRole(Rol.OWNER);
        return ownerRepository.saveUser(user);
    }
}
