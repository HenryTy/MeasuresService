package com.service.measures.state;

import java.util.HashMap;

public class StateService {
    private HashMap<String, StateMachine> processingStates = new HashMap<>();

    public StateService(StateMachineBuilder stateMachineBuilder) {
        this.stateMachineBuilder = stateMachineBuilder;
    }

    private StateMachineBuilder stateMachineBuilder = null;

    public ProcessingState sendEvent(String measuresId, ProcessingEvent event) {
        StateMachine stateMachine;
        synchronized(this){
            stateMachine = processingStates.get(measuresId);
            if (stateMachine==null) {
                stateMachine=stateMachineBuilder.build();
                processingStates.put(measuresId, stateMachine);
            }
        }
        return stateMachine.sendEvent(event);

    }

    public void removeState(String measuresId) {
        processingStates.remove(measuresId);
    }

}