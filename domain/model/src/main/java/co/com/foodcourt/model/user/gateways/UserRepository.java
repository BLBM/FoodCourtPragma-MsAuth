package co.com.foodcourt.model.user.gateways;

import co.com.foodcourt.model.user.User;

public interface UserRepository {
    User saveUser(User user);
    User findByIdWithRole(Long userId);
}
