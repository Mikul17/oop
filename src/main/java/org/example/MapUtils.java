package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapUtils {

    public static <T> List<T> collapseToList(Map<T, Integer> map){
        List<T> list = new ArrayList<T>();
        map.forEach((k,v)->{
            for(int i = 0; i<v ; i++){
                list.add(k);
            }
        });
        return list;
    }

    public static <T> Map<T, Integer> collapseToMap(List<T> list){
        return list.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        e -> 1,
                        Integer::sum
                ));
    }

    public static <INHERIT,BASE,V> Map<BASE,V> mapInheritedToBase(
            Map<INHERIT,V> map,
            Function<? super INHERIT,? extends BASE> mapper,
            BinaryOperator<V> mergeFunction
    ) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> mapper.apply(entry.getKey()),
                        Map.Entry::getValue,
                        mergeFunction,
                        HashMap::new
                ));
    }
}
