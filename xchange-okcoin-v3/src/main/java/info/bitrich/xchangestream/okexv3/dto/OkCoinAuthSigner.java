package info.bitrich.xchangestream.okexv3.dto;

import info.bitrich.xchangestream.okexv3.sdk.HmacSHA256Base64Utils;
import info.bitrich.xchangestream.service.ws.AuthSigner;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.time.Instant;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;

/**
 * Created by Sergey Shurmin on 6/3/17.
 */
public class OkCoinAuthSigner implements AuthSigner {

    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private final String apikey;
    private final String secretKey;
    private final String passphrase;
    //    private final MessageDigest md;
    private String timestamp;
    private String sign;

//    private final Comparator<Entry<String, String>> comparator = Comparator.comparing(Map.Entry::getKey);

    public OkCoinAuthSigner(String apikey, String secretKey, String passphrase) {

        this.apikey = apikey;
        this.secretKey = secretKey;
        this.passphrase = passphrase;

//        try {
//            md = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Problem instantiating message digest.");
//        }
    }

    public static OkCoinAuthSigner fromExchange(Exchange exchange) {
        final ExchangeSpecification spec = exchange.getExchangeSpecification();
        final Boolean v3AsExtra = (Boolean) spec.getExchangeSpecificParametersItem("okex-v3-as-extra");

        final OkCoinAuthSigner signer;
        if (v3AsExtra != null && v3AsExtra) {
            final String apikey = (String) spec.getExchangeSpecificParametersItem("okex-v3-key");
            final String secretKey = (String) spec.getExchangeSpecificParametersItem("okex-v3-secret");
            final String passphrase = (String) spec.getExchangeSpecificParametersItem("okex-v3-passphrase");
            signer = new OkCoinAuthSigner(apikey, secretKey, passphrase);
        } else {
            final String apikey = spec.getApiKey();
            final String secretKey = spec.getSecretKey();
            final String passphrase = spec.getPassword();
            signer = new OkCoinAuthSigner(apikey, secretKey, passphrase);
        }
        return signer;
    }

    public void sign() {
        this.timestamp = String.valueOf(Instant.now().toEpochMilli() / 1000);

        try {
            // Example of sign :
            // sign=CryptoJS.enc.Base64.stringify(CryptoJS.HmacSHA256(timestamp +'GET'+ '/users/self/verify', secret))
            this.sign = HmacSHA256Base64Utils.sign(timestamp, "GET", "/users/self/verify",
                    "", "", secretKey);
        } catch (final IOException e) {
            throw new RuntimeException("Request get body io exception.", e);
//            throw new APIException("Request get body io exception.", e);
        } catch (final CloneNotSupportedException e) {
            throw new RuntimeException("Hmac SHA256 Base64 Signature clone not supported exception.", e);
        } catch (final InvalidKeyException e) {
            throw new RuntimeException("Hmac SHA256 Base64 Signature invalid key exception.", e);
        }
    }

//    private static char[] encodeHex(byte[] data, char[] toDigits) {
//
//        int l = data.length;
//        char[] out = new char[l << 1];
//        // two characters form the hex value.
//        for (int i = 0, j = 0; i < l; i++) {
//            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
//            out[j++] = toDigits[0x0F & data[i]];
//        }
//        return out;
//    }
//
//    public String digestParams(final Map<String, String> nameValueMap) {
//
//        nameValueMap.remove("sign");
//        nameValueMap.put("api_key", apikey);
//
//        // odd requirements for buy/sell market orders
//        if (nameValueMap.containsKey("type") && nameValueMap.get("type").contains("market")) {
//            if (nameValueMap.get("type").equals("buy_market")) {
//                nameValueMap.remove("amount");
//            } else if (nameValueMap.get("type").equals("sell_market")) {
//                nameValueMap.remove("price");
//            }
//        }
//        final List<Entry<String, String>> nameValueList = new ArrayList<Entry<String, String>>(nameValueMap.entrySet());
//        nameValueList.sort(comparator);
//
//        final Params newParams = Params.of();
//        for (int i = 0; i < nameValueList.size(); i++) {
//            Map.Entry<String, String> param = nameValueList.get(i);
//            newParams.add(param.getKey(), param.getValue());
//        }
//
//        // final String message = newParams.asQueryString() + "&secret_key=" + secretKey;
//        final String message = newParams.toString() + "&secret_key=" + secretKey;
//
//        md.reset();
//
//        byte[] digest = md.digest(message.getBytes(StandardCharsets.UTF_8));
//
//        return String.valueOf(encodeHex(digest, DIGITS_UPPER));
//    }


    public String getApikey() {
        return apikey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }
}