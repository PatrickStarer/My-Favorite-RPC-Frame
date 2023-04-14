package org;


import balancer.RandomBalancer;
import mfrf.CarService;
import mfrf.UserService;
import netty.client.Client;
import netty.client.proxy.ClientProxy;
import serializer.Serializer;

public class Consumer {
    public static void main(String[] args) {
        Client client = new Client(new RandomBalancer(),Serializer.JSON);
        ClientProxy proxy = new ClientProxy(client);
        CarService service = proxy.getProxy(CarService.class);
        String add = service.add(1, 2);
        System.out.println(add);


    }
}
