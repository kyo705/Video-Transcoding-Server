package com.ktube.transcoding.service;

import com.ktube.transcoding.dto.InputFileMetaData;
import com.ktube.transcoding.error.NotExistingOriginalFileException;
import com.ktube.transcoding.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.ktube.transcoding.constants.constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class HlsTranscodingService implements TranscodingService {

    private final FFmpegExecutor executor;
    private final FileRepository fileRepository;

    @Value("${home_dir}")
    private final String home_dir;

    @Override
    public String transcode(InputFileMetaData inputFileMetaData) {

        //원본 파일 경로       : /video/{userId}/{videoId}/original/
        //hls 변환된 파일 경로 : /video/{userId}/videoId}/transcoding/hls/

        String originalDirectory = home_dir + inputFileMetaData.getCommonVideoFileDirectory() + ORIGINAL_DIR_PREFIX;
        String hlsDirectory = home_dir + inputFileMetaData.getCommonVideoFileDirectory() + HLS_DIR_PREFIX;

        String inputFilePath = originalDirectory + inputFileMetaData.getFileName() + inputFileMetaData.getFileFormat();
        String outFilePath = hlsDirectory + OUTPUT_FILE_NAME;

        beforeTranscoding(inputFilePath, hlsDirectory);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFilePath) //파일 경로 및 파일 이름 , 확장자까지 다 나오도록
                .overrideOutputFiles(true)
                .addOutput(outFilePath) //파일 경로 및 파일 이름 , 확장자까지 다 나오도록
                .setFormat("hls")
                .setStartOffset(0, TimeUnit.MILLISECONDS) // Use null to start immediately
                .addExtraArgs("-hls_time", "10") // Set segment duration
                .addExtraArgs("-hls_segment_type", "mpegts") // Set segment type
                .addExtraArgs("-hls_list_size", "0") // Keep all segments in playlist
                .addExtraArgs("-hls_segment_filename", hlsDirectory + "segment%d.ts") // Segment filename pattern
                .done();

        log.info("Transcoding is started now.");
        executor.createJob(builder).run();
        log.info("Transcoding is completed now.");

        return outFilePath;
    }

    private void beforeTranscoding(String inputFilePath, String outputDirectoryPath) {

        if(!fileRepository.isExistingFile(inputFilePath)) {
            log.error("File name : {} is not exist.", inputFilePath);
            throw new NotExistingOriginalFileException(inputFilePath);
        }
        if(fileRepository.isExistingDirectory(outputDirectoryPath)) {
            log.info("Directory : {} is existing", outputDirectoryPath);
            return;
        }
        log.info("Directory : {} is not existing. creating now", outputDirectoryPath);
        fileRepository.createDirectory(outputDirectoryPath);
    }
}
