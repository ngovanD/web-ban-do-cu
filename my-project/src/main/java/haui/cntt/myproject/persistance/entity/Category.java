package haui.cntt.myproject.persistance.entity;

import com.github.slugify.Slugify;
import haui.cntt.myproject.base.BaseEntity;
import haui.cntt.myproject.common.text.VNCharacterUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Category extends BaseEntity {

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
    private Category categoryParent;

    @PrePersist
    public void onPrePersist() {
        super.onPrePersist();
        slug = new Slugify().slugify(VNCharacterUtil.removeAccent(name));
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "category_properties", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "property_id"))
    private Collection<Property> properties;

//    @OneToMany(targetEntity = ProductCategoryEntity.class, mappedBy = "category")
//    private Collection<ProductCategoryEntity> productCategories;
}
