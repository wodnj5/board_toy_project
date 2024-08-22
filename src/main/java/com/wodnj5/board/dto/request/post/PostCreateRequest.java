package com.wodnj5.board.dto.request.post;

import java.util.List;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostCreateRequest {

    @NonNull
    private String title;
    @NonNull
    private String contents;
    private List<MultipartFile> multipartFiles;

}
