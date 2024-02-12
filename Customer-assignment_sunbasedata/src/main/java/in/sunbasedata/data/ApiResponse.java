package in.sunbasedata.data;

import lombok.Data;
import org.springframework.validation.FieldError;

import java.util.List;

@Data
public class ApiResponse {
    private Boolean success;
    private String message;
    private int status;
    private Object responseObject;
    private List<FieldError> fieldValidationErrors;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(List<FieldError> fieldValidationErrors) {
        this.fieldValidationErrors = fieldValidationErrors;
        this.success = false;
    }

    public ApiResponse(Object responseObject) {
        this.responseObject = responseObject;
        this.success = true;
    }

    public ApiResponse(String message) {
        this.message=message;
        this.success = true;
    }

    public ApiResponse(Object responseObject, Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.responseObject = responseObject;
    }

    public ApiResponse(Boolean success, String message, int status) {
        this.success = success;
        this.message = message;
        this.status = status;
    }

}

