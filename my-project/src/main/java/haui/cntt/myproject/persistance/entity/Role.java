package haui.cntt.myproject.persistance.entity;

import haui.cntt.myproject.base.BaseEntity;
import haui.cntt.myproject.common.enum_.RoleEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 10)
    private RoleEnum name;
}
