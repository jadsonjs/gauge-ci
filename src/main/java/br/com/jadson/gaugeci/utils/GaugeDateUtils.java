package br.com.jadson.gaugeci.utils;

import br.com.jadson.gaugeci.model.UnitOfTime;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class GaugeDateUtils {

    public LocalDateTime toLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return formatter.format(date1).equals(formatter.format(date2));
    }

    public BigDecimal scaleTime(long seconds, UnitOfTime unit) {
        switch (unit){
            case SECONDS:
                return new BigDecimal(seconds);
            case MINUTES:
                return new BigDecimal(seconds).divide( new BigDecimal(60), GaugeMathUtils.SCALE, RoundingMode.HALF_UP );
            case HOURS:
                return new BigDecimal(seconds).divide( new BigDecimal(60*60), GaugeMathUtils.SCALE, RoundingMode.HALF_UP );
            case DAYS:
                return new BigDecimal(seconds).divide( new BigDecimal(24*60*60), GaugeMathUtils.SCALE, RoundingMode.HALF_UP );
        }
        return new BigDecimal(seconds);
    }

}
