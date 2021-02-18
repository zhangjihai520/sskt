package com.ry.sskt.core.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListStringUtil {
    /**@
     *  将相关字典表查询list信息合并到主业务查询数据List中(list数据强关联,mainList中存在匹配到dictList的数据才会返回)
     * @param mainList 主业务数据的list
     * @param dictList 相关字典表信息list
     * @param mainKey 主业务数据中和字典信息关联的字段,主业务表中不含有该键值的数值,则直接返回主业务数据
     * @param dictKey 字典信息中和主业务数据关联的字段,字典表中不含有该键值的数值,则直接返回主业务数据
     * @return List
     */
    public static List<Map<String, Object>> mergeList(List<Map<String, Object>> mainList,
                                                      List<Map<String, Object>> dictList, String mainKey, String dictKey) {
        // 结果返回的List
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        // 防空校验
        if (mainList == null || dictList == null) {
            return returnList;
        }

        // 循环主业务数据的List
        for (Map<String, Object> mainMap : mainList) {
            String mainValue = mainMap.get(mainKey) != null ? mainMap.get(mainKey).toString() : null;
            // 如果主业务表中不存在以mainKey为键值的字段则直接返回主业务数据
            if (null == mainValue) {
                return mainList;
            }
            String dictValue = "";
            // 每条主业务数据和字典表信息依次去匹配
            for (Map<String, Object> dictMap : dictList) {
                dictValue = dictMap.get(dictKey) != null ? dictMap.get(dictKey).toString() : null;
                // 如果字典表中不存在以dictKey为键值的字段则直接返回主业务数据
                if (null == dictValue) {
                    return mainList;
                }
                // 迭代字典表信息
                Iterator<Map.Entry<String, Object>> it = dictMap.entrySet().iterator();
                // 主业务信息中比较字段的值和字典信息中比较字段的值相等则将该条字典表数据合并到当前迭代的主业务信息数据中
                if (mainValue.equals(dictValue)) {
                    while (it.hasNext()) {
                        Map.Entry<String, Object> dictEntry = (Map.Entry<String, Object>) it.next();
                        // 将字典表信息中和主业务信息中重复的key的字段去掉,剩余的合并到主业务信息中
                        if (!mainMap.containsKey(dictEntry.getKey())) {
                            mainMap.put(dictEntry.getKey(), dictEntry.getValue());
                        }

                    }
                    returnList.add(mainMap);
                    break;
                }
            }
        }
        // 主业务数据和字典表数据无匹配的数据也直接返回主业务数据
        return returnList.isEmpty() ? mainList : returnList;
    }

    /**@
     *  将相关字典表查询list信息合并到主业务查询数据List中(list数据左关联,mainList中存在匹配到dictList的数据有值,其余的没值)
     * @param mainList 主业务数据的list
     * @param dictList 相关字典表信息list
     * @param mainKey 主业务数据中和字典信息关联的字段,主业务表中不含有该键值的数值,则直接返回主业务数据
     * @param dictKey 字典信息中和主业务数据关联的字段,字典表中不含有该键值的数值,则直接返回主业务数据
     * @return List
     */
    public static List<Map<String, Object>> mergeListLeft(List<Map<String, Object>> mainList,
                                                          List<Map<String, Object>> dictList, String mainKey, String dictKey) {
        // 结果返回的List
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        // 防空校验
        if (mainList == null || dictList == null) {
            return returnList;
        }

        // 循环主业务数据的List
        for (Map<String, Object> mainMap : mainList) {
            String mainValue = mainMap.get(mainKey) != null ? mainMap.get(mainKey).toString() : null;
            // 如果主业务表中不存在以mainKey为键值的字段则直接返回主业务数据
            if (null == mainValue) {
                return mainList;
            }
            String dictValue = "";
            // 每条主业务数据和字典表信息依次去匹配
            int dictListSize = dictList.size();
            int currentSize = 0;
            for (Map<String, Object> dictMap : dictList) {
                dictValue = dictMap.get(dictKey) != null ? dictMap.get(dictKey).toString() : null;
                // 如果字典表中不存在以dictKey为键值的字段则直接返回主业务数据
                if (null == dictValue) {
                    return mainList;
                }
                // 迭代字典表信息
                Iterator<Map.Entry<String, Object>> it = dictMap.entrySet().iterator();
                // 主业务信息中比较字段的值和字典信息中比较字段的值相等则将该条字典表数据合并到当前迭代的主业务信息数据中
                // 存在匹配值的时候将字典表信息加入业务表数据
                if (mainValue.equals(dictValue)) {
                    while (it.hasNext()) {
                        Map.Entry<String, Object> dictEntry = (Map.Entry<String, Object>) it.next();
                        // 将字典表信息中和主业务信息中重复的key的字段去掉,剩余的合并到主业务信息中
                        if (!mainMap.containsKey(dictEntry.getKey())) {
                            mainMap.put(dictEntry.getKey(), dictEntry.getValue());
                        }
                    }
                    returnList.add(mainMap);
                    break;
                    // 字典没有匹配的数据,将字典表的值清空放入业务表
                } else {
                    if (++currentSize == dictListSize) {
                        dealIterator(mainMap, it);
                        returnList.add(mainMap);
                    }
                }
            }

        }

        // 主业务数据和字典表数据无匹配的数据也直接返回主业务数据
        return returnList.isEmpty() ? mainList : returnList;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param mainMap
     * @param it
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private static void dealIterator(Map<String, Object> mainMap, Iterator<Map.Entry<String, Object>> it) {
        while (it.hasNext()) {
            Map.Entry<String, Object> dictEntry = (Map.Entry<String, Object>) it.next();
            // 将字典表信息中和主业务信息中重复的key的字段去掉,剩余的合并到主业务信息中
            if (!mainMap.containsKey(dictEntry.getKey())) {
                mainMap.put(dictEntry.getKey(), null);
            }
        }
    }
}
