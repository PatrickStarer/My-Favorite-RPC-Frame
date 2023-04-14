package org.mfrf.impl;

import annotation.Provider;
import mfrf.UserService;


@Provider
public class UserServiceImpl implements UserService {
    @Override
    public void say(String msg) {
        System.out.println("hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world " );
    }
}
