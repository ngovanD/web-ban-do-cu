package haui.cntt.myproject.persistance.entity;

import com.github.slugify.Slugify;
import haui.cntt.myproject.base.BaseEntity;
import haui.cntt.myproject.common.enum_.ProductStatusEnum;
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
public class Product extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private ProductStatusEnum status;

    @Column(name = "slug")
    private String slug;

    @Column(name = "current_status")
    private String currentStatus;

    @Column(name = "price")
    private long price;

    @Column(name = "description")
    private String description;

    @Column(name = "view")
    private int view;

    @OneToOne(mappedBy = "product")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private Collection<ImageProduct> imageProducts;

    @OneToMany(mappedBy = "product")
    private Collection<ProductProperty> productProperties;

    @PreUpdate
    public void onPreUpdate(){
        slug = new Slugify().slugify(VNCharacterUtil.removeAccent(name) + this.getId());
    }
}
