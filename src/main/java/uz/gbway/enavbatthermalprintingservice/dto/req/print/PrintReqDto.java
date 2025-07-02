package uz.gbway.enavbatthermalprintingservice.dto.req.print;

import lombok.Getter;

@Getter
public class PrintReqDto {
    private String product;
    private String price;

    // Getters & setters
    public String getProduct() { return product; }

    public void setProduct(String product) { this.product = product; }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }
}

