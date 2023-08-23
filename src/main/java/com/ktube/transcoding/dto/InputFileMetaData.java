package com.ktube.transcoding.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InputFileMetaData {

    private String fileDirectory;
    private String fileName;
    private String fileExtension;
}
