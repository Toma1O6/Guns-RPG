package dev.toma.gunsrpg.common.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.util.text.TranslationTextComponent;

public class JsonArgumentType implements ArgumentType<JsonElement> {

    private static final JsonParser PARSER = new JsonParser();
    private static final DynamicCommandExceptionType INVALID_JSON = new DynamicCommandExceptionType(msg -> new TranslationTextComponent("command.gunsrpg.exception.arg_json_invalid", msg));

    private JsonArgumentType() {}

    public static JsonArgumentType json() {
        return new JsonArgumentType();
    }

    public static JsonElement getJson(final CommandContext<?> context, final String name) {
        return context.getArgument(name, JsonElement.class);
    }

    @Override
    public JsonElement parse(StringReader reader) throws CommandSyntaxException {
        int startPos = reader.getCursor();
        String value = readWholeString(reader);
        JsonElement element;
        try {
            element = PARSER.parse(value);
        } catch (JsonSyntaxException syntaxException) {
            reader.setCursor(startPos);
            throw INVALID_JSON.create(syntaxException.getMessage());
        }
        return element;
    }

    private String readWholeString(StringReader reader) {
        StringBuilder builder = new StringBuilder();
        while (canRead(reader)) {
            char next = reader.peek();
            if (next == ' ') {
                break;
            }
            char value = reader.read();
            builder.append(value);
        }
        return builder.toString();
    }

    private boolean canRead(StringReader reader) {
        return reader.getRemainingLength() > 0;
    }
}
