package com.iweb.serverloadbalancer;

import java.util.ArrayList;
import java.util.List;

public class Server {
    public double currentLoadPercentage;
    public int capacity;
    private List<Vm> vms = new ArrayList<Vm>(10);
    public static double MAXIMUM_LOAD = 100.0d;

    public Server(final int capacity) {
        this.capacity = capacity;
    }

    public boolean contains(final Vm vm) {
        return vms.contains(vm);
    }

    public void addVm(final Vm vm) {
        vms.add(vm);
        currentLoadPercentage += loadOfVm(vm);
    }

    private double loadOfVm(final Vm vm) {
        return (double) vm.size / (double) capacity * 100.0d;
    }

    public int vmCount() {
        return vms.size();
    }

    public boolean canFit(final Vm vm) {
        return currentLoadPercentage + loadOfVm(vm) <= MAXIMUM_LOAD;
    }
}
