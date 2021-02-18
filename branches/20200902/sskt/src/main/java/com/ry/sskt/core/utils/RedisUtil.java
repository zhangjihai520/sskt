package com.ry.sskt.core.utils;

import com.ry.sskt.core.annotation.IRedisOperationObject;
import com.ry.sskt.core.annotation.IRedisStoredObject;
import com.ry.sskt.core.websocket.RedisSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis缓存操作类
 *
 * @author 幸仁强
 * @createtime 2018-2-22
 */
@Component
@Slf4j
public class RedisUtil {
    private static final String OK = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    /**
     * ms
     */
    private static final String PX_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private JedisPool jedisPool;

    // 维护一个本类的静态变量
    private static RedisUtil redisUtil;


    @PostConstruct
    public void init() {
        redisUtil = this;
        redisUtil.jedisPool = this.jedisPool;
    }

    public static Jedis getJedis() {
        return redisUtil.jedisPool.getResource();
    }

    /**
     * @throws
     * @Title: getListAll
     * @Description:获取list中的所有值
     * @param: @param key
     * @param: @return
     * @return: List<String>
     */
    public static List<String> getListAll(String key) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.lrange(key, 0, -1);
        } catch (Exception e) {
            log.error("获取Redis的List缓存对象异常，key为" + key);
        } finally {
            jedis.close();
        }
        return null;
    }

    public static boolean exists(String key) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            log.error("判断Redis对象是否存在异常，key为" + key);
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 获取redis键值-object
     *
     * @param key
     */
    public static Object getObject(String key) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if (null != bytes) {
                return SerializationUtils.deserialize(bytes);
            }
        } catch (Exception e) {
            log.error("获取Redis对象缓存异常，key为" + key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 设置redis键值-object
     *
     * @param key
     * @param value
     */
    public static String setObject(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.jedisPool.getResource();
            return jedis.set(key.getBytes(), SerializationUtils.serialize((Serializable) value));
        } catch (Exception e) {
            log.error("插入Redis缓存对象异常", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 设置redis键值-object-expiretime
     *
     * @param key
     * @param value
     * @param expiretime
     * @return java.lang.String
     * @author Wang926454
     * @date 2018/9/4 15:50
     */
    public static String setObject(String key, Object value, int expiretime) {
        String result = "";
        Jedis jedis = null;
        try {
            jedis = redisUtil.jedisPool.getResource();
            result = jedis.set(key.getBytes(), SerializationUtils.serialize((Serializable) value));
            if (OK.equals(result)) {
                jedis.expire(key.getBytes(), expiretime);
            }
            return result;
        } catch (Exception e) {
            log.error("插入Redis缓存对象异常", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static Set<String> keys(String pattern) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.keys(pattern);
        } catch (Exception e) {
            log.error("获取Redis缓存所有Key异常", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * @throws
     * @Title: rPush
     * @Description: list后面追加
     * @param: @param key
     * @param: @param value
     * @param: @return
     * @return: long
     */
    public static long rPush(String key, String value) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.rpush(key, value);
        } catch (Exception e) {
            log.error("Redis缓存对象list后面追加异常", e);
        } finally {
            jedis.close();
        }
        return 0;
    }


    /**
     * @throws
     * @Title: lrem
     * @Description: 移除
     * @param: @param key
     * @param: @param value
     * @param: @return
     * @return: long
     */
    public static long lrem(String key, String value) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.lrem(key, 0, value);
        } catch (Exception e) {
            log.error("Redis缓存对象lrem异常", e);
        } finally {
            jedis.close();
        }
        return 0;
    }

    public static boolean hmset(String key, Map<String, String> map) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            jedis.hmset(key, map);
            return true;
        } catch (Exception e) {
            log.error("Redis缓存对象hmset异常", e);
        } finally {
            jedis.close();
        }
        return false;
    }

    public static String hmget(String key, String field) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.hget(key, field);
        } catch (Exception e) {
            log.error("Redis缓存对象hmget异常", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    public static Map<String, String> hgetAll(String key) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            log.error("Redis缓存对象hgetAll异常", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    public static boolean setex(String key, int seconds, String val) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            jedis.setex(key, seconds, val);
            return true;
        } catch (Exception e) {
            log.error("Redis缓存对象setex异常", e);
        } finally {
            jedis.close();
        }
        return false;
    }

    public static boolean set(String key, String val) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            jedis.set(key, val);
            return true;
        } catch (Exception e) {
            log.error("Redis缓存对象set异常", e);
        } finally {
            jedis.close();
        }
        return false;
    }

    public static boolean diff(String key, String val) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            String cacheval = jedis.get(key);
            if (StringUtils.isNotBlank(cacheval) && val.equals(cacheval)) {
                jedis.del(key);
                return true;
            }
        } catch (Exception e) {
            log.error("Redis缓存对象diff异常", e);
        } finally {
            jedis.close();
        }
        return false;
    }

    public static String getVal(String key) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            log.error("Redis缓存对象getVal异常", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * @throws
     * @Title: getValReTime
     * @Description: 判断key是否存在，并重新刷新时间
     * @param: @param key
     * @param: @param time
     * @param: @return
     * @return: boolean
     */
    public static boolean getValReTime(String key, int time) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            String val = jedis.get(key);
            if (!StringUtils.isEmpty(val)) {
                jedis.setex(key, time, val);
                return true;
            }
        } catch (Exception e) {
            log.error("Redis缓存对象getValReTime异常", e);
        } finally {
            jedis.close();
        }
        return false;
    }

    public static void subscribe(RedisSubscriber redisSubscriber, String channel) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            jedis.subscribe(redisSubscriber, channel);
        } catch (Exception e) {
            log.error("消息订阅失败", e);
        } finally {
            jedis.close();
        }
    }

    public static <T extends IRedisStoredObject> void publish(String channel, String message) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            jedis.publish(channel, message);
        } catch (Exception e) {
            log.error("消息发布失败", e);
        } finally {
            jedis.close();
        }
    }

    public static Long incr(String key) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            return jedis.incr(key);
        } catch (Exception e) {
            log.error("Redis缓存对象incr异常", e);
        } finally {
            jedis.close();
        }
        return 0L;

    }

    /**
     * 尝试获取分布式锁  利用redis set值的 NX参数
     *
     * @param lockKey    锁
     * @param requestId  锁拥有者标识
     * @param expireTime 超期时间 ms为单位
     * @return
     */
    public static boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, PX_EXPIRE_TIME, expireTime);
            if (OK.equals(result)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Redis缓存对象tryGetDistributedLock异常", e);
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 释放分布式锁  利用 LUA脚本保证操作的原子性（Redis单进程单线程并保证执行LUA脚本时不执行其它命令）
     *
     * @param lockKey   锁
     * @param requestId 锁拥有者标识
     * @return
     */
    public static boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object res = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            return RELEASE_SUCCESS.equals(res);
        } catch (Exception e) {
            log.error("Redis缓存对象releaseDistributedLock异常", e);
        } finally {
            jedis.close();
        }
        return false;
    }


    /**
     * 获取缓存对象的byte数组
     *
     * @param <T>
     * @param key
     * @return
     * @throws IOException
     */
    public static <T extends IRedisStoredObject> T getObj(String key, Class<T> clazz) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            byte[] objByte = jedis.get(key.getBytes());
            if (ArrayUtils.isNotEmpty(objByte)) {
                return ProtoBufUtil.deserializer(objByte, clazz);
            }
            return null;
        } catch (Exception e) {
            log.error("获取Redis缓存失败！", e);
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 添加缓存
     *
     * @param obj 需要缓存的对象
     * @return
     * @throws Exception
     */
    public static <T extends IRedisStoredObject> boolean setObj(T obj) {
        return upset(obj, 0);
    }

    /**
     * 添加缓存
     *
     * @param obj             需要缓存的对象
     * @param expireOfSeconds 过期时间（秒）
     * @return
     * @throws Exception
     */
    public static <T extends IRedisStoredObject> boolean setObj(T obj, int expireOfSeconds) {
        return upset(obj, expireOfSeconds);
    }

    /**
     * CAS更新指定缓存
     *
     * @param obj 需要缓存的对象
     * @return
     * @throws Exception
     */
    public static <T extends IRedisOperationObject & IRedisStoredObject> boolean updateCas(T obj) {
        return upsetCas(obj, 0);
    }

    /**
     * CAS更新指定缓存
     *
     * @param obj             需要缓存的对象
     * @param expireOfSeconds 缓存过期时间（分钟）
     * @return
     * @throws Exception
     */
    public static <T extends IRedisOperationObject & IRedisStoredObject> boolean updateCas(T obj,
                                                                                           int expireOfSeconds) {
        return upsetCas(obj, expireOfSeconds);
    }

    /**
     * 添加缓存
     *
     * @param obj             需要缓存的对象
     * @param expireOfSeconds 过期时间（秒）
     * @return
     * @throws Exception
     */
    private static <T extends IRedisStoredObject> boolean upset(T obj, int expireOfSeconds) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        String result = null;
        try {
            byte[] bytes = ProtoBufUtil.serializer(obj);
            if (expireOfSeconds > 0) {
                result = jedis.setex(obj.getKey().getBytes(), expireOfSeconds, bytes);
            } else {
                result = jedis.set(obj.getKey().getBytes(), bytes);
            }
            return StringUtils.isNotBlank(result);
        } catch (Exception e) {
            log.error("存入Redis缓存失败！", e);
        } finally {
            jedis.close();
        }
        return false;
    }

    public static <T extends IRedisOperationObject & IRedisStoredObject> boolean upsetCas(T obj,
                                                                                          int expireOfSeconds) {//lockName可以为共享变量名，也可以为方法名，主要是用于模拟锁信息
        String lockKey = obj.getKey() + "lock";
        int i = 1;
        int tryNum = 3;
        while (i <= tryNum) {
            Jedis jedis = redisUtil.jedisPool.getResource();
            try {

                Long setnxResult = jedis.setnx(lockKey, String.valueOf(System.currentTimeMillis() + 5000));
                if (setnxResult != null && setnxResult.intValue() == 1) {
                    jedis.expire(lockKey, 5);
                    byte[] objByte = jedis.get(obj.getKey().getBytes());
                    boolean operation = obj.operation(objByte);
                    if (operation) {
                        byte[] bytes = ProtoBufUtil.serializer(obj);
                        if (expireOfSeconds > 0) {
                            jedis.setex(obj.getKey().getBytes(), expireOfSeconds, bytes);
                        } else {
                            jedis.set(obj.getKey().getBytes(), bytes);
                        }
                    }
                    jedis.del(lockKey);
                    return true;
                } else {
                    String lockValueA = jedis.get(lockKey);
                    if (lockValueA != null && Long.parseLong(lockValueA) >= System.currentTimeMillis()) {
                        String lockValueB = jedis.getSet(lockKey, String.valueOf(System.currentTimeMillis() + 5000));
                        if (lockValueB == null || lockValueB.equals(lockValueA)) {
                            jedis.expire(lockKey, 5);
                            byte[] objByte = jedis.get(obj.getKey().getBytes());
                            boolean operation = obj.operation(objByte);
                            if (operation) {
                                byte[] bytes = ProtoBufUtil.serializer(obj);
                                if (expireOfSeconds > 0) {
                                    jedis.setex(obj.getKey().getBytes(), expireOfSeconds, bytes);
                                } else {
                                    jedis.set(obj.getKey().getBytes(), bytes);
                                }
                            }
                            jedis.del(lockKey);
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                if (i == tryNum) {
                    log.error("存入Redis缓存失败！", e);
                    throw new RedisSystemException("存入Redis缓存失败", e);
                }
            } finally {
                i++;
                jedis.close();
            }
        }
        return false;
    }


    /**
     * 根据Key删除Redis缓存
     *
     * @param key
     * @return
     */
    public static boolean del(String key) {
        Jedis jedis = redisUtil.jedisPool.getResource();
        try {
            if (StringUtils.isNotBlank(jedis.get(key))) {
                jedis.del(key);
            }
            return true;
        } catch (Exception e) {
            log.error("删除Redis缓存失败", e);
            throw new RedisSystemException("删除Redis缓存失败", e);
        } finally {
            jedis.close();
        }
    }


}
