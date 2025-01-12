/*
 * Copyright 2021-2025 MinecraftAuth.me
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.minecraftauth.fabric;

import com.mojang.brigadier.context.CommandContext;
import github.scarsz.configuralize.ParseException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.literal;

import java.io.IOException;

public class Command {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(
                    literal("minecraftauth")
                            .then(literal("reload"))
                            .requires(cs -> cs.hasPermissionLevel(3))
                            .executes((ctx) -> reload(ctx))
            );
        });
    }



    private static int reload(CommandContext<ServerCommandSource> context) {
        try {
            MinecraftAuthMod.getInstance().getService().fullReload();
            context.getSource().sendMessage(Text.literal("MinecraftAuth config reloaded").formatted(Formatting.GREEN));
            return 1;
        } catch (IOException e) {
            context.getSource().sendError(Text.literal("IO exception while reading config: " + e.getMessage()).formatted(Formatting.RED));
            e.printStackTrace();
        } catch (ParseException e) {
            context.getSource().sendError(Text.literal("Exception while parsing config: " + e.getMessage()).formatted(Formatting.RED));
            e.printStackTrace();
        }
        return -1;
    }

}
