package com.soprasteria.modelling.api.context;

import com.soprasteria.modelling.framework.exception.AuthException;
import com.soprasteria.modelling.framework.exception.AuthoException;

public class APIError {
    private String errorCode = "0";
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public APIError() {
    }

    public APIError(Exception e) {
        if (e instanceof AuthException) errorCode = "2";
        if (e instanceof AuthoException) errorCode = "3";
        else errorCode = "1";

        if (e instanceof NullPointerException) errorMsg = "something is missing.";
        else errorMsg = e.getMessage();
    }

}
