package info.bitrich.xchangestream.okexv3.dto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.okcoin.FuturesContract;

public class InstrumentDto {

    private final CurrencyPair currencyPair;
    private final FuturesContract futuresContract;

    public InstrumentDto(CurrencyPair currencyPair, FuturesContract futuresContract) {
        this.currencyPair = currencyPair;
        this.futuresContract = futuresContract;
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public FuturesContract getFuturesContract() {
        return futuresContract;
    }

    public String getInstrumentId() {
        return getInstrumentId(currencyPair, getExpDate());
    }

    /**
     * Example: "BTC-USD-190329"
     */
    public static String getInstrumentId(CurrencyPair currencyPair, String expDate) {
        final StringBuilder channelBuilder = new StringBuilder();
        final String base = currencyPair.base.getCurrencyCode();
        final String counter = currencyPair.counter.getCurrencyCode();
        channelBuilder.append(base);
        channelBuilder.append("-");
        channelBuilder.append(counter);
        channelBuilder.append("-");
        channelBuilder.append(expDate);
        return channelBuilder.toString();
    }

    /**
     * Examples: 190907, 190831
     * or SWAP
     */
    private String getExpDate() {
        if (futuresContract == FuturesContract.Swap) {
            return "SWAP";
        }

        final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return getExpString(now, true);
    }

    @SuppressWarnings("SameParameterValue")
    private String getExpString(LocalDateTime now, boolean withYear) {
        // The last four digits of a futures contract name indicate the delivery date.
        // The form is (20180928)(month,date).Contracts will be settled every Friday at 800 AM UTC (400 PM China CST).
        LocalDateTime expTime;
        if (futuresContract == FuturesContract.ThisWeek) {
            // weekly BTC0831 ->
            expTime = LocalDateTime.of(2018, 8, 31, 8, 0, 0);
            while (now.isAfter(expTime)) {
                expTime = expTime.plusDays(7);
            }
        } else if (futuresContract == FuturesContract.NextWeek) {
            // bi-weekly  - always next week! BTC0907 ->
            expTime = LocalDateTime.of(2018, 8, 31, 8, 0, 0);
            while (now.isAfter(expTime)) {
                expTime = expTime.plusDays(7);
            }
            expTime = expTime.plusDays(7);
        } else if (futuresContract == FuturesContract.Quarter) {
            // Quarterly BTC0928 ->
            expTime = LocalDateTime.of(2018, 9, 28, 8, 0, 0);
            // use next Time when bi-weekly==Quarterly
            final LocalDateTime plus2Weeks = now.plusDays(14);
            while (plus2Weeks.isAfter(expTime)) {
                expTime = expTime.plusDays(28 * 3 + 7);
            }
        } else if (futuresContract == FuturesContract.BiQuarter) {
            expTime = LocalDateTime.of(2018, 9, 28, 8, 0, 0);
            final LocalDateTime plus2Weeks = now.plusDays(14);
            while (plus2Weeks.isAfter(expTime)) {
                expTime = expTime.plusDays(28 * 3 + 7);
            }
            expTime = expTime.plusDays(28 * 3 + 7);
        } else {
            throw new IllegalArgumentException("Illegal futuresContract " + futuresContract);
        }
        int monthValue = expTime.getMonthValue();
        int dayOfMonth = expTime.getDayOfMonth();
        if (withYear) {
            final String yearTwoDigits = String.valueOf(expTime.getYear()).substring(2);
            return String.format("%s%s%s",
                    yearTwoDigits,
                    monthValue < 10 ? "0" + monthValue : monthValue,
                    dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth
            );
        }
        return String.format("%s%s",
                monthValue < 10 ? "0" + monthValue : monthValue,
                dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth
        );
    }
}
