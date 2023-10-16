package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(appliesTo = "m_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 0,message = "Имя не может быть пустым")
    private String firstName;
    @Size(min = 0,message = "Фамилия не может быть пустым")
    private String secondName;
    private String lastNAme;
    @Size(min=5, message = "Не меньше 5 знаков")
    private String password;
    @Transient
    private String passwordConfirm;
    private boolean is_actual;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
