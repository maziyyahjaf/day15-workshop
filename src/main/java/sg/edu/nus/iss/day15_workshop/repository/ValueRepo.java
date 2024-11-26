package sg.edu.nus.iss.day15_workshop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day15_workshop.util.Util;

@Repository
public class ValueRepo {

    @Autowired
    @Qualifier(Util.template02)
    private RedisTemplate<String,String> template;

    // list all the opsFor for value methods

    public void createValue(String redisKey, String value) {
        template.opsForValue().set(redisKey, value);
    }

    public void setIfAbsent(String redisKey, String value) {
        template.opsForValue().set(redisKey, value);
    }

    public String getValue(String redisKey) {
        return template.opsForValue().get(redisKey);
    }

    public Boolean checkExists(String redisKey) {
        return template.hasKey(redisKey);
    }

    public Boolean deleteKey(String redisKey) {
        return template.delete(redisKey);
    }

    
    
}
