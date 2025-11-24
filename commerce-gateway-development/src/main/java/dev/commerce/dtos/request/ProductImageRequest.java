package dev.commerce.dtos.request;

import jakarta.mail.Multipart;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductImageRequest {
    private boolean isPrimary;
    private MultipartFile file;

}
