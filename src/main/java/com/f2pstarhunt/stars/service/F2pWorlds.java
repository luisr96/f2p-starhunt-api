package com.f2pstarhunt.stars.service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.f2pstarhunt.stars.thirdparty.WorldClient;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Collection of F2P worlds.
 */
@Service
public class F2pWorlds {

    private static final long ONE_HOUR_MILLIS = 1000 * 60 * 60;

    private final Set<Integer> f2pWorlds = new ConcurrentSkipListSet<>();
    private final WorldClient worldClient = new WorldClient();

    @Scheduled(fixedRate = ONE_HOUR_MILLIS)
    public void refreshF2pWorlds() {
        Set<Integer> f2pWorlds = worldClient.fetchF2pWorlds();
        this.f2pWorlds.addAll(f2pWorlds);       //add the f2p worlds
        this.f2pWorlds.retainAll(f2pWorlds);    //remove the no-longer-f2p worlds
    }

    /**
     * Is the given world an f2p world?
     * @param world the world's number
     * @return true if the world is available for free-to-players, false if it does not exist or is members-only
     */
    public boolean isF2pWorld(int world) {
        return f2pWorlds.isEmpty()  // failsafe in case our worldClient fails due to Jagex updating their worlds endpoint.
                || f2pWorlds.contains(Integer.valueOf(world));
    }
}
