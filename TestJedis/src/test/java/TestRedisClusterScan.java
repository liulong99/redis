import com.xufree.learning.redis.config.ConfigBean;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import redis.clients.jedis.*;
import redis.clients.jedis.util.JedisClusterCRC16;
import redis.clients.jedis.util.SafeEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.util.*;

public class TestRedisClusterScan {
    Map<Integer, JedisPool> slotsMap=new HashMap<>();
    Map<String, JedisPool> nodesMap=new HashMap<>();

    public void getSlotsMap() {
        Set<Map.Entry<Integer, JedisPool>> entries = slotsMap.entrySet();
        Iterator<Map.Entry<Integer, JedisPool>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, JedisPool> next = iterator.next();
            Integer key = next.getKey();
            JedisPool jedisPool = slotsMap.get(key);
            System.out.println(key+"--"+jedisPool);
        }
    }

    public void getNodesMap() {
        Set<Map.Entry<String, JedisPool>> entries = nodesMap.entrySet();
        Iterator<Map.Entry<String, JedisPool>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, JedisPool> next = iterator.next();
            String key = next.getKey();
            JedisPool jedisPool = nodesMap.get(key);
            System.out.println(key+"--"+jedisPool);
        }
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(ConfigBean.class);
        JedisClusterInfoCache bean = annotationConfigApplicationContext.getBean(JedisClusterInfoCache.class);
        System.out.println(bean.getNodes());
        TestRedisClusterScan testRedisClusterScan=new TestRedisClusterScan();

        Logger logger= LoggerFactory.getLogger(TestRedisCluster.class);
        Set<HostAndPort> nodesList=new HashSet<>();
        nodesList.add(new HostAndPort("192.168.204.188",7000));
        nodesList.add(new HostAndPort("192.168.204.188",7001));
        nodesList.add(new HostAndPort("192.168.204.188",7002));
        nodesList.add(new HostAndPort("192.168.204.188",7003));
        nodesList.add(new HostAndPort("192.168.204.188",7004));
        nodesList.add(new HostAndPort("192.168.204.188",7005));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(200);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(1000);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(100);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(3000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(false);
        JedisCluster jedisCluster=new JedisCluster(nodesList,2000,2000,6,"123456",jedisPoolConfig);
        int hello = JedisClusterCRC16.getCRC16("taibai0");
        System.out.println(hello);
        System.out.println(hello%16384);
//        Jedis connectionFromSlot = jedisCluster.getConnectionFromSlot(hello);
//        List<Object> slots = connectionFromSlot.clusterSlots();
//        String s = connectionFromSlot.clusterInfo();
//        System.out.println(s);
//        String localNodes = connectionFromSlot.clusterNodes();
//        System.out.println(localNodes);
//        Iterator var3 = slots.iterator();
//        while(true) {
//            List slotInfo;
//            do {
//                if (!var3.hasNext()) {
//                    return;
//                }
//                Object slotInfoObj = var3.next();
//                slotInfo = (List)slotInfoObj;
//            } while(slotInfo.size() <= 2);
//
//            List<Integer> slotNums = testRedisClusterScan.getAssignedSlotArray(slotInfo);
//            int size = slotInfo.size();
//            for(int i = 2; i < size; ++i) {
//                List<Object> hostInfos = (List)slotInfo.get(i);
//                if (hostInfos.size() > 0) {
//                    HostAndPort targetNode = testRedisClusterScan.generateHostAndPort(hostInfos);
//                    testRedisClusterScan.setupNodeIfNotExist(targetNode);
//                    if (i == 2) {
//                        testRedisClusterScan.assignSlotsToNode(slotNums, targetNode);
////                        testRedisClusterScan.getSlotsMap();
//                        System.out.println("====================");
//                        testRedisClusterScan.getNodesMap();
//                    }
//                }
//            }
//        }
    }




    public  List<Integer> getAssignedSlotArray(List<Object> slotInfo) {
        List<Integer> slotNums = new ArrayList();

        for(int slot = ((Long)slotInfo.get(0)).intValue(); slot <= ((Long)slotInfo.get(1)).intValue(); ++slot) {
            slotNums.add(slot);
        }

        return slotNums;
    }


    public  HostAndPort generateHostAndPort(List<Object> hostInfos) {
        return new HostAndPort(SafeEncoder.encode((byte[])((byte[])hostInfos.get(0))), ((Long)hostInfos.get(1)).intValue());
    }


    public  JedisPool setupNodeIfNotExist(HostAndPort node) {

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(200);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(1000);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(100);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(3000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(false);
        JedisPool nodePool;
        try {
            String nodeKey = getNodeKey(node);
            JedisPool existingPool = (JedisPool)nodesMap.get(nodeKey);
            if (existingPool == null) {
                nodePool = new JedisPool(jedisPoolConfig, node.getHost(), node.getPort(), 2000, 2000, null, 0, null, false, (SSLSocketFactory)null, (SSLParameters)null, (HostnameVerifier)null);
                nodesMap.put(nodeKey, nodePool);
                JedisPool var5 = nodePool;
                return var5;
            }
            nodePool = existingPool;
        } finally {
        }

        return nodePool;
    }


    public void assignSlotsToNode(List<Integer> targetSlots, HostAndPort targetNode) {

        try {
            JedisPool targetPool = this.setupNodeIfNotExist(targetNode);
            Iterator var4 = targetSlots.iterator();

            while(var4.hasNext()) {
                Integer slot = (Integer)var4.next();
                slotsMap.put(slot, targetPool);
            }
        } finally {
        }

    }


    public static String getNodeKey(HostAndPort hnp) {
        return hnp.getHost() + ":" + hnp.getPort();
    }
}
