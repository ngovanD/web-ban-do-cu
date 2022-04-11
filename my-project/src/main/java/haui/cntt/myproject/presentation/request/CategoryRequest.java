package haui.cntt.myproject.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    private long id;

    private String name;

    private String slug;

    private long categoryParentId;

    private List<Long> propertyRequestList;
}
