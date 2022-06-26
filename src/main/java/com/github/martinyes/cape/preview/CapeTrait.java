package com.github.martinyes.cape.preview;

import com.github.martinyes.Main;
import com.github.martinyes.account.Account;
import com.github.martinyes.account.AccountService;
import com.github.martinyes.cape.CapeService;
import com.github.martinyes.cape.command.CapeCommands;
import com.github.martinyes.cape.dto.Cape;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

@TraitName("cape_trait")
public class CapeTrait extends Trait {

    private final PreviewService previewService;
    private final CapeService capeService;
    private final AccountService accountService;
    private final String key = "cape_data";
    @Persist
    private String capeData;

    public CapeTrait() {
        super("cape_data");

        this.previewService = ((PreviewService) Main.getService(PreviewService.class));
        this.capeService = ((CapeService) Main.getService(CapeService.class));
        this.accountService = ((AccountService) Main.getService(AccountService.class));
    }

    // Here you should load up any values you have previously saved (optional).
    // This does NOT get called when applying the trait for the first time, only
    // loading onto an existing npc at server start.
    // This is called AFTER onAttach so you can load defaults in onAttach and
    // they will be overridden here.
    // This is called BEFORE onSpawn, npc.getBukkitEntity() will return null.
    public void load(DataKey key) {
        capeData = key.getString(this.key, "");
    }

    // Save settings for this NPC (optional). These values will be persisted to
    // the Citizens saves file
    public void save(DataKey key) {
        key.setString(this.key, capeData);
    }

    // Run code when your trait is attached to a NPC.
    // This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
    // This would be a good place to load configurable defaults for new NPCs.
    @Override
    public void onAttach() {
        if (!this.getNPC().data().has(this.key))
            return;

        this.capeData = this.getNPC().data().get(this.key).toString();
    }

    // Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
    // This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {
        this.getNPC().data().set(this.key, this.capeData);
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        String value = event.getNPC().data().get(this.key);

        if (value == null) return;

        if (event.getNPC() == this.getNPC()) {
            Player player = event.getClicker();
            Account account = this.accountService.find(a -> a.matches(player.getUniqueId())).get();
            Cape cape = this.capeService.find(c -> c.getId() == Integer.parseInt(value)).get();

            if (player.isSneaking()) {
                CapeCommands.info(player, cape);
                return;
            }

            this.previewService.showNext(account, cape);
        }
    }

    @EventHandler
    public void onLeftClick(NPCDamageByEntityEvent event) {
        System.out.println("\"left click\"");
        if (!(event.getDamager() instanceof Player))
            return;

        String value = event.getNPC().data().get(this.key);

        if (value == null) return;

        if (event.getNPC() == this.getNPC()) {
            Player player = (Player) event.getDamager();
            Account account = this.accountService.find(a -> a.matches(player.getUniqueId())).get();
            Cape cape = this.capeService.find(c -> c.getId() == Integer.parseInt(value)).get();

            if (player.isSneaking()) {
                CapeCommands.info(player, cape);
                return;
            }

            this.previewService.showPrevious(account, cape);
        }
    }
}
