package pl.pawc.ewi.model;

public class NormaNotFoundException extends Exception{

    public NormaNotFoundException(long id){
        super(String.valueOf(id));
    }

}