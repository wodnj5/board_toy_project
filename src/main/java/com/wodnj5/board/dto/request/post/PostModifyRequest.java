package com.wodnj5.board.dto.request.post;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostModifyRequest {

    private String title;
    private String contents;
    private List<Long> uploadedFileIds;
    private List<MultipartFile> multipartFiles;

}
