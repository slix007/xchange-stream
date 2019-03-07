package info.bitrich.xchangestream.okexv3.dto.request;

import java.util.Arrays;
import java.util.List;

public class LoginDto extends RequestDto {

    private LoginDto(OP op, List<String> args) {
        super(op, args);
    }

    public static LoginDto create(String apiKey, String passphrase, String timestamp, String sign) {
        List<String> args = Arrays.asList(apiKey, passphrase, timestamp, sign);
        return new LoginDto(OP.login, args);
    }

}
