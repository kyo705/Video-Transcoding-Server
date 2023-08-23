package com.ktube.transcoding.config;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;

@Configuration
public class TranscodingConfig {

    private static final String FFMPEG_EXE_PATH = "/usr/bin/ffmpeg";


    @Bean
    public FFmpeg fFmpeg() throws IOException {

        return new FFmpeg(FFMPEG_EXE_PATH);
    }

    @Bean
    public FFmpegExecutor executor() throws IOException {

        return new FFmpegExecutor(fFmpeg());
    }
}
