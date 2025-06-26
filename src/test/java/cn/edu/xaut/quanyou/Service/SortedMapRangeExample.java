package cn.edu.xaut.quanyou.Service;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SortedMapRangeExample {
    @Test
    public  void tests() {
        SortedMap<String, Integer> map = new TreeMap<>();
        map.put("apple", 10);
        map.put("banana", 5);
        map.put("cherry", 15);
        map.put("date", 7);
        map.put("fig", 9);

        System.out.println("原始 TreeMap: " + map);
List<Map.Entry<String ,Integer>> list = new ArrayList<>(map.entrySet());
list.sort(Map.Entry.comparingByValue());
System.out.println("排序后的 TreeMap: "+ list);
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
}

}
