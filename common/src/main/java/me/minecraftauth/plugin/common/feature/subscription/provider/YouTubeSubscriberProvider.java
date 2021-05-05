package me.minecraftauth.plugin.common.feature.subscription.provider;

import alexh.weak.Dynamic;
import me.minecraftauth.lib.AuthService;
import me.minecraftauth.lib.account.MinecraftAccount;
import me.minecraftauth.lib.exception.LookupException;
import me.minecraftauth.plugin.common.feature.subscription.RequireSubscriptionFeature;

public class YouTubeSubscriberProvider extends AbstractSubscriptionProvider {

    private final RequireSubscriptionFeature feature;
    private final Dynamic config;

    public YouTubeSubscriberProvider(RequireSubscriptionFeature feature, Dynamic config) {
        this.feature = feature;
        this.config = config;
    }

    @Override
    public boolean isSubscribed(MinecraftAccount account) throws LookupException {
        return AuthService.isSubscribedYouTube(getServerToken(feature, config), account.getUUID());
    }

}
