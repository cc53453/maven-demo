package io.github.cc53453.datatype.util;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ListSearchHelperTest {
    @Test
    void test() {
        List<UserModel> users = Arrays.asList(
                new UserModel(0, false, "张0", 29L), 
                new UserModel(1, false, "张1", 19L), 
                new UserModel(2, true, "张2", 18L), 
                new UserModel(3, false, "张3", 17L), 
                new UserModel(4, true, "张4", 19L), 
                new UserModel(5, false, "张5", 18L));
        ListSearchHelper<UserModel> search = ListSearchHelper.of(users)
                .index("id", UserModel::getId).index("sex", UserModel::getSex)
                .index("name", UserModel::getName).index("age", UserModel::getAge);
        Assertions.assertEquals(users.get(1), search.get("id", 1).first().get());
        Assertions.assertEquals(users.get(3), search.get("name", "张3").first().get());
        Assertions.assertEquals(users.get(4), search.get("age", 19L).get("sex", true).first().get());
    
        Assertions.assertTrue(search.get("id", 6).isEmpty());
        Assertions.assertEquals(1, search.get("id", 1).size());
        Assertions.assertEquals(users.get(1), search.get("id", 1).toList().get(0));
    }
    
    @Data
    public class UserModel {
        @NonNull
        private Integer id;
        @NonNull
        private Boolean sex;
        @NonNull
        private String name;
        @NonNull
        private Long age;
    }
}
