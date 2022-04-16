package haui.cntt.myproject.presentation.controller.admin;

import haui.cntt.myproject.presentation.mapper.PropertyMapper;
import haui.cntt.myproject.presentation.request.PropertyRequest;
import haui.cntt.myproject.presentation.response.PropertyResponse;
import haui.cntt.myproject.service.Impl.PropertyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/property")
public class PropertyController {

    @Autowired
    PropertyServiceImpl propertyService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody PropertyRequest propertyRequest) {
        propertyService.create(PropertyMapper.convertToProperty(propertyRequest));
        return ResponseEntity.ok().body("Tạo thuộc tính loại sản phẩm thành công !!!");
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PropertyResponse>> getAll() {
        List<PropertyResponse> propertyResponseList = propertyService.getAll()
                .stream()
                .map(PropertyMapper::convertToPropertyResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(propertyResponseList);
    }

    @GetMapping("/get-all-property")
    public String getAllProperty(Model model) {
        List<PropertyResponse> propertyResponseList = propertyService.getAll()
                .stream()
                .map(PropertyMapper::convertToPropertyResponse)
                .collect(Collectors.toList());
        model.addAttribute("propertyResponseList", propertyResponseList);
        return "admin_list_property";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") long propertyId) {
        propertyService.delete(propertyId);
        return ResponseEntity.ok().body("Xóa thuộc tính loại sản phẩm thành công !!!");
    }
}
