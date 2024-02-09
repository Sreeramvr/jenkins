package com.strand.app.config;

import org.json.simple.JSONObject;

public class ResponseDataGeneric<T>
{
    public int error_code;
    public String status;
    public T data;

    public ResponseDataGeneric()
    {
        error_code = -1;
        status = "";
    }

    public ResponseDataGeneric(int ErrorCode, String Status, T Data)
    {
        SetError(ErrorCode, Status, Data);
    }

    public void SetError(int ErrorCode, String Status, T Data)
    {
  
        error_code = ErrorCode;
        status = Status;
        data = Data;
    }

    public void SetSuccess(String Status, T Data)
    {
        error_code = 0;
        status = Status;
        data = Data;
    }
    public void SetSuccessJson(String Status, JSONObject Data)
    {
        error_code = 0;
        status = Status;
        data = (T) Data;
    }
 
}