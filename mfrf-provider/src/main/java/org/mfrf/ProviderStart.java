package org.mfrf;

import annotation.ProviderLoader;
import netty.Server;


@ProviderLoader
public class ProviderStart {

    public static void main(String[] args) {
        Server server  = new Server(9999,"127.0.0.1",1);
        server.start();
    }
}
