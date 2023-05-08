package com.runwithme.runwithme.global.result;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;
import lombok.Getter;


//@ApiModel(description = "결과 응답 데이터")
@Getter
@Data
public class ResultResponseDto {

//    @ApiModelProperty(value = "Http 상태 코드")

    private int status;
//    @ApiModelProperty(value = "Business 상태 코드")
    private String code;
//    @ApiModelProperty(value = "응답 메세지")
    private String message;
//    @ApiModelProperty(value = "응답 데이터")
    private Object data;

    public ResultResponseDto(ResultCode resultCode, Object data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    //전송할 데이터가 있는 경우
    public static ResultResponseDto of(ResultCode resultCode, Object data) {
        return new ResultResponseDto(resultCode, data);
    }

    //전송할 데이터가 없는 경우
    public static ResultResponseDto of(ResultCode resultCode) {
        return new ResultResponseDto(resultCode, "");
    }
}
