package co.com.foodcourt.usecase.createowner;

import co.com.foodcourt.model.owner.Owner;
import co.com.foodcourt.model.owner.gateways.OwnerRepository;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.usecase.util.ValidateUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateOwnerUseCase {

    private final OwnerRepository ownerRepository;

    public Owner saveOwner(Owner owner) {
        ValidateUser.validateUser(owner);
        owner.setRole(Rol.OWNER);
        return ownerRepository.saveOwner(owner);
    }
}
