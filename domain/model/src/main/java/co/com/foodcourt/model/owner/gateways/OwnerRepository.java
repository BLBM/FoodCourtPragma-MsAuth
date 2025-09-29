package co.com.foodcourt.model.owner.gateways;

import co.com.foodcourt.model.owner.Owner;

public interface OwnerRepository {
    Owner saveOwner(Owner owner);
}
