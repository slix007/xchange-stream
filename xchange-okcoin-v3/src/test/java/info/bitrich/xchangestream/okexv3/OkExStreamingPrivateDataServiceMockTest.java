package info.bitrich.xchangestream.okexv3;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.core.dto.Position;
import info.bitrich.xchangestream.okexv3.dto.InstrumentDto;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkExStreamingPrivateDataServiceMockTest {

    @Mock
    private OkCoinStreamingService streamingService;
    @Mock
    private OkExStreamingExchange exchange;
    @Mock
    private InstrumentDto instrumentDto;
    private OkExStreamingPrivateDataService dataService;

    @Before
    public void setUp() throws Exception {
        exchange = (OkExStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(OkExStreamingExchange.class.getName());
        dataService = new OkExStreamingPrivateDataService(streamingService, exchange);
//        System.setProperty("log4j2.configurationFile", "./log4j2.xml");

//        final Logger logger = LogManager.getLogger(OkExStreamingMarketDataServiceMockTest.class.getName());
    }


    @Test
    public void getPosition() throws IOException {
        // Given order book in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(ClassLoader.getSystemClassLoader().getResourceAsStream("position.json"));

        when(streamingService.subscribeChannel(any())).thenReturn(Observable.just(jsonNode));
        when(streamingService.doLogin(any())).thenReturn(Completable.complete());

//        Date createdAt = Date.from(Instant.parse("2019-03-15T10:59:51.000Z"));

        // Call get order book observable
//        final InstrumentDto instrumentDto = new InstrumentDto(CurrencyPair.BTC_USD, FuturesContract.ThisWeek);
        when(instrumentDto.getInstrumentId()).thenReturn("BTC-USD-190322");
        TestObserver<Position> test = dataService.getPositionObservable(instrumentDto).test();

        test.awaitTerminalEvent();

        System.out.println(test.values());
        test.assertValueCount(1);
        test.assertNoErrors();

        final Position expected = new Position(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(0.0),
                BigDecimal.ZERO,
                BigDecimal.valueOf(3869.42),
                BigDecimal.valueOf(3868.56),
                instrumentDto.getInstrumentId(),
                "");

        test.assertResult(expected);

    }
}