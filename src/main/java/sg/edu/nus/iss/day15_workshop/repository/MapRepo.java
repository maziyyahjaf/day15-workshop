package sg.edu.nus.iss.day15_workshop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day15_workshop.util.Util;

@Repository
public class MapRepo {

    @Autowired
    @Qualifier(Util.template01)
    private RedisTemplate<String, Object> template;

    
    
}
