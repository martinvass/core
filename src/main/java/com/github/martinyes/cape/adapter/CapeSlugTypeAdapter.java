package com.github.martinyes.cape.adapter;

import com.github.martinyes.Main;
import com.github.martinyes.cape.CapeService;
import com.github.martinyes.cape.dto.Cape;
import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class CapeSlugTypeAdapter extends DrinkProvider<Cape> {

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Nullable
    @Override
    public Cape provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws CommandExitMessage {
        CapeService service = ((CapeService) Main.getService(CapeService.class));

        String slug = arg.get().toUpperCase();
        Cape cape = service.find(c -> c.getSlug().equalsIgnoreCase(slug)).get();

        if (cape != null)
            return cape;

        throw new CommandExitMessage("No cape with slug '" + slug + "' found.");
    }

    @Override
    public String argumentDescription() {
        return "slug";
    }

    @Override
    public List<String> getSuggestions(@NotNull String prefix) {
        CapeService service = ((CapeService) Main.getService(CapeService.class));

        return service.flatList()
                .stream().filter(c -> StringUtils.startsWithIgnoreCase(c.getSlug(), prefix))
                .map(c -> c.getSlug().toLowerCase()).collect(Collectors.toList());
    }
}