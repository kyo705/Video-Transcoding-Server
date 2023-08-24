package com.ktube.transcoding.dto;

import com.ktube.transcoding.validation.InputVideoFileFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class InputFileMetaData {

    @NotEmpty
    private String commonVideoFileDirectory;
    @NotEmpty
    private String fileName;
    @NotEmpty @InputVideoFileFormat
    private String fileFormat;
}
