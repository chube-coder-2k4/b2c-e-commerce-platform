package dev.commerce.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private String name;
    private String slug;
    // name ở đây là tên danh mục
    // slug là tên định danh dùng trong URL
    // ví dụ: name = "Điện thoại", slug = "dien-thoai"
    // Các trường này sẽ được sử dụng khi tạo hoặc cập nhật danh mục sản phẩm
    // ví dụ khi tạo sẽ có url /api/categories/dien-thoai
}
