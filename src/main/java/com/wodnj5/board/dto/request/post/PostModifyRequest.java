package com.wodnj5.board.dto.request.post;

import java.util.List;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostModifyRequest {

    @NonNull
    private String title;
    @NonNull
    private String contents;
    private List<Long> uploadedFileIds;
    private List<MultipartFile> multipartFiles;

}
