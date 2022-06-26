package com.github.martinyes.cape.adapter;

import com.github.martinyes.cape.dto.Cape;
import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class CapeCategoryTypeAdapter extends DrinkProvider<Cape.Category> {

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
    public Cape.Category provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws CommandExitMessage {
        String categorySlug = arg.get().toUpperCase();
        Optional<Cape.Category> category = Cape.Category.find(categorySlug);

        if (category.isPresent())
            return category.get();

        throw new CommandExitMessage("No cape with category '" + categorySlug + "' found.");
    }

    @Override
    public String argumentDescription() {
        return "category";
    }

    @Override
    public List<String> getSuggestions(@NotNull String prefix) {
        return Arrays.stream(Cape.Category.values())
                .filter(c -> StringUtils.startsWithIgnoreCase(c.getSlug(), prefix))
                .map(c -> c.getSlug().toLowerCase(Locale.ROOT)).collect(Collectors.toList());
    }
}
