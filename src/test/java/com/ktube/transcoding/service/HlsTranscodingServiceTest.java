package com.ktube.transcoding.service;


import com.ktube.transcoding.dto.InputFileMetaData;
import com.ktube.transcoding.error.NotExistingOriginalFileException;
import com.ktube.transcoding.repository.FileRepository;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ktube.transcoding.constants.constants.HLS_DIR_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HlsTranscodingServiceTest {

    private static final String USER_HOME_PATH = "user_home_path";

    @Mock private FFmpegExecutor fFmpegExecutor;
    @Mock private FFmpegJob fFmpegJob;
    @Mock private FileRepository fileRepository;
    private HlsTranscodingService hlsTranscodingService;

    @BeforeEach
    public void setup() {

        hlsTranscodingService = new HlsTranscodingService(fFmpegExecutor, fileRepository, USER_HOME_PATH);
    }

    @DisplayName("원본 파일이 유효하고 해당 파일의 정확한 경로를 파라미터로 전달할 경우 성공한다.")
    @Test
    public void testWithSuccessParam() {

        //given
        InputFileMetaData inputFileMetaData = getInputFileMetaData();

        given(fFmpegExecutor.createJob(any(FFmpegBuilder.class))).willReturn(fFmpegJob);
        given(fileRepository.isExistingFile(any())).willReturn(true);
        given(fileRepository.isExistingDirectory(any())).willReturn(true);

        //when
        String resultFilePath = hlsTranscodingService.transcode(inputFileMetaData);

        //then
        String expectedResultDirectory = USER_HOME_PATH + inputFileMetaData.getCommonVideoFileDirectory() + HLS_DIR_PREFIX;
        String realResultDirectory = resultFilePath.substring(0, resultFilePath.lastIndexOf("/") + 1);

        assertThat(realResultDirectory).isEqualTo(expectedResultDirectory);
    }

    @DisplayName("요청 값에 적절한 원본 동영상 파일이 없는 경우 예외가 발생한다.")
    @Test
    public void testWithNoFileInRequestDirectory() {

        //given
        InputFileMetaData inputFileMetaData = getInputFileMetaData();

        given(fileRepository.isExistingFile(any())).willReturn(false);

        //when & then
        assertThrows(NotExistingOriginalFileException.class,
                () ->hlsTranscodingService.transcode(inputFileMetaData));
    }

    @DisplayName("output file을 담을 유효한 디렉토리가 없는 경우 유효한 디렉토리를 생성한다.")
    @Test
    public void testWithNotExistingOutputDirectory() {

        //given
        InputFileMetaData inputFileMetaData = getInputFileMetaData();

        given(fFmpegExecutor.createJob(any(FFmpegBuilder.class))).willReturn(fFmpegJob);
        given(fileRepository.isExistingFile(any())).willReturn(true);
        given(fileRepository.isExistingDirectory(any())).willReturn(false);

        //when
        String resultFilePath = hlsTranscodingService.transcode(inputFileMetaData);

        //then
        String expectedResultDirectory = USER_HOME_PATH + inputFileMetaData.getCommonVideoFileDirectory() + HLS_DIR_PREFIX;
        String realResultDirectory = resultFilePath.substring(0, resultFilePath.lastIndexOf("/") + 1);

        assertThat(realResultDirectory).isEqualTo(expectedResultDirectory);
        verify(fileRepository, times(1)).createDirectory(any());
    }

    private static InputFileMetaData getInputFileMetaData() {

        InputFileMetaData inputFileMetaData = new InputFileMetaData();
        inputFileMetaData.setFileFormat(".mp4");
        inputFileMetaData.setFileName("originalFile");
        inputFileMetaData.setCommonVideoFileDirectory("/video/1/1");

        return inputFileMetaData;
    }
}
