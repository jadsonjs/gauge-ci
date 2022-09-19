package br.com.jadson.gaugeci.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class MathUtils {

    public static final int SCALE = 4;

    /**
     * Calc the sum of a list of values.
     * @param values
     * @return
     */
    public BigDecimal sumOfValues(List<BigDecimal> values) {

        BigDecimal total = BigDecimal.ZERO;

        for(int count = 0; count < values.size() ; count++){
            total = total.add(values.get( count));
        }
        return total;
    }

    /**
     * Calculate the average of values of specific record
     *
     * @return
     */
    public BigDecimal meanOfValues(List<BigDecimal> values) {

        // precondition
        if(values.size() == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal sum = sumOfValues(values);
        return sum.divide(new BigDecimal( values.size() ), SCALE, RoundingMode.HALF_UP );
    }

    /**
     * Median just for specific values
     * @param values
     * @return
     */
    public BigDecimal medianOfValues(List<BigDecimal> values) {

        // precondition
        if(values.size() == 0){
            return BigDecimal.ZERO;
        }

        List<BigDecimal> tempList = new LinkedList<BigDecimal>();

        for(int count = 0; count < values.size() ; count++){
            tempList.add(values.get( count ));
        }

        Collections.sort(tempList);

        BigDecimal median = BigDecimal.ZERO;
        // if old, it the the average of the 2 in the middle
        if (tempList.size() % 2 == 0)
            median = ( ( tempList.get(tempList.size()/2) )
                    .add(   tempList.get(tempList.size()/2-1)  )
            ).divide(new BigDecimal(2), SCALE, RoundingMode.HALF_UP);
        else {
            // If even, it is the middle element
            median = tempList.get(tempList.size() / 2);
        }

        return median;
    }
}
