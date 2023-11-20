package com.ktube.transcoding.config;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class TranscodingConfig {

    @Value("${ffmpeg.path}")
    private String ffprobePath; //"/usr/bin/ffprobe"
    @Value("${ffprobe.path}")
    private String ffmpegPath; // "/usr/bin/ffmpeg"
    private String absFfprobePath = Paths.get(ffprobePath).toString();
    private String absFfmpegPath = Paths.get(ffmpegPath).toString();

    @Bean
    public FFprobe fFprobe() throws IOException {

        return new FFprobe(absFfprobePath);
    }

    @Bean
    public FFmpeg fFmpeg() throws IOException {

        return new FFmpeg(absFfmpegPath);
    }

    @Bean
    public FFmpegExecutor executor() throws IOException {

        return new FFmpegExecutor(fFmpeg(), fFprobe());
    }
}
