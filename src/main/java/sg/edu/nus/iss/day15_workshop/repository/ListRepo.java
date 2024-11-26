package sg.edu.nus.iss.day15_workshop.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day15_workshop.model.Contacts;
import sg.edu.nus.iss.day15_workshop.util.Util;

@Repository
public class ListRepo {

    @Autowired
    @Qualifier(Util.template01)
    private RedisTemplate<String, Object> template;

    // add to the back
    public void rightPush(String redisKey, String value) {
        template.opsForList().rightPush(redisKey, value);
    }

    // remove from the back
    public void rightPop(String redisKey) {
        template.opsForList().rightPop(redisKey);
    }

    // delete entire list
    public void delete(String redisKey) {
        template.delete(redisKey);
    }

    public Boolean checkExists(String redisKey) {
        return template.hasKey(redisKey);
    }

    public String get(String redisKey, Integer index) {
        return template.opsForList().index(redisKey, index).toString();
    }


    public List<Object> getList(String redisKey) {
        return template.opsForList().range(redisKey, 0, -1);
    }

    public Long listSize(String redisKey) {
        return template.opsForList().size(redisKey);
    }

    public Boolean deleteContact(String redisKey, Contacts contact) {

        // make the return type a Boolean so we know if its successful or not
        Boolean isDeleted = false;
       
        String valueToDelete = contact.toString().toLowerCase();

        Long indexFound = template.opsForList().indexOf(redisKey, valueToDelete);

        if (indexFound >= 0) {
            template.opsForList().remove(redisKey, 1, valueToDelete);
            isDeleted = true;
        }

        return isDeleted;
    }


    public Long findIndex(String redisKey, Contacts contactBeforeUpdate) {

        Long indexFound = -1L;
        String targetValue = contactBeforeUpdate.toString().toLowerCase();

        indexFound = template.opsForList().indexOf(redisKey, targetValue);

        if (indexFound >= 0) {
            return indexFound;
        }

        return indexFound;
    }

    public void updateContact(String redisKey, Long indexToUpdateContact, Contacts updatedContact) {

        String contactWithUpdatedValues = updatedContact.toString().toLowerCase();
        template.opsForList().set(redisKey, indexToUpdateContact, contactWithUpdatedValues);
    }






    
}
