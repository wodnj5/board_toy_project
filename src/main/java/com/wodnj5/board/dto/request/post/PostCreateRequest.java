package com.wodnj5.board.dto.request.post;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostCreateRequest {

    private String title;
    private String contents;
    private List<MultipartFile> multipartFiles;

}
