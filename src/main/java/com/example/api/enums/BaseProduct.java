package com.example.api.enums;

/**
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
public enum BaseProduct {

    FLIGHTS("FLIGHTS"),
    HOTELS("HOTELS"),
    CARS("CARS"),
    ALL("ALL");
    
    private String type;
    
    private BaseProduct(String type){
        this.type = type;
    }
    
    @Override
    public String toString() {
        return this.type;
    }
        
}
