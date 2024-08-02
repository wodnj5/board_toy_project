package com.wodnj5.board.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostDto {

    private Long id;
    private String title;
    private String contents;
    private List<String> fileIds;
    private List<MultipartFile> files;

}
