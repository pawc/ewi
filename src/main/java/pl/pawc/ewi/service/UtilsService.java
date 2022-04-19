package pl.pawc.ewi.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UtilsService {

    double myRound(double d, boolean precisionMode){
        if(precisionMode){
            return (double) Math.round(Math.floor(d*1000)/10)/100;
        }
        else{
            return (double) Math.round(Math.floor(d*100)/10)/10;
        }
    }

}