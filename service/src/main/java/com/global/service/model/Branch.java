package com.global.service.model;

import com.global.jdbc.SsdcEntity;

import javax.persistence.*;

@Entity
public class Branch extends SsdcEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  public String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
