package ru.geekbrains.lesson3.hibernate.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "persons_details")
public class PersonDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column
    private String phone;

    // Если в этом классе убрать поле person, то получится однонаправленная связь: персона
    // сможет ссылаться на свои детали, а детали нет.
    // В данном же случае прописана двунаправленная связь
    @OneToOne(mappedBy = "personDetails")
    private Person person;

    public PersonDetails() {
    }

    public PersonDetails(String description, String phone) {
        this.description = description;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDetails that = (PersonDetails) o;
        return id.equals(that.id) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "PersonDetails{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}

