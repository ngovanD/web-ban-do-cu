package haui.cntt.myproject.service.Impl;

import haui.cntt.myproject.persistance.entity.Role;
import haui.cntt.myproject.persistance.repository.RoleRepository;
import haui.cntt.myproject.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void create(Role role) {
        roleRepository.save(role);
    }
}
