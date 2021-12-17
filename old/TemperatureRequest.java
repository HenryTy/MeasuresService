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

package com.service.measures.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * TemperatureRequest
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-12-17T10:43:35.462286600+01:00[Europe/Warsaw]")
public class TemperatureRequest {
  @JsonProperty("from")
  private OffsetDateTime from = null;

  @JsonProperty("to")
  private OffsetDateTime to = null;

  @JsonProperty("roomNr")
  private Integer roomNr = null;

  public TemperatureRequest from(OffsetDateTime from) {
    this.from = from;
    return this;
  }

   /**
   * Get from
   * @return from
  **/
  public OffsetDateTime getFrom() {
    return from;
  }

  public void setFrom(OffsetDateTime from) {
    this.from = from;
  }

  public TemperatureRequest to(OffsetDateTime to) {
    this.to = to;
    return this;
  }

   /**
   * Get to
   * @return to
  **/
  public OffsetDateTime getTo() {
    return to;
  }

  public void setTo(OffsetDateTime to) {
    this.to = to;
  }

  public TemperatureRequest roomNr(Integer roomNr) {
    this.roomNr = roomNr;
    return this;
  }

   /**
   * Get roomNr
   * @return roomNr
  **/
  public Integer getRoomNr() {
    return roomNr;
  }

  public void setRoomNr(Integer roomNr) {
    this.roomNr = roomNr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemperatureRequest temperatureRequest = (TemperatureRequest) o;
    return Objects.equals(this.from, temperatureRequest.from) &&
        Objects.equals(this.to, temperatureRequest.to) &&
        Objects.equals(this.roomNr, temperatureRequest.roomNr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, roomNr);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemperatureRequest {\n");

    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    roomNr: ").append(toIndentedString(roomNr)).append("\n");
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