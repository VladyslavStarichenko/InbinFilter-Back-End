package nure.com.InbinFilter.security.jwt;

import nure.com.InbinFilter.models.user.Role;
import nure.com.InbinFilter.models.user.Status;
import nure.com.InbinFilter.models.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user){
        return new JwtUser(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                mapToGrantedAuthorities(Arrays.asList(user.getRole())),
                user.getStatus().equals(Status.ACTIVE),
                user.getUpdatedAt()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles){
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
