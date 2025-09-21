package com.github.gomestkd.startup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "person")
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String address;

    @NotBlank
    @Column(nullable = false, length = 6)
    private String gender;

    @NotNull
    @Column(nullable = false)
    private Boolean enabled = true;

    @Size(max = 255)
    @Column(name = "wikipedia_profile_url", length = 255)
    private String wikipediaProfileUrl;

    @Size(max = 255)
    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_books",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    public Person() {}

    public Person(Long id, String firstName, String lastName, String address,
                  String gender, Boolean enabled,
                  String wikipediaProfileUrl, String photoUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
        this.enabled = enabled;
        this.wikipediaProfileUrl = wikipediaProfileUrl;
        this.photoUrl = photoUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public String getWikipediaProfileUrl() { return wikipediaProfileUrl; }
    public void setWikipediaProfileUrl(String wikipediaProfileUrl) { this.wikipediaProfileUrl = wikipediaProfileUrl; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public Set<Book> getBooks() { return books; }
    public void setBooks(Set<Book> books) { this.books = books; }

    public String printBooks() {
        if (books == null || books.isEmpty()) {
            return "No books";
        }
        return books.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", enabled=" + enabled +
                ", wikipediaProfileUrl='" + wikipediaProfileUrl + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", books=[" + printBooks() + "]" +
                '}';
    }
}
