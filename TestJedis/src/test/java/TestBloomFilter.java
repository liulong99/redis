import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;

public class TestBloomFilter {
    private static int size = 1000000;
    //size   预计要插入多少数据
    //fpp    容错率
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, 0.001);
    public static void main(String[] args) {
        for (int i = 0; i <=size; i++) {
            bloomFilter.put(i);
        }
        List<Integer> list = new ArrayList<>(10000);
        // 故意取10000个不在过滤器里的值，看看有多少个会被认为在过滤器里
        for (int i = size + 10000; i < size + 20000; i++) {
            if (bloomFilter.mightContain(i)) {  //误判
                list.add(i);
            }
        }
        System.out.println("误判的数量：" + list.size());
    }
}
