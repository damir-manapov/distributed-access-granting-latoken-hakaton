package ru.damirmanapov;

import ru.damirmanapov.exception.AppException;
import ru.damirmanapov.messages.GetPartialKeyForRedistributionMessage;
import ru.damirmanapov.messages.GetPartialKeyMessage;
import ru.damirmanapov.messages.PutPartialKeyMessage;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ControllerRepository {

    private static final int REPLICATION_RATE = 2;

    private EthProxy ethProxy;

    public void putPartialKey(String partialKey, Peer peer) {

        PutPartialKeyMessage message = new PutPartialKeyMessage();
        message.setPartialKey(partialKey);

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        ClientServer.sendMessage(peer, message, response -> {
            String answer = (String) response;
            System.out.println("answer: " + answer);
        });

    }

    public String getRemotePartialKey(int partialIndex, Peer peer) {

        GetPartialKeyMessage message = new GetPartialKeyMessage();
        message.setPartialIndex(partialIndex);

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        ClientServer.sendMessage(peer, message, response -> {
            String partialKey = (String) response;
            System.out.println("partialKey: " + partialKey);
            completableFuture.complete(partialKey);
        });

        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AppException(e);
        }

    }

    public void getPartialKeyForRedistribution(int partialIndex, Peer peer) {

        GetPartialKeyForRedistributionMessage message = new GetPartialKeyForRedistributionMessage();
        message.setPartialIndex(partialIndex);

        ClientServer.sendMessage(peer, message, response -> {
            String partialKey = (String) response;
            System.out.println("partialKey: " + partialKey);
            if (partialKey != null && !partialKey.isEmpty()) {
                Sidechain.parts.put(partialIndex, partialKey);
            }
//            client.close();
//            System.exit(0);
        });

    }

    public void setEthProxy(EthProxy ethProxy) {
        this.ethProxy = ethProxy;
    }
}
