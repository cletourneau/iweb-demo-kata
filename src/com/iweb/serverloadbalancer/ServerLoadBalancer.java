package com.iweb.serverloadbalancer;

import java.util.ArrayList;
import java.util.List;

public class ServerLoadBalancer {
    public void balance(final Server[] servers, final Vm[] vms) {
        for (Vm vm : vms) {
            addVmOnLessLoadedServer(servers, vm);
        }
    }

    private void addVmOnLessLoadedServer(final Server[] servers, final Vm vm) {
        List<Server> serverWithEnoughRoom = extractAllServersWithEnoughRoomForVm(servers, vm);

        if (serverWithEnoughRoom.size() > 0) {
            Server lessLoadedServer = getLessLoadedServer(serverWithEnoughRoom);
            lessLoadedServer.addVm(vm);
        }
    }

    private List<Server> extractAllServersWithEnoughRoomForVm(final Server[] servers, final Vm vm) {
        final List<Server> serversWithEnoughRoom = new ArrayList<Server>(servers.length);
        for (Server server : servers) {
            if (server.canFit(vm)) {
                serversWithEnoughRoom.add(server);
            }
        }
        return serversWithEnoughRoom;
    }

    private Server getLessLoadedServer(final List<Server> servers) {
        Server lessLoaded = null;
        for (Server server : servers) {
            if (lessLoaded == null || server.currentLoadPercentage < lessLoaded.currentLoadPercentage) {
                lessLoaded = server;
            }
        }
        return lessLoaded;
    }
}
