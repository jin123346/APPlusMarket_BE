package com.aplus.aplusmarket.dataset;

import com.aplus.aplusmarket.entity.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.ArrayList;
import java.util.List;

public class DataSetUp implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        final List<User> users = new ArrayList<>();

    }
}
