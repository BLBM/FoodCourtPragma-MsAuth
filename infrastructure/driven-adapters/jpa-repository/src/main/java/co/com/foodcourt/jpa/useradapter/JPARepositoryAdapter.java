package co.com.foodcourt.jpa.useradapter;

import co.com.foodcourt.jpa.entity.UserEntity;
import co.com.foodcourt.jpa.helper.AdapterOperations;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.gateways.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JPARepositoryAdapter extends AdapterOperations<User, UserEntity, String, JPARepository>
implements UserRepository
{

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public User saveUser(User user) {
        return null;
    }
}
