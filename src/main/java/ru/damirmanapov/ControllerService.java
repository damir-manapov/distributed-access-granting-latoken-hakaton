package ru.damirmanapov;

import java.util.*;

public class ControllerService {

    private static final int REPLICATION_RATE = 2;

    private EthProxy ethProxy;
    private ControllerRepository repository;

    public void distributeKey(String key) {

        Map<String, List<String>> destination = new HashMap<>();
        Set<String> nodeIds = ethProxy.getPeerIds();

        Set<String> partialKeys = new HashSet<>();

        // TODO remove hardcoded values
        for (int i = 0; i < 8; i++) {
            partialKeys.add(key.substring(i, i + 8));
            System.out.println(key.substring(32 * i, 32 * i + 32));
        }

        for (int i = 0; i < 10; i++) {
            String objectId = String.valueOf(i);
            destination.put(objectId, RendezvousHashing.getDestinations(objectId, nodeIds, REPLICATION_RATE));
        }

    }

    public void setEthProxy(EthProxy ethProxy) {
        this.ethProxy = ethProxy;
    }

    public void setControllerRepository(ControllerRepository repository) {
        this.repository = repository;
    }
}
