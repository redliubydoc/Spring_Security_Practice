package com.raj.dto;

import java.util.Map;

public class GenericResponseDTO {

    private Object data;
    private boolean isError;
    private Map<String, Object> error;

    public GenericResponseDTO() {}

    public GenericResponseDTO(Object data, boolean isError, Map<String, Object> error) {
        this.data = data;
        this.isError = isError;
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean getIsError() {
        return isError;
    }

    public void setIsError(boolean error) {
        isError = error;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GenericResponseDTO[data=" + data + ", isError=" + isError + ", error=" + error + "]";
    }
}
