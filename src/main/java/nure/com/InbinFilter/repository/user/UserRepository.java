package nure.com.InbinFilter.repository.user;



import nure.com.InbinFilter.models.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {


    Optional<User> findUserByUserName(String name);

    boolean existsByUserName(String userName);



}
