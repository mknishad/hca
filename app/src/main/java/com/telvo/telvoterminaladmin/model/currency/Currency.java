package com.telvo.telvoterminaladmin.model.currency;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Dell on 09-Mar-17.
 */

public class Currency implements Serializable {
    List<CurrencyData> CcyNtry;

    public List<CurrencyData> getCcyNtry() {
        return CcyNtry;
    }

    public void setCcyNtry(List<CurrencyData> ccyNtry) {
        CcyNtry = ccyNtry;
    }
}
