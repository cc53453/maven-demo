package io.github.cc53453.datatype.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ListMinMaxHelperTest {
    @Test
    void test() {
    	List<UserModel> users = Arrays.asList(
                new UserModel(BigDecimal.valueOf(1)), 
                new UserModel(BigDecimal.valueOf(2)), 
                new UserModel(BigDecimal.valueOf(3)), 
                new UserModel(BigDecimal.valueOf(3)), 
                new UserModel(BigDecimal.valueOf(2)), 
                new UserModel(BigDecimal.valueOf(1))
                );
    	ListMinMaxHelper<UserModel> helper = new ListMinMaxHelper<>(users, 0, 5);
    	Assertions.assertEquals(2, helper.getMaxIndex(UserModel::getId));
    	Assertions.assertEquals(0, helper.getMinIndex(UserModel::getId));
    	helper = new ListMinMaxHelper<>(users, 1, 2);
    	Assertions.assertEquals(2, helper.getMaxIndex(UserModel::getId));
    	Assertions.assertEquals(1, helper.getMinIndex(UserModel::getId));
    }

    @Data
    public class UserModel {
        @NonNull
    	BigDecimal id;
    }
}
