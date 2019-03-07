package info.bitrich.xchangestream.core;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.service.ws.WsConnectableService;
import info.bitrich.xchangestream.service.ws.WsConnectionSpec;
import io.reactivex.Completable;
import org.knowm.xchange.ExchangeSpecification;

public interface StreamingExchangeEx extends StreamingExchange {

    /**
     * Register event onDisconnect. It is helpful for re-connections.
     * @return {@link Completable} that completes upon disconnect event.
     */
    Completable onDisconnect();


    /**
     * Returns service that can be used to access account data.
     */
    StreamingTradingService getStreamingTradingService();

    StreamingPrivateDataService getStreamingPrivateDataService();

    /**
     * Returns service that can be used to access account data.
     */
    StreamingAccountService getStreamingAccountService();

    default void applyStreamingSpecification(ExchangeSpecification exchangeSpec, WsConnectableService streamingService) {
        final WsConnectionSpec wsSpec = streamingService.getWsConnectionSpec();
        final Object socksProxyHost = exchangeSpec.getExchangeSpecificParametersItem(SOCKS_PROXY_HOST);
        if (socksProxyHost != null) {
            wsSpec.setSocksProxyHost((String) socksProxyHost);
        }
        final Object socksProxyPort = exchangeSpec.getExchangeSpecificParametersItem(SOCKS_PROXY_PORT);
        if (socksProxyPort != null) {
            wsSpec.setSocksProxyPort((Integer) socksProxyPort);
        }
//        wsSpec.setBeforeConnectionHandler((Runnable) exchangeSpec.getExchangeSpecificParametersItem(ConnectableService.BEFORE_CONNECTION_HANDLER));

        final Boolean accept_all_ceriticates = (Boolean) exchangeSpec.getExchangeSpecificParametersItem(ACCEPT_ALL_CERITICATES);
        if (accept_all_ceriticates != null && accept_all_ceriticates) {
            wsSpec.setAcceptAllCertificates(true);
        }

        final Boolean enable_logging_handler = (Boolean) exchangeSpec.getExchangeSpecificParametersItem(ENABLE_LOGGING_HANDLER);
        if (enable_logging_handler != null && enable_logging_handler) {
            wsSpec.setEnableLoggingHandler(true);
        }
    }

}
