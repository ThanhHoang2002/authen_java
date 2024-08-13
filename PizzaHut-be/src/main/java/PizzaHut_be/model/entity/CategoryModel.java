package PizzaHut_be.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class CategoryModel {
    @Id
    private int id;

    private String categoryCode;

    private String categoryNameVn;

    private String categoryNameEn;

    private String photoLink;

    private String photoLinkEn;

    private String orderBy;

    private Boolean component;

    private Boolean defaultCate;

    private Boolean homePageMenu;
}
