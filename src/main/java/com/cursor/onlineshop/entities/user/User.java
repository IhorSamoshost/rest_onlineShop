package com.cursor.onlineshop.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "fName")
    private String firstName;

    @Column(name = "lName")
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "phone_number")
    private String phoneNumber;

    public User(String accountId) {
        this.accountId = accountId;
    }
}
