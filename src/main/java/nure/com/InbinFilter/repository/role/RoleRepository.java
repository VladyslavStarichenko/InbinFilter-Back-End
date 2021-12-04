package nure.com.InbinFilter.repository.role;


import nure.com.InbinFilter.models.user.Role;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RoleRepository extends PagingAndSortingRepository<Role,Long> {
    Role findByName(String name);
}