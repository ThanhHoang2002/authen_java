package PizzaHut_be.dao;

import jakarta.transaction.Transactional;
import lombok.CustomLog;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Setter
@CustomLog
public abstract class BaseDao<T, ID> {
    private static final int DEFAULT_DB = 0;
    @Autowired
    protected JedisPool jedisPool;

    private int dbIndex = 0;

    public abstract T findOneById(ID id);

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class})
    public abstract T save(T entity);

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class})
    public abstract void deleteById(ID id);

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class})
    public abstract void delete(T entity);

    protected Jedis createConnection() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            if (dbIndex != DEFAULT_DB) {
                jedis.select(dbIndex);
            }

            return jedis;
        } catch (Exception e) {
            log.error("Error creating connection to Redis: ", e);
            return new Jedis();
        }
    }
}
