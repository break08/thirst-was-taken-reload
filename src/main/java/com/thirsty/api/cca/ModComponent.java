package com.thirsty.api.cca;

import com.thirsty.thirst.ThirstData;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;

public class ModComponent implements EntityComponentInitializer {
    /**
     * Called to register component factories for statically declared component types.
     *
     * <p><strong>The passed registry must not be held onto!</strong> Static component factories
     * must not be registered outside of this method.
     *
     * @param registry an {@link EntityComponentFactoryRegistry} for <em>statically declared</em> components
     */
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PLAYER_THIRST, ThirstData::new, RespawnCopyStrategy.NEVER_COPY);
    }
}
