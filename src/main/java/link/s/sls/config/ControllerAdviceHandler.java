package link.s.sls.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class ControllerAdviceHandler {
    /**
     *  拦截自定义异常
     * @param e 自定义异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> resultException(Exception e){
        e.printStackTrace();
        log.error("错误信息:" + e.getMessage());
        JSONObject param = new JSONObject();
        //@Validated @RequestBody 参数绑定验证
        if (e instanceof WebExchangeBindException){
            WebExchangeBindException bindException = (WebExchangeBindException) e;
            BindingResult bindingResult = bindException.getBindingResult();
            String message = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            param.put("message", message);
            log.error("数据绑定异常：{}",message);
            return new ResponseEntity<>(param, HttpStatus.BAD_REQUEST);
        }
        param.put("message", e.getMessage());
        return new ResponseEntity<>(param, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /**
     * 自定义数据绑定格式化
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators();

    }


}
