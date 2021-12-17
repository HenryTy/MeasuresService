package com.service.measures;

import static org.apache.camel.model.rest.RestParamType.body;
import java.math.BigDecimal;
import java.util.List;

import com.service.measures.model.MeasuresRequest;
import com.service.measures.model.MeasuresResponse;
import com.service.measures.model.MeasuresResponseMeasures;
import com.service.measures.model.PowerResponse;
import com.service.measures.temperature.DataRequest;
import com.service.measures.temperature.TemperatureResponse;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeasuresService extends RouteBuilder {

    @Autowired
    MeasureIdentifierService measureIdentifierService;

    @Override
    public void configure() throws Exception {
        gateway();
        temperatures();
        powers();
    }

    private void gateway() {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .contextPath("/api")
                // turn on swagger api-doc
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Measures Getting API")
                .apiProperty("api.version", "1.0.0");

        rest("/measures").description("Measures Service")
                .consumes("application/json")
                .produces("application/json")
                .post("/download").description("Download measures").type(MeasuresRequest.class).outType(MeasuresResponse.class)
                .param().name("body").type(body).description("Time and place for measures").endParam()
                .responseMessage().code(200).message("Measures successfully downloaded").endResponseMessage()
                .to("direct:downloadMeasures");

        from("direct:downloadMeasures").routeId("downloadMeasures")
                .log("downloadMeasures fired")
                .process((exchange) -> {
                    exchange.getMessage().setHeader("measuresId",
                            measureIdentifierService.getMeasureIdentifier());
                })
                .to("direct:MeasuresRequest")
                .to("direct:measuresRequester");

        from("direct:measuresRequester").routeId("measuresRequester")
                .log("measuresRequester fired")
                .process(
                        (exchange) -> {
                            exchange.getMessage().setBody(prepareMeasuresResponse(
                                    exchange.getMessage().getHeader("measuresId", String.class), null));
                        }
                );

        from("direct:MeasuresRequest").routeId("MeasuresRequest")
                .log("MeasuresReqTopic fired")
                .marshal().json()
                .to("kafka:MeasuresReqTopic?brokers=localhost:9092");
    }

    private void temperatures() {
        final JaxbDataFormat jaxbTemperature = new
                JaxbDataFormat(TemperatureResponse.class.getPackage().getName());
        from("kafka:MeasuresReqTopic?brokers=localhost:9092").routeId("getTemperatures")
                .log("fired getTemperatures")
                .unmarshal().json(JsonLibrary.Jackson, MeasuresRequest.class)
                .process((exchange) ->
                {exchange.getMessage().setBody(
                        prepareTemperatureRequest(exchange.getMessage().getBody(MeasuresRequest.class)));
                } )
                .marshal(jaxbTemperature)
                .to("spring-ws:http://localhost:8080/soap-api/service/temperature")
                .to("stream:out")
                .unmarshal(jaxbTemperature)
                .marshal().json()
                .setHeader("serviceType", constant("temperatures"))
                .to("kafka:MeasuresTopic?brokers=localhost:9092");
        ;

    }

    private void powers() {
        from("kafka:MeasuresReqTopic?brokers=localhost:9092").routeId("getPowers")
                .log("fired getPowers")
                .unmarshal().json(JsonLibrary.Jackson, MeasuresRequest.class)
                .process(
                        (exchange) -> {
                            PowerResponse powerResponse = new PowerResponse();
                            powerResponse.setMeasuresId(measureIdentifierService.getMeasureIdentifier());
                            MeasuresRequest measuresRequest = exchange.getMessage().getBody(MeasuresRequest.class);
                            if (measuresRequest != null) {
                                powerResponse.setDates(null);
                                powerResponse.setPowers(null);
                            }
                            exchange.getMessage().setBody(measuresRequest);
                        }
                )
                .marshal().json()
                .to("stream:out")
                .setHeader("serviceType", constant("powers"))
                .to("kafka:MeasuresTopic?brokers=localhost:9092");

    }

    private DataRequest prepareTemperatureRequest(MeasuresRequest measuresRequest) {
        DataRequest temperatureRequest = new DataRequest();
        temperatureRequest.setFrom(measuresRequest.getFrom());
        temperatureRequest.setTo(measuresRequest.getTo());
        temperatureRequest.setRoomNr(measuresRequest.getRoomNr());
        return temperatureRequest;
    }

    private MeasuresResponse prepareMeasuresResponse(String measuresId, List<MeasuresResponseMeasures> measures) {
        MeasuresResponse measuresResponse = new MeasuresResponse();
        return measuresResponse.measuresId(measuresId).measures(measures);
    }
}
