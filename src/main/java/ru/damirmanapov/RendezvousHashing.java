package ru.damirmanapov;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class RendezvousHashing {

    public static List<String> getDestinations(String key, Set<String> nodeIds) {

        Map<String, byte[]> nodeHashes = new HashMap<>();

        for (String nodeIs : nodeIds) {
            nodeHashes.put(nodeIs, SHA3_256.hash(nodeIs.getBytes(StandardCharsets.UTF_8)));
        }

        Map<String, String> destinationHashes = new HashMap<>();

        for (String nodeIs : nodeIds) {
            String concatenation = key + nodeIs;
            destinationHashes.put(nodeIs, bytesToHex(SHA3_256.hash(concatenation.getBytes(StandardCharsets.UTF_8))));
        }

//        for (int i = 0; i < 32; i++) {
//
//            byte maxByte = Byte.MIN_VALUE;
//
//            for (Map.Entry<String, byte[]> destinationHash : destinationHashes.entrySet()) {
//                maxByte = (byte) Math.max(maxByte, destinationHash.getValue()[i]);
//            }
//
//            Iterator<Map.Entry<String, byte[]>> iter = destinationHashes.entrySet().iterator();
//            while (iter.hasNext()) {
//                Map.Entry<String, byte[]> entry = iter.next();
//                if (entry.getValue()[i] < maxByte) {
//                    iter.remove();
//                }
//            }
//
//        }

        destinationHashes.entrySet().stream().sorted((x1, x2) -> destinationHashes.get(x1).compareToIgnoreCase(destinationHashes.get(x2)));

        if (destinationHashes.size() == 0) {
            throw new RuntimeException("Something went wrong");
        }

        return destinationHashes.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
    }

    public static String getBestDestination(String key, Set<String> nodeIds) {
        return getDestinations(key, nodeIds).get(0);
    }

    public static List<String> getDestinations(String key, Set<String> nodeIds, int count) {
        return getDestinations(key, nodeIds).subList(0, count);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
