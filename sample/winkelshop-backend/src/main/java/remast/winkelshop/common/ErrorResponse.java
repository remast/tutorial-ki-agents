package remast.winkelshop.common;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    private ErrorBody error;

    public ErrorResponse() {
    }

    public ErrorResponse(String code, String message) {
        this.error = new ErrorBody(code, message, new ArrayList<>());
    }

    public ErrorResponse(String code, String message, List<FieldErrorItem> fields) {
        this.error = new ErrorBody(code, message, fields);
    }

    public ErrorBody getError() {
        return error;
    }

    public void setError(ErrorBody error) {
        this.error = error;
    }

    public static class ErrorBody {
        private String code;
        private String message;
        private List<FieldErrorItem> fields;

        public ErrorBody() {
        }

        public ErrorBody(String code, String message, List<FieldErrorItem> fields) {
            this.code = code;
            this.message = message;
            this.fields = fields;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<FieldErrorItem> getFields() {
            return fields;
        }

        public void setFields(List<FieldErrorItem> fields) {
            this.fields = fields;
        }
    }

    public static class FieldErrorItem {
        private String name;
        private String message;

        public FieldErrorItem() {
        }

        public FieldErrorItem(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
