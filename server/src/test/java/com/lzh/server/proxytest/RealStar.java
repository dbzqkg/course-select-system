package com.lzh.server.proxytest;

public class RealStar implements CanDance,CanSing{
    @Override
    public String dance(String name) {
        System.out.println(name);
        return "OK";
    }

    @Override
    public void sing(String name) {
        System.out.println(name);
    }
}
