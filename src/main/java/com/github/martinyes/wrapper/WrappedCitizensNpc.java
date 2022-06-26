package com.github.martinyes.wrapper;

import com.github.martinyes.cape.dto.Cape;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.npc.CitizensNpc;
import org.bukkit.Location;

@Getter
public class WrappedCitizensNpc {

    private final Cape cape;
    private final Location location;
    private final boolean singleModel;

    @Setter private CitizensNpc citizensNpc;

    public WrappedCitizensNpc(Cape cape, Location location, boolean singleModel) {
        this.cape = cape;
        this.location = location;
        this.singleModel = singleModel;
    }
}