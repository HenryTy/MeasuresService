/*
 * Measures Service
 * Micro service to get temperature and power measures in building
 *
 * OpenAPI spec version: 1.0.0
 * Contact: supportm@measures.pl
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.service.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * MeasuresResponseMeasures
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-12-17T10:43:35.462286600+01:00[Europe/Warsaw]")
public class MeasuresResponseMeasures {
  @JsonProperty("date")
  private OffsetDateTime date = null;

  @JsonProperty("temperature")
  private BigDecimal temperature = null;

  @JsonProperty("power")
  private BigDecimal power = null;

  public MeasuresResponseMeasures date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

   /**
   * Get date
   * @return date
  **/
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public MeasuresResponseMeasures temperature(BigDecimal temperature) {
    this.temperature = temperature;
    return this;
  }

   /**
   * Get temperature
   * @return temperature
  **/
  public BigDecimal getTemperature() {
    return temperature;
  }

  public void setTemperature(BigDecimal temperature) {
    this.temperature = temperature;
  }

  public MeasuresResponseMeasures power(BigDecimal power) {
    this.power = power;
    return this;
  }

   /**
   * Get power
   * @return power
  **/
  public BigDecimal getPower() {
    return power;
  }

  public void setPower(BigDecimal power) {
    this.power = power;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasuresResponseMeasures measuresResponseMeasures = (MeasuresResponseMeasures) o;
    return Objects.equals(this.date, measuresResponseMeasures.date) &&
        Objects.equals(this.temperature, measuresResponseMeasures.temperature) &&
        Objects.equals(this.power, measuresResponseMeasures.power);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, temperature, power);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MeasuresResponseMeasures {\n");

    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    temperature: ").append(toIndentedString(temperature)).append("\n");
    sb.append("    power: ").append(toIndentedString(power)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
