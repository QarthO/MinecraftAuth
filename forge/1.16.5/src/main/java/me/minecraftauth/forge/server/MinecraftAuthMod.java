/*
 * Copyright 2021 MinecraftAuth.me
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

package me.minecraftauth.forge.server;

import github.scarsz.configuralize.DynamicConfig;
import github.scarsz.configuralize.ParseException;
import lombok.Getter;
import me.minecraftauth.plugin.common.service.GameService;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

@Mod(MinecraftAuthMod.MOD_ID)
public class MinecraftAuthMod {

    public static final String MOD_ID = "minecraftauth";
    @Getter private static final Logger logger = LogManager.getLogger();
    @Getter private static MinecraftAuthMod instance;

    @Getter private GameService service;

    public MinecraftAuthMod() {
        MinecraftAuthMod.instance = this;

        // inform mod loader that this is a server-only mod and isn't required on clients
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        DynamicConfig config = new DynamicConfig();
        try {
            config.addSource(MinecraftAuthMod.class, "game-config", new File("config", "MinecraftAuth.yml"));
            config.saveAllDefaults();
            config.loadAll();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return;
        }

        try {
            service = new GameService.Builder()
                    .withConfig(config)
                    .withLogger(new me.minecraftauth.plugin.common.abstracted.Logger() {
                        @Override
                        public void info(String message) {
                            logger.info(message);
                        }
                        @Override
                        public void warning(String message) {
                            logger.warn(message);
                        }
                        @Override
                        public void error(String message) {
                            logger.error(message);
                        }
                        @Override
                        public void debug(String message) {
                            logger.debug(message);
                        }
                    })
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
