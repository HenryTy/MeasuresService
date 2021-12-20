package com.service.measures;

import static org.apache.camel.model.rest.RestParamType.body;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.service.measures.model.MeasuresRequest;
import com.service.measures.model.MeasuresResponse;
import com.service.measures.model.MeasuresResponseMeasures;
import com.service.measures.power.PowerRequest;
import com.service.measures.power.PowerResponse;
import com.service.measures.temperature.DataRequest;
import com.service.measures.temperature.GetTemperatures;
import com.service.measures.temperature.GetTemperaturesResponse;
import com.service.measures.temperature.TemperatureResponse;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@Component
public class MeasuresService extends RouteBuilder {

    @Autowired
    MeasureIdentifierService measureIdentifierService;

    @Autowired
    JoinMeasuresService joinMeasuresService;

    @Override
    public void configure() throws Exception {
        gateway();
        temperatures();
        powers();
        join();
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
                .to("kafka:TemperaturesTopic?brokers=localhost:9092");

    }

    private void powers() {
        from("kafka:MeasuresReqTopic?brokers=localhost:9092").routeId("getPowers")
                .log("fired getPowers")
                .unmarshal().json(JsonLibrary.Jackson, MeasuresRequest.class)
                .process((exchange) ->
                {exchange.getMessage().setBody(
                        preparePowerRequest(exchange.getMessage().getBody(MeasuresRequest.class)));
                } )
                .marshal().json()
                .removeHeaders("Camel*")
                .setHeader("accept", constant("*/*"))
                .to("rest:post:/service/power?host=localhost:8081")
                .to("kafka:PowersTopic?brokers=localhost:9092");
    }

    private void join() {
        from("kafka:TemperaturesTopic?brokers=localhost:9092").routeId("joinTemperatures")
                .log("fired joinTemperatures")
                .unmarshal().json(JsonLibrary.Jackson, GetTemperaturesResponse.class)
                .process(
                        (exchange) -> {
                            String measuresId =
                                    exchange.getMessage().getHeader("measuresId", String.class);
                            boolean isReady= joinMeasuresService.addTemperatureResponse(
                                    measuresId,
                                    exchange.getMessage().getBody(GetTemperaturesResponse.class).getReturn());
                            exchange.getMessage().setHeader("isReady", isReady);
                        }
                )
                .choice()
                .when(header("isReady").isEqualTo(true)).to("direct:joinMeasures")
                .endChoice();

        from("kafka:PowersTopic?brokers=localhost:9092").routeId("joinPowers")
                .log("fired joinPowers")
                .unmarshal().json(JsonLibrary.Jackson, PowerResponse.class)
                .process(
                        (exchange) -> {
                            String measuresId =
                                    exchange.getMessage().getHeader("measuresId", String.class);
                            boolean isReady= joinMeasuresService.addPowerResponse(
                                    measuresId,
                                    exchange.getMessage().getBody(PowerResponse.class));
                            exchange.getMessage().setHeader("isReady", isReady);
                        }
                )
                .choice()
                .when(header("isReady").isEqualTo(true)).to("direct:joinMeasures")
                .endChoice();

        from("direct:joinMeasures").routeId("joinMeasures")
                .log("fired joinMeasures")
                .process(
                        (exchange) -> {
                            String measuresId =
                                    exchange.getMessage().getHeader("measuresId", String.class);
                            JoinMeasuresService.AllMeasures allMeasures =
                                    joinMeasuresService.getMeasures(measuresId);
                            List<MeasuresResponseMeasures> measures = new ArrayList<>();
                            List<OffsetDateTime> dates = allMeasures.powerResponse.getDates();
                            List<Double> temperatures = allMeasures.temperatureResponse.getTemperatures();
                            List<Double> powers = allMeasures.powerResponse.getPowers();
                            for(int i = 0; i < dates.size(); i++) {
                                MeasuresResponseMeasures mes = new MeasuresResponseMeasures();
                                mes.setDate(dates.get(i));
                                mes.setTemperature(BigDecimal.valueOf(temperatures.get(i)));
                                mes.setPower(BigDecimal.valueOf(powers.get(i)));
                                measures.add(mes);
                            }
                            MeasuresResponse measuresResponse = new MeasuresResponse();
                            measuresResponse.setMeasuresId(measuresId);
                            measuresResponse.setMeasures(measures);
                            exchange.getMessage().setBody(allMeasures);
                        }
                )
                .to("direct:notification");

        from("direct:notification").routeId("notification")
                .log("fired notification")
                .to("stream:out");
    }

    private GetTemperatures prepareTemperatureRequest(MeasuresRequest measuresRequest) {
        DataRequest temperatureRequest = new DataRequest();
        temperatureRequest.setFrom(offsetDateTimeToXML(measuresRequest.getFrom()));
        temperatureRequest.setTo(offsetDateTimeToXML(measuresRequest.getTo()));
        temperatureRequest.setRoomNr(measuresRequest.getRoomNr());
        GetTemperatures getTemperatures = new GetTemperatures();
        getTemperatures.setArg0(temperatureRequest);
        return getTemperatures;
    }

    private PowerRequest preparePowerRequest(MeasuresRequest measuresRequest) {
        PowerRequest powerRequest = new PowerRequest();
        powerRequest.setFrom(measuresRequest.getFrom());
        powerRequest.setTo(measuresRequest.getTo());
        powerRequest.setRoomNr(measuresRequest.getRoomNr());
        return powerRequest;
    }

    private MeasuresResponse prepareMeasuresResponse(String measuresId, List<MeasuresResponseMeasures> measures) {
        MeasuresResponse measuresResponse = new MeasuresResponse();
        return measuresResponse.measuresId(measuresId).measures(measures);
    }

    private XMLGregorianCalendar offsetDateTimeToXML(OffsetDateTime offsetDateTime) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    offsetDateTime.getYear(),
                    offsetDateTime.getMonthValue(),
                    offsetDateTime.getDayOfMonth(),
                    offsetDateTime.getHour(),
                    offsetDateTime.getMinute(),
                    offsetDateTime.getSecond(),
                    0,
                    0
            );
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
