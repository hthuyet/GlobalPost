package com.global.webapp.models;

import java.util.Date;

public class Test {
  private String name;
  private Date birthday;
  private float payment;
  private int bonus;


  public Test() {
  }

  public Test(String name, Date birthday, float payment, int bonus) {
    this.name = name;
    this.birthday = birthday;
    this.payment = payment;
    this.bonus = bonus;
  }

  public Test(int bonus) {
    Date date = new Date();
    date.setDate(bonus);
    this.name = "name "+bonus;
    this.birthday = date;
    this.payment = Float.parseFloat(String.valueOf(bonus));
    this.bonus = bonus;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public float getPayment() {
    return payment;
  }

  public void setPayment(float payment) {
    this.payment = payment;
  }

  public int getBonus() {
    return bonus;
  }

  public void setBonus(int bonus) {
    this.bonus = bonus;
  }
}
