package com.yukti.dataone.model;

/**
 * Created by admin on 1/18/2016.
 */
public class Pricing
{
    public String msrp, gas_guzzler_tax, invoice_price, destination_charge;

    public String getDestination_charge() {
        return destination_charge;
    }

    public void setDestination_charge(String destination_charge) {
        this.destination_charge = destination_charge;
    }

    public String getGas_guzzler_tax() {
        return gas_guzzler_tax;
    }

    public void setGas_guzzler_tax(String gas_guzzler_tax) {
        this.gas_guzzler_tax = gas_guzzler_tax;
    }

    public String getInvoice_price() {
        return invoice_price;
    }

    public void setInvoice_price(String invoice_price) {
        this.invoice_price = invoice_price;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }
}
