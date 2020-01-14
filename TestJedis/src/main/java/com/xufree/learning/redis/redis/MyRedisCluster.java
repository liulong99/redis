package com.xufree.learning.redis.redis;

import com.google.common.collect.Lists;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.util.JedisClusterCRC16;

import java.util.*;

public class MyRedisCluster extends JedisCluster {


    public MyRedisCluster(Set node, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        super(node, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
    }

    @Override
    public String mset(String... keysvalues) {
        for (int i=0;i<keysvalues.length;i++){
            if(i%2==0){
                int slot=JedisClusterCRC16.getCRC16(keysvalues[i])%16384;
                Jedis connectionFromSlot = this.getConnectionFromSlot(slot);
                System.out.println(keysvalues[i]+"---"+keysvalues[i + 1]);
                System.out.println(connectionFromSlot.set(keysvalues[i], keysvalues[i + 1]));
                connectionFromSlot.close();
            }
        }
        return "OK";
    }



















    @Override
    public ScanResult<String> scan(String cursor, ScanParams params) {
        ScanParams scanParams=new ScanParams();
        scanParams.match("*");
        scanParams.count(5);
        return super.scan(cursor, params);
    }

    public static List<String> methodScan(Jedis jedis,String cursor, ScanParams params){
        List<String> list = new ArrayList<>();
        while (true) {
            ScanResult scanResult = jedis.scan(cursor,params);
            List<String> elements = scanResult.getResult();
            if (elements != null && elements.size() > 0) {
                list.addAll(elements);
            }
            cursor = scanResult.getCursor();
            if ("0".equals(cursor)) {
                break;
            }
        }
        return list;
    }

    public Map<Integer,ScanResult> myScan(String cursor, ScanParams params) {
        Map<Integer,ScanResult> map = new HashMap<>();
        Map<String, JedisPool> clusterNodes = this.getClusterNodes();
        for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
            Jedis jedis = entry.getValue().getResource();
            // 判断非从节点(因为若主从复制，从节点会跟随主节点的变化而变化)
            if (!jedis.info("replication").contains("role:slave")) {
//                ScanResult scanResult = jedis.scan(cursor,params);
//                int code = entry.getValue().hashCode();
//                map.put(code,scanResult);
                jedis.set("{taibai}:taibai","awfafa");
            }
        }
        return map;
    }

    public Map<Integer,ScanResult> myScan(Map<Integer,Map<String,ScanParams>>  map) {
        Map<Integer,ScanResult> newMap = new HashMap<>();
        Map<String, JedisPool> clusterNodes = this.getClusterNodes();
        for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
            Jedis jedis = entry.getValue().getResource();
            // 判断非从节点(因为若主从复制，从节点会跟随主节点的变化而变化)
            if (!jedis.info("replication").contains("role:slave")) {
                Set<Integer> integers = map.keySet();
                for (Integer code : integers) {
                    if(code==entry.getValue().hashCode()){
                        Map<String, ScanParams> stringScanParamsMap = map.get(code);
                        Set<String> strings = stringScanParamsMap.keySet();
                        for (String string : strings) {
                            ScanResult scanResult = jedis.scan(string,stringScanParamsMap.get(string));
                            newMap.put(code,scanResult);
                        }
                    }
                }
            }
        }
        return newMap;
    }

    public List<String> getRedisKeys(String cursor, ScanParams params) {
        List<String> list = new ArrayList<>();
        try {
            Map<String, JedisPool> clusterNodes = this.getClusterNodes();
            for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
                Jedis jedis = entry.getValue().getResource();
                // 判断非从节点(因为若主从复制，从节点会跟随主节点的变化而变化)
                if (!jedis.info("replication").contains("role:slave")) {
                    List<String> keys = methodScan(jedis,cursor,params);
                    if (keys.size() > 0) {
                        Map<Integer, List<String>> map = new HashMap<>();
                        for (String key : keys) {
                            // cluster模式执行多key操作的时候，这些key必须在同一个slot上，不然会报:JedisDataException:
                            // CROSSSLOT Keys in request don't hash to the same slot
                            int slot = JedisClusterCRC16.getSlot(key);
                            // 按slot将key分组，相同slot的key一起提交
                            if (map.containsKey(slot)) {
                                map.get(slot).add(key);
                            } else {
                                map.put(slot, Lists.newArrayList(key));
                            }
                        }
                        for (Map.Entry<Integer, List<String>> integerListEntry : map.entrySet()) {
                            // System.out.println("integerListEntry="+integerListEntry);
                            list.addAll(integerListEntry.getValue());
                        }
                    }
                }
            }
        } finally {
            return list;
        }
    }



}
