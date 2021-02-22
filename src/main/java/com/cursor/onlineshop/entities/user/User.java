package com.cursor.onlineshop.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "fName")
    private String firstName;
    @Column(name = "lName")
    private String lastName;
    private Integer age;
    private String phoneNumber;

    public User(String accountId) {
        this.accountId = accountId;
    }
}
