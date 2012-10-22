package com.iweb.serverloadbalancer;

public class ServerBuilder implements Builder<Server> {
    private int capacity;
    private double expectedLoad;
    private boolean expectedLoadSet;

    @Override
    public Server build() {
        final Server server = new Server(capacity);
        if (expectedLoadSet) {
            fillServerWithAVmOfTheRightSize(server, expectedLoad);
        }
        return server;
    }

    private void fillServerWithAVmOfTheRightSize(final Server server, final double expectedLoad) {
        int serverCapacity = server.capacity;
        int wantedVmSize = (int) (expectedLoad / Server.MAXIMUM_LOAD * (double) serverCapacity);
        server.addVm(new Vm(wantedVmSize));
    }

    public static ServerBuilder server() {
        return new ServerBuilder();
    }

    public ServerBuilder withCapacity(final int capacity) {
        this.capacity = capacity;
        return this;
    }

    public ServerBuilder withCurrentLoadOf(final double expectedLoad) {
        this.expectedLoad = expectedLoad;
        expectedLoadSet = true;
        return this;
    }
}
