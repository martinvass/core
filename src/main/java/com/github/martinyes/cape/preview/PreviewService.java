package com.github.martinyes.cape.preview;

import com.github.martinyes.Main;
import com.github.martinyes.Service;
import com.github.martinyes.account.Account;
import com.github.martinyes.cape.CapeService;
import com.github.martinyes.cape.command.CapeCommands;
import com.github.martinyes.cape.dto.Cape;
import com.github.martinyes.cape.position.Position;
import com.github.martinyes.wrapper.WrappedCitizensNpc;
import com.github.martinyes.wrapper.WrappedResult;
import me.lucko.helper.npc.CitizensNpc;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.citizensnpcs.trait.LookClose;

import java.util.function.Predicate;

import static com.github.martinyes.cape.preview.util.PreviewUtils.getNormalName;
import static com.github.martinyes.cape.preview.util.PreviewUtils.removeSpacing;

public class PreviewService implements Service<WrappedCitizensNpc> {

    @Override
    public long load() {
        long elapsed = System.currentTimeMillis();

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CapeTrait.class).withName("cape_trait"));

        CapeService capeService = ((CapeService) Main.getService(CapeService.class));

        for (Cape cape : capeService.flatList()) {
            if (cape.getModel() != null)
                continue;

            if (cape.getCategory() == null || Position.ofCategory(cape.getCategory()) == null)
                continue;

            this.create(new WrappedCitizensNpc(cape, Position.ofCategory(cape.getCategory()), false));
        }

        return System.currentTimeMillis() - elapsed;
    }

    @Override
    public WrappedCitizensNpc create(WrappedCitizensNpc wrapper) {
        // TODO: set skin
        CitizensNpc citizensNpc = Main.getInstance().getNpcFactory().spawnNpc(
                wrapper.getLocation(),
                getNormalName(removeSpacing(wrapper.getCape().getName()) + "Cape"),
                "",
                ""
        );

        if (!wrapper.isSingleModel())
            citizensNpc.getNpc().data().set("cape_data", wrapper.getCape().getId());
        else
            citizensNpc.setClickCallback(p -> CapeCommands.info(p, wrapper.getCape()));
        wrapper.getCape().setModel(citizensNpc);

        // Remove LookClose trait, so it doesn't look at the nearby player
        citizensNpc.getNpc().removeTrait(LookClose.class);
        citizensNpc.getNpc().addTrait(CapeTrait.class);

        wrapper.setCitizensNpc(citizensNpc);

        return wrapper;
    }

    @Override
    public WrappedResult<WrappedCitizensNpc> find(Predicate<WrappedCitizensNpc> predicate) {
        throw new UnsupportedOperationException("This method is not implemented yet.");
    }

    @Override
    public int totalBy(Predicate<WrappedCitizensNpc> predicate) {
        throw new UnsupportedOperationException("This method is not implemented yet.");
    }

    @Override
    public int total() {
        throw new UnsupportedOperationException("This method is not implemented yet.");
    }

    public void showNext(Account account, Cape curr) {
        curr.hideModel(account);
        getNextCape(curr).showModel(account);
    }

    public void showPrevious(Account account, Cape curr) {
        curr.hideModel(account);
        getPreviousCape(curr).showModel(account);
    }

    private Cape getNextCape(Cape curr) {
        CapeService capeService = ((CapeService) Main.getService(CapeService.class));

        int index = capeService.getCapes().get(curr.getCategory()).indexOf(curr);
        try {
            return capeService.getCapes().get(curr.getCategory()).get(index + 1);
        } catch (IndexOutOfBoundsException e) {
            return capeService.getCapes().get(curr.getCategory()).get(0);
        }
    }

    private Cape getPreviousCape(Cape curr) {
        CapeService capeService = ((CapeService) Main.getService(CapeService.class));

        int index = capeService.getCapes().get(curr.getCategory()).indexOf(curr);
        try {
            return capeService.getCapes().get(curr.getCategory()).get(index - 1);
        } catch (IndexOutOfBoundsException e) {
            return capeService.getCapes().get(curr.getCategory()).get(capeService.total() - 1);
        }
    }

    public void setupCapeModels(Account account) {
        CapeService capeService = ((CapeService) Main.getService(CapeService.class));

        capeService.getCapes().forEach((key, value) -> {
            Cape cape = value.stream().findFirst().orElse(null);

            if (cape != null && cape.getModel() != null && cape.getModel().getEntity() != null) {
                Main.getInstance().getEntityUtils().showEntity(account.toPlayer(),
                        cape.getModel().getEntity());
            }
        });
    }
}