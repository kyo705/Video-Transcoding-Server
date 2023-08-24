package com.ktube.transcoding.controller;

import com.ktube.transcoding.dto.InputFileMetaData;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TranscodingControllerSetup {

    protected static InputFileMetaData validInputFileMetadata() {

        InputFileMetaData inputFileMetaData = new InputFileMetaData();
        inputFileMetaData.setFileFormat(".mp4");
        inputFileMetaData.setFileName("originalFile");
        inputFileMetaData.setCommonVideoFileDirectory("/video/1/1");

        return inputFileMetaData;
    }

    protected static InputFileMetaData invalidInputFileMetadata() {

        InputFileMetaData inputFileMetaData = new InputFileMetaData();
        inputFileMetaData.setFileFormat(".img");
        inputFileMetaData.setFileName("originalFile");
        inputFileMetaData.setCommonVideoFileDirectory("/video/1/1");

        return inputFileMetaData;
    }
}
