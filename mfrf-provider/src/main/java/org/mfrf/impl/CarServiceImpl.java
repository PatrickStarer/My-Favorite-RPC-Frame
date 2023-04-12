package org.mfrf.impl;

import annotation.Provider;
import mfrf.CarService;
import mfrf.UserService;

@Provider
public class CarServiceImpl implements CarService {
    @Override
    public int add(int a, int b) {
        return a+b;
    }
}
