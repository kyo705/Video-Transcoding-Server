package com.ktube.transcoding.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktube.transcoding.dto.InputFileMetaData;
import com.ktube.transcoding.error.NotExistingOriginalFileException;
import com.ktube.transcoding.service.HlsTranscodingService;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class TranscodingControllerTest {

    private static final String TRANSCODING_URI = "/api/video/transcoding";
    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private FFmpeg fFmpeg;
    @MockBean private FFmpegExecutor executor;
    @MockBean private HlsTranscodingService transcodingService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @DisplayName("정상적인 요청시 200 상태 코드를 리턴한다.")
    @Test
    public void testWithValidRequest() throws Exception {

        //given
        InputFileMetaData inputFileMetaData = TranscodingControllerSetup.validInputFileMetadata();
        String requestBody = objectMapper.writeValueAsString(inputFileMetaData);
        String outputFilePath = "tempFilePath";
        given(transcodingService.transcode(any())).willReturn(outputFilePath);

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(TRANSCODING_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(result -> {
                    String jsonResultBody = result.getResponse().getContentAsString();
                    List<String> outputFilePaths = objectMapper.readValue(jsonResultBody, new TypeReference<List<String>>() {});
                    assertThat(outputFilePaths.size()).isEqualTo(1);
                    assertThat(outputFilePaths.get(0)).isEqualTo(outputFilePath);
                });
    }

    @DisplayName("실제 존재하지 않는 원본 파일에 대한 요청이 들어올 경우 400 상태코드를 리턴한다.")
    @Test
    public void testWithNotExistingInputFilePath() throws Exception {

        //given
        InputFileMetaData inputFileMetaData = TranscodingControllerSetup.validInputFileMetadata();
        String requestBody = objectMapper.writeValueAsString(inputFileMetaData);
        given(transcodingService.transcode(any())).willThrow(new NotExistingOriginalFileException("원본 파일이 존재하지 않습니다"));

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(TRANSCODING_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("허용되지 않는 원본 파일 확장자로 요청이 들어올 경우 400 상태코드를 리턴한다.")
    @Test
    public void testWithInvalidFileFormat() throws Exception {

        //given
        InputFileMetaData inputFileMetaData = TranscodingControllerSetup.invalidInputFileMetadata();
        String requestBody = objectMapper.writeValueAsString(inputFileMetaData);

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(TRANSCODING_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("트랜스코딩 진행 중 실패한 경우 500 상태코드를 리턴한다.")
    @Test
    public void testWithRuntimeTranscodingError() throws Exception {

        //given
        InputFileMetaData inputFileMetaData = TranscodingControllerSetup.validInputFileMetadata();
        String requestBody = objectMapper.writeValueAsString(inputFileMetaData);

        given(transcodingService.transcode(any())).willThrow(RuntimeException.class);


        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(TRANSCODING_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
