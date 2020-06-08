package com.mmking.framework.exception;

import com.mmking.framework.model.response.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@Getter //只能拿不拿取
@ToString
public class MMkingException extends  RuntimeException {

    private ResultCode resultCode;

}
