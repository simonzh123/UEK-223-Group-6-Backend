package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractEntity;
import com.example.demo.domain.customlist.ListEntry;
import com.example.demo.domain.role.Role;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class User extends AbstractEntity {

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password")
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
             inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<ListEntry> listEntries = new HashSet<>();

  public User(UUID id, String firstName, String lastName, String email, String password, Set<Role> roles) {
    super(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.roles = roles;
  }

}
