//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.17 at 02:49:01 PM CET 
//


package com.service.measures.temperature;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.service.measures.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetTemperatures_QNAME = new QName("http://temperature.service.com/", "getTemperatures");
    private final static QName _GetTemperaturesResponse_QNAME = new QName("http://temperature.service.com/", "getTemperaturesResponse");
    private final static QName _Fault_QNAME = new QName("http://temperature.service.com/", "Fault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.service.measures.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTemperatures }
     * 
     */
    public GetTemperatures createGetTemperatures() {
        return new GetTemperatures();
    }

    /**
     * Create an instance of {@link GetTemperaturesResponse }
     * 
     */
    public GetTemperaturesResponse createGetTemperaturesResponse() {
        return new GetTemperaturesResponse();
    }

    /**
     * Create an instance of {@link Fault }
     * 
     */
    public Fault createFault() {
        return new Fault();
    }

    /**
     * Create an instance of {@link DataRequest }
     * 
     */
    public DataRequest createDataRequest() {
        return new DataRequest();
    }

    /**
     * Create an instance of {@link TemperatureResponse }
     * 
     */
    public TemperatureResponse createTemperatureResponse() {
        return new TemperatureResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTemperatures }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetTemperatures }{@code >}
     */
    @XmlElementDecl(namespace = "http://temperature.service.com/", name = "getTemperatures")
    public JAXBElement<GetTemperatures> createGetTemperatures(GetTemperatures value) {
        return new JAXBElement<GetTemperatures>(_GetTemperatures_QNAME, GetTemperatures.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTemperaturesResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetTemperaturesResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://temperature.service.com/", name = "getTemperaturesResponse")
    public JAXBElement<GetTemperaturesResponse> createGetTemperaturesResponse(GetTemperaturesResponse value) {
        return new JAXBElement<GetTemperaturesResponse>(_GetTemperaturesResponse_QNAME, GetTemperaturesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     */
    @XmlElementDecl(namespace = "http://temperature.service.com/", name = "Fault")
    public JAXBElement<Fault> createFault(Fault value) {
        return new JAXBElement<Fault>(_Fault_QNAME, Fault.class, null, value);
    }

}