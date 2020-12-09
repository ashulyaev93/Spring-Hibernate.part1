package ru.geekbrains.lesson3.hibernate.entity;

import javax.persistence.*;

@Entity
@Table(name = "simple_items")
@NamedQuery(name = "SimpleItem.findAll", query = "FROM SimpleItem ")
public class SimpleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private int price;

    @Transient
    private String value;

    public SimpleItem() {

    }

    public SimpleItem(String title, int price) {
        this.title = title;
        this.price = price;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("SimpleItem [id = %d, title = %s, price = %d]", id, title, price);
    }
}

