package com.ktube.transcoding.service;

import com.ktube.transcoding.dto.InputFileMetaData;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class HlsTranscodingService implements TranscodingService {

    private final FFmpegExecutor executor;

    @Value("${home_dir}")
    private String home_dir;

    @Override
    public String transcode(InputFileMetaData inputFileMetaData) {

        //디렉토리 형식(원본)   : video/{userId}/original/{inputFileName}/
        //디렉토리 형식(인코딩) : video/{userId}/transcode/hls/{inputFileName}/

        String inputFileName = inputFileMetaData.getFileName();
        String outputDirectory = "video/1/hls/";
        String outputFile = "output.m3u8";

        String inputFilePath = home_dir + "/" + inputFileMetaData.getFileDirectory() + "/" + inputFileMetaData.getFileName() + inputFileMetaData.getFileExtension();
        String outFilePath = home_dir + "/" + outputDirectory + outputFile;

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFilePath) //파일 경로 및 파일 이름 , 확장자까지 다 나오도록
                .overrideOutputFiles(true)
                .addOutput(outFilePath) //파일 경로 및 파일 이름 , 확장자까지 다 나오도록
                .setFormat("hls")
                .setStartOffset(0, TimeUnit.MILLISECONDS) // Use null to start immediately
                .addExtraArgs("-hls_time", "10") // Set segment duration
                .addExtraArgs("-hls_segment_type", "mpegts") // Set segment type
                .addExtraArgs("-hls_list_size", "0") // Keep all segments in playlist
                .addExtraArgs("-hls_segment_filename", home_dir + "/" +outputDirectory + "segment%d.ts") // Segment filename pattern
                .done();

        executor.createJob(builder).run();

        return outFilePath;
    }
}
