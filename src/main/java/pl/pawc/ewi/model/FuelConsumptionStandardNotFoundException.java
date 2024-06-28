package pl.pawc.ewi.model;

public class FuelConsumptionStandardNotFoundException extends Exception {

    public FuelConsumptionStandardNotFoundException(long id){
        super(String.valueOf(id));
    }

}