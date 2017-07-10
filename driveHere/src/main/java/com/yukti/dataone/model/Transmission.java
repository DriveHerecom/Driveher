package com.yukti.dataone.model;

/**
 * Created by admin on 1/18/2016.
 */
public class Transmission
{
    public String msrp, marketing_name, name, brand, invoice_price, type,
            gears, order_code, detail_type, availability, transmission_id;

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDetail_type() {
        return detail_type;
    }

    public void setDetail_type(String detail_type) {
        this.detail_type = detail_type;
    }

    public String getGears() {
        return gears;
    }

    public void setGears(String gears) {
        this.gears = gears;
    }

    public String getInvoice_price() {
        return invoice_price;
    }

    public void setInvoice_price(String invoice_price) {
        this.invoice_price = invoice_price;
    }

    public String getMarketing_name() {
        return marketing_name;
    }

    public void setMarketing_name(String marketing_name) {
        this.marketing_name = marketing_name;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getTransmission_id() {
        return transmission_id;
    }

    public void setTransmission_id(String transmission_id) {
        this.transmission_id = transmission_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
