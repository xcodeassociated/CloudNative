package io.spring.start.javanative.reservationclient;

public class DefaultErrorCode implements ErrorCodeInterface {
    private final String errorMessage;
    private final Integer errorCode;

    DefaultErrorCode(DefaultErrorCodeBuilder defaultErrorCodeBuilder) {
        this.errorMessage = defaultErrorCodeBuilder.errorMessage;
        this.errorCode = defaultErrorCodeBuilder.errorCode;
    }

    static class DefaultErrorCodeBuilder {
        final String errorMessage;
        final Integer errorCode;

        public DefaultErrorCodeBuilder(String errorMessage, Integer errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

        public DefaultErrorCode build() {
            return new DefaultErrorCode(this);
        }
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public Integer getCode() {
        return this.errorCode;
    }
}
