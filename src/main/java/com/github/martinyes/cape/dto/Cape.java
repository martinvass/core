package com.github.martinyes.cape.dto;

import com.github.martinyes.Main;
import com.github.martinyes.account.Account;
import com.github.martinyes.cape.CapeService;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import me.lucko.helper.npc.CitizensNpc;
import net.citizensnpcs.api.npc.NPC;

import java.text.ParseException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Getter
@ToString
public class Cape {

    private final int id;
    private final String slug, name, description, modelUUID, link;
    private final Category category;
    private final double price;
    private final boolean saladPay;
    private final String saladLink;
    private final Date addedAt;

    private NPC model;

    public Cape(JsonObject json) throws ParseException {
        this.id = json.get("id").getAsInt();
        this.slug = json.get("slug").getAsString();
        this.name = json.get("name").getAsString();
        this.description = json.get("description").getAsString();
        this.modelUUID = (json.get("modelUuid").isJsonNull() ? "" : json.get("modelUuid").getAsString());

        String categorySlug = (json.get("category").isJsonNull() ? null : json.get("category").getAsString());
        this.category = (Category.find(categorySlug).isPresent() ? Category.find(categorySlug).get() : Category.OTHER);

        this.price = json.get("price").getAsDouble() / 100.0D;
        this.saladPay = json.get("saladpay").getAsBoolean();
        this.saladLink = (json.get("saladLink").isJsonNull() ? "" : json.get("saladLink").getAsString());
        this.addedAt = Date.from(Instant.parse(json.get("createdAt").getAsString()));

        this.link = "https://127.0.0.1/shop/" + this.slug;

        ((CapeService) Main.getService(CapeService.class)).create(this);
    }

    public void setModel(CitizensNpc model) {
        this.model = model.getNpc();
    }

    public void hideModel(Account account) {
        if (this.model.getEntity() == null) {
            System.out.println("Cape model entity null at Cape.hideModel!");
            return;
        }

        //Main.getInstance().getEntityUtils().hideEntity(account.toPlayer(), this.model.getEntity());
    }

    public void showModel(Account account) {
        if (this.model.getEntity() == null) {
            System.out.println("Cape model entity null at Cape.hideModel!");
            return;
        }

        //Main.getInstance().getEntityUtils().showEntity(account.toPlayer(), this.model.getEntity());
    }

    @AllArgsConstructor
    @Getter
    public enum Category {
        BASIC("basic"),
        UNIQUE("unique"),
        YOUTUBER("youtuber"),
        REWARD("reward"),
        T_THE_A("through-the-ages"),
        THREE_D_CAPES("3d-capes"),
        OTHER("");

        private final String slug;

        public static Optional<Category> find(String slug) {
            if (slug == null)
                return Optional.of(Category.OTHER);

            return Arrays.stream(values()).filter(c -> c.getSlug().equalsIgnoreCase(slug)).findFirst();
        }
    }
}