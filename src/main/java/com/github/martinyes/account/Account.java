package com.github.martinyes.account;

import com.github.martinyes.Main;
import com.github.martinyes.cape.dto.Cape;
import com.github.martinyes.cape.preview.PreviewService;
import com.github.martinyes.wrapper.WrappedCitizensNpc;
import lombok.Getter;
import lombok.ToString;
import me.lucko.helper.npc.CitizensNpc;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@ToString
public class Account {

    private final UUID uuid;
    private CitizensNpc previewNpc;

    public Account(UUID uuid) {
        this.uuid = uuid;
    }

    public void createPreviewNPC(Cape cape, Location location) {
        if (previewNpc != null) {
            destroyPreviewNPC(false);
        }

        PreviewService previewService = ((PreviewService) Main.getService(PreviewService.class));
        WrappedCitizensNpc wrappedNpc = previewService.create(new WrappedCitizensNpc(cape, location, true));

        NPC npc = wrappedNpc.getCitizensNpc().getNpc();
        Main.getInstance().getEntityUtils().showEntity(toPlayer(), npc.getEntity());

        this.previewNpc = wrappedNpc.getCitizensNpc();
    }

    public void destroyPreviewNPC(boolean message) {
        if (previewNpc == null)
            return;

        this.previewNpc.getNpc().destroy();

        if (message)
            toPlayer().sendMessage(ChatColor.RED + "You are no longer viewing " +
                    ChatColor.YELLOW + this.previewNpc.getNpc().getName() +
                    ChatColor.RED + " cape.");

        this.previewNpc = null;
    }

    public boolean matches(UUID uuid) {
        return this.uuid.equals(uuid);
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}