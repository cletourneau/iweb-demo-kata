package com.iweb.serverloadbalancer;

import static com.iweb.serverloadbalancer.CurrentLoadPercentageMatcher.hasCurrentLoadPercentageOf;
import static com.iweb.serverloadbalancer.ServerBuilder.server;
import static com.iweb.serverloadbalancer.VmBuilder.vm;
import static com.iweb.serverloadbalancer.VmCountOfServerMatcher.hasAVmCountOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class ServerLoadBalancerTest {
    @Test public void
    itCompiles() {
        assertThat(true, equalTo(true));
    }
    
    private void balance(Server[] servers, Vm[] vms) {
        new ServerLoadBalancer().balance(servers, vms);
    }
    
    private <T> T a(Builder<T> builder) {
        return builder.build();
    }

    private Server[] theListOfServerWith(final Server... servers) {
        return servers;
    }

    private Vm[] theListOfVm(final Vm... vms) {
        return vms;
    }

    private Vm[] anEmptyListOfVm() {
        return new Vm[0];
    }

    @Test public void
    balancingOneServer_noVm_serverStaysEmpty() {
        Server theServer = a(server().withCapacity(1));

        balance(theListOfServerWith(theServer), anEmptyListOfVm());

        assertThat(theServer, hasCurrentLoadPercentageOf(0.0d));
    }

    @Test public void
    balanceAServerWithOneSlotCapacity_andAOneSlotVm_fillsTheServerWithTheVm() {
        Server theServer = a(server().withCapacity(1));
        Vm theVm = a(vm().ofSize(1));

        balance(theListOfServerWith(theServer), theListOfVm(theVm));

        assertThat(theServer, hasCurrentLoadPercentageOf(100.0d));
        assertThat("The server should contain the VM", theServer.contains(theVm));
    }
    
    @Test public void
    balanceAServerWithTenSlotsCapacity_andAOneSlotVm_fillsTheServerAtTenPercent() {
        Server theServer = a(server().withCapacity(10));
        Vm theVm = a(vm().ofSize(1));

        balance(theListOfServerWith(theServer), theListOfVm(theVm));

        assertThat(theServer, hasCurrentLoadPercentageOf(10.0d));
        assertThat("The server should contain the VM", theServer.contains(theVm));
    }
    
    @Test public void
    balanceAServerWithEnoughRoom_getsFilledWithAllVms() {
        Server theServer = a(server().withCapacity(100));
        Vm theFirstVm = a(vm().ofSize(1));
        Vm theSecondVm = a(vm().ofSize(1));

        balance(theListOfServerWith(theServer), theListOfVm(theFirstVm, theSecondVm));

        assertThat("The server should contain the VM", theServer.contains(theFirstVm));
        assertThat("The server should contain the VM", theServer.contains(theSecondVm));
        assertThat(theServer, hasAVmCountOf(2));
    }
    
    @Test public void
    aVm_shouldBeBalanced_onLessLoadedServerFirst() {
        Server mostLoadedServer = a(server().withCapacity(100).withCurrentLoadOf(50.0d));
        Server lessLoadedServer = a(server().withCapacity(100).withCurrentLoadOf(45.0d));

        Vm theVm = a(vm().ofSize(10));

        balance(theListOfServerWith(mostLoadedServer, lessLoadedServer), theListOfVm(theVm));

        assertThat("The less loaded server should contain the VM", lessLoadedServer.contains(theVm));
    }
    
    @Test public void
    balanceAServerWithNotEnoughRoom_shouldNotBeFilledWithAVm() {
        Server theServer = a(server().withCapacity(10).withCurrentLoadOf(90.0d));
        Vm theVm = a(vm().ofSize(2));

        balance(theListOfServerWith(theServer), theListOfVm(theVm));

        assertThat("The server should not contain the VM", !theServer.contains(theVm));
    }
    
    @Test public void
    balanceIWeb_serversAndVms() {
        Server server1 = a(server().withCapacity(4));
        Server server2 = a(server().withCapacity(6));
        
        Vm vm1 = a(vm().ofSize(1));
        Vm vm2 = a(vm().ofSize(4));
        Vm vm3 = a(vm().ofSize(2));
        
        balance(theListOfServerWith(server1, server2), theListOfVm(vm1, vm2, vm3));
        
        assertThat("The server 1 should contain the vm 1", server1.contains(vm1));
        assertThat("The server 2 should contain the vm 2", server2.contains(vm2));
        assertThat("The server 1 should contain the vm 3", server1.contains(vm3));
        
        assertThat(server1, hasCurrentLoadPercentageOf(75.0d));
        assertThat(server2, hasCurrentLoadPercentageOf(66.66d));
    }
}
