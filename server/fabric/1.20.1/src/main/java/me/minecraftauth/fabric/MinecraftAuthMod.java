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

import github.scarsz.configuralize.DynamicConfig;
import github.scarsz.configuralize.ParseException;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import me.minecraftauth.plugin.common.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

public class MinecraftAuthMod implements DedicatedServerModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("minecraftauth");
	@Getter private static MinecraftAuthMod instance;
	@Getter private AuthenticationService service;

	@Override
	public void onInitializeServer() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		MinecraftAuthMod.instance = this;


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
			service = new AuthenticationService.Builder()
					.withConfig(config)
					.withLogger(new FabricLogger(config, LOGGER))
					.build();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

//		try {
//			service = new AuthenticationService().Builder()
//					.withConfig(config)
//					.withLogger(new FabricLogger(config, LOGGER))
//					.build();
//
//		} catch (IOException | ParseException e) {
//			e.printStackTrace();
//		}

		Command.register();

	}
}
