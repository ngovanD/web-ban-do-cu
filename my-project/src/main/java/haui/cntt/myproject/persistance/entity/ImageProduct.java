package haui.cntt.myproject.persistance.entity;

import haui.cntt.myproject.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class ImageProduct extends BaseEntity {
    @Column(name = "fileName")
    private String fileName;

    @Column(name = "is_main_image")
    private boolean isMainImage;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
