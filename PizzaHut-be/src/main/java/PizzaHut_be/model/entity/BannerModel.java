package PizzaHut_be.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "banner")
public class BannerModel {
    @Id
    private int id;

    private String bannerPositionId;

    private String bannerPositionName;

    private String bannerName;

    private String bannerImageLink;

    private String bannerImageLinkEn;

    private String bannerImageLinkTablet;

    private String bannerImageLinkTableEn;

    private String bannerImageLinkMobile;

    private String bannerImageLinkMobileEn;

    private String bannerDirectionalLink;

    private Boolean outsideLink;
}
