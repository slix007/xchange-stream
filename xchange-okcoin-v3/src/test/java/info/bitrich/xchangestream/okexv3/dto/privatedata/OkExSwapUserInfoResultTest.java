package info.bitrich.xchangestream.okexv3.dto.privatedata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.helper.WsObjectMapperHelper;
import info.bitrich.xchangestream.okexv3.OkExAdapters;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfoContracts;
import org.knowm.xchange.utils.Assert;

public class OkExSwapUserInfoResultTest {

    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = WsObjectMapperHelper.getObjectMapper();
    }

    @Test
    public void testParsing() throws IOException {

        JsonNode s = objectMapper.readTree(ClassLoader.getSystemClassLoader().getResourceAsStream("account.json"));

        JsonNode data = s.get("data");
        for (JsonNode item : data) {
            OkExSwapUserInfoResult parsed = objectMapper.treeToValue(item, OkExSwapUserInfoResult.class);
            System.out.println(parsed);
            AccountInfoContracts converted = OkExAdapters.adaptSwapUserInfo(Currency.LINK, parsed);
            System.out.println(converted);
            Assert.notNull(converted, "converted account info is not null");
        }
    }
}