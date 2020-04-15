package net.dreamfteam.quiznet.exception.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private Long timestamp;
    private Integer statusCode;
    private String error;
    private String message;
    private String requestPath;
}
