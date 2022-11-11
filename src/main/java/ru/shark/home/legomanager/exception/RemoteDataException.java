package ru.shark.home.legomanager.exception;

public class RemoteDataException extends RuntimeException {
    private Integer code;

    public RemoteDataException(Integer code, String error) {
        super(error);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
