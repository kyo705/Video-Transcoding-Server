package com.ktube.transcoding.controller;

import com.ktube.transcoding.dto.InputFileMetaData;
import com.ktube.transcoding.service.TranscodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class TranscodingController {

    private final List<TranscodingService> transcodingServices;

    @PostMapping("/api/video/transcoding")
    public List<String> transcode(@RequestBody InputFileMetaData inputFileMetaData) {

        //validate(inputFileMetaData);

        return transcodingServices
                .stream()
                .map(service -> service.transcode(inputFileMetaData))
                .collect(Collectors.toList());
    }
}
