package com.ktube.transcoding.service;


import com.ktube.transcoding.dto.InputFileMetaData;
import org.springframework.stereotype.Service;

@Service
public interface TranscodingService {

    String transcode(InputFileMetaData inputFileMetaData);
}
