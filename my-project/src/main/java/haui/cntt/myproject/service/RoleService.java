package haui.cntt.myproject.service;

import haui.cntt.myproject.persistance.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    void create(Role role);
}
