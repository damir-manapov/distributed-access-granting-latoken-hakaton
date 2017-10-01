package ru.damirmanapov;

import net.jodah.concurrentunit.ConcurrentTestCase;
import org.testng.annotations.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class LogicTest extends ConcurrentTestCase {

    private static final int REPLICATION_RATE = 2;

//    Получаем список адресов майнеров, разбиваем дешифрующий ключ между ними
//
//    Проверяем, не нужно ли перераспределить держателей, убеждаемся что часть пиров вылетела, мы становимся держателем части ключа, запрашиваем его у живого пира, если он возвращает его сохраняем
//
//    Получаем запрос о ключе от ноды, проверяем не является ли она в данных обстоятельствах держателем этой части, если да передаем ей ей часть ключа
//
//    Получаем запрос на часть ключа от пользователя, проверяем что он действительно представляет указанный им адрес, проверяем, одобряет ли контракт передачу ему части ключа, если да пеердаем ему часть ключа
//
//    Получаем запрос на добавление ключа, добавляем его

    @Test
    public void testDistributingKey() {

//      Распределяем 10 объектов по 3 нодам,  добавляем 4. Проверяем что если объект поменял расположение
//      то новая нода 4. Убираем 2 ноду, если нода изменила расположение, то старая была второй.

//        String nodeId1 = "1";
//        String nodeId2 = "2";
//        String nodeId3 = "3";

        String decriptionKey = "CAmapq2rw8kdkKrEeEcjPXcLnpuvGdyAWzPyRJU8VmguxwNvqGqgxAfFmZ4qyE8XTtUqddJcMgPRZ9GCe5aNpVc7mv8gct5wrFJaBbVF2G2bqqhJTTFfQPYXMR8aPGRgUdRVJJh5m2Wb7WdhSRLNgL5naU4BcPXkVfaxGHTMAekPMC9z7wZnaSBCMcPAvB99sHkBFPwQ6SHKqv7BNQQ8EAD2yuRJsBRzTWmDgPBr5wFVFnYK3vsSuXWFaRSWZQBW";

        Map<String, List<String>> destination = new HashMap<>();
        Set<String> nodeIds = new HashSet<>();

        nodeIds.add("d24400ae8BfEBb18cA49Be86258a3C749cf46853");
        nodeIds.add("c12B92934aF5618a3395E286C5a340cC7E009AC7");
        nodeIds.add("25bA0C888dAB435A6e155f9CfA9bFCEc704AD3F1");
        nodeIds.add("2Cae18DD1223Aea3bFDFDdFEE4cfBbCB4b80Cc22");
        nodeIds.add("B26d92a32795D24DB363B31B36B04f5f33045F1D");
        nodeIds.add("762C0f710cddBAE48d121f87AF45B392cEc2C815");
        nodeIds.add("FBb1b73C4f0BDa4f67dcA266ce6Ef42f520fBB98");
        nodeIds.add("d26114cd6EE289AccF82350c8d8487fedB8A0C07");
        nodeIds.add("4470BB87d77b963A013DB939BE332f927f2b992e");
        nodeIds.add("dEaaBf1215245619748cc790A23fF51123bD45E0");
        nodeIds.add("4A2Ca1Ac4450fEB122da167892eA67bc8334D7ee");
        nodeIds.add("bCCBD8FE5DF31bf58C7cD86d0de8f132D72a1631");
        nodeIds.add("425520865169c7a387e9bd3Fd84Ea7a3ac923014");
        nodeIds.add("8d69B3E9C07280A48FE4f2B1b91F7BADf6bCfb3D");
        nodeIds.add("Ce51e961df3d2Ff64660D502f4d8890330652D77");
        nodeIds.add("E6f977fEb99F109389dccA0DbBd09d420F0C6eC8");
        nodeIds.add("B809433862262aA74be3EE5b1C4BF1F9520b31e0");
        nodeIds.add("f656065982c180C75F05B355a08290F7f33c8272");
        nodeIds.add("F94554b11720D6a6Bdf25a4cA14a7d97AaddcbEa");
        nodeIds.add("A95C2Bc25cb484C84ED4F7f3EFC5235cC0Ad74A7");
        nodeIds.add("b356B9bce35A6cFC1031a7826AbfaEBaE302B903");
        nodeIds.add("25C14CE079Ee8C638f35fD2ECfA76a2EFC63aB39");
        nodeIds.add("D3C445e2E72cC0c328646F3d2fB280390a9480c7");
        nodeIds.add("BD9bde31F9Ebb40Fb04dF093B5c2aae0E1739BfA");
        nodeIds.add("18A54a69986f8A6e408243D1ea0CF5c3c596C327");

        Set<String> partialKeys = new HashSet<>();

        // TODO remove hardcoded values
        for (int i = 0; i < 8; i++) {
            partialKeys.add(decriptionKey.substring(i, i + 8));
            System.out.println(decriptionKey.substring(32 * i, 32 * i + 32));
        }

        for (int i = 0; i < 10; i++) {
            String objectId = String.valueOf(i);
            destination.put(objectId, RendezvousHashing.getDestinations(objectId, nodeIds, REPLICATION_RATE));
        }


//
//        String nodeId4 = "4";
//
//        nodeIds.add(nodeId4);
//
//        Map<String, String> newDestination = new HashMap<>();
//
//        // Dispersing between 4 nodes
//        for (int i = 0; i < 10; i++) {
//            String objectId = String.valueOf(i);
//            newDestination.put(objectId, RendezvousHashing.getDestinations(objectId, nodeIds));
//        }
//
//        // Check object moved only to new node
//        for (int i = 0; i < 10; i++) {
//            String objectId = String.valueOf(i);
//            if (!destination.get(objectId).equals(newDestination.get(objectId))) {
//                assertThat(newDestination.get(objectId), is(nodeId4));
//            }
//        }
//
//        nodeIds.remove(nodeId2);
//
//        Map<String, String> destinationRemovedSecond = new HashMap<>();
//
//        // Dispersing between nodes after removing second node
//        for (int i = 0; i < 10; i++) {
//            String objectId = String.valueOf(i);
//            destinationRemovedSecond.put(objectId, RendezvousHashing.getDestinations(objectId, nodeIds));
//        }
//
//        // Check after removing second node only its objects moved
//        for (int i = 0; i < 10; i++) {
//            String objectId = String.valueOf(i);
//            if (!newDestination.get(objectId).equals(destinationRemovedSecond.get(objectId))) {
//                assertThat(newDestination.get(objectId), is(nodeId2));
//            }
//        }
//
//        assertThat(true, samePropertyValuesAs(true));
    }

}