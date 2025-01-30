package com.app.orderprocessing.models;

public enum OrderStatus {
  PLACED("placed"),
  CANCELLED("cancelled");

  private final String status;

  OrderStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}

