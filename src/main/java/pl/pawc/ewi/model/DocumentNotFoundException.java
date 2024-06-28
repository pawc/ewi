package pl.pawc.ewi.model;

public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException(String id){
        super(id);
    }

}