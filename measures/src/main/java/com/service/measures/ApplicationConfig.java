package com.service.measures;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.service.measures.state.ProcessingEvent;
import com.service.measures.state.ProcessingState;
import com.service.measures.state.StateMachineBuilder;
import com.service.measures.state.StateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class ApplicationConfig {

    @org.springframework.beans.factory.annotation.Value("${measures.mongo.server}")
    private String measuresMongoServer;

    @Bean(name="basicStateMachineBuilder")
    public StateMachineBuilder basicStateMachineBuilder() {
        StateMachineBuilder smb = new StateMachineBuilder();
        smb.initialState(ProcessingState.NONE)
                .add(ProcessingState.NONE, ProcessingEvent.START,ProcessingState.STARTED)
                .add(ProcessingState.STARTED,ProcessingEvent.FINISH,ProcessingState.FINISHED)
                .add(ProcessingState.NONE,ProcessingEvent.CANCEL,ProcessingState.CANCELLED)
                .add(ProcessingState.STARTED,ProcessingEvent.CANCEL,ProcessingState.CANCELLED)
                .add(ProcessingState.FINISHED,ProcessingEvent.CANCEL,ProcessingState.CANCELLED)
                .add(ProcessingState.CANCELLED,ProcessingEvent.START,ProcessingState.CANCELLED)
                .add(ProcessingState.CANCELLED,ProcessingEvent.FINISH,ProcessingState.CANCELLED)
                .add(ProcessingState.CANCELLED,ProcessingEvent.CANCEL,ProcessingState.CANCELLED)
                .add(ProcessingState.FINISHED,ProcessingEvent.COMPLETE,ProcessingState.COMPLETED)
                .add(ProcessingState.CANCELLED,ProcessingEvent.COMPLETE,ProcessingState.COMPLETED)
        ;
        return smb;
    }

    @Bean
    @Scope("prototype")
    public StateService stateService(@Qualifier("basicStateMachineBuilder") StateMachineBuilder stateMachineBuilder) {
        return new StateService (stateMachineBuilder);
    }

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://" + measuresMongoServer + "/measures_service");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }
}
