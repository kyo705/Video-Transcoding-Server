package com.ktube.transcoding.handler;

import com.ktube.transcoding.config.ErrorResponseEntitiesConfig.ErrorResponseBody;
import com.ktube.transcoding.error.NotExistingOriginalFileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static com.ktube.transcoding.config.ErrorResponseEntitiesConfig.BAD_REQUEST_ENTITY_NAME;
import static com.ktube.transcoding.config.ErrorResponseEntitiesConfig.INTERNAL_SERVER_ERROR_ENTITY_NAME;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class TranscodingServerExceptionHandler {

    private final Map<String, ResponseEntity<ErrorResponseBody>> responseEntities;

    @ExceptionHandler({NotExistingOriginalFileException.class})
    public ResponseEntity<ErrorResponseBody> handleNotExistingOriginalFileException(NotExistingOriginalFileException ex){

        log.error(ex.getMessage());

        return responseEntities.get(BAD_REQUEST_ENTITY_NAME);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponseBody> handleTranscodingException(RuntimeException ex){

        log.error(ex.getMessage());

        return responseEntities.get(INTERNAL_SERVER_ERROR_ENTITY_NAME);
    }
}
