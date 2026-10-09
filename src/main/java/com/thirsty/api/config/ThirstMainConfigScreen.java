package com.thirsty.api.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;

public class ThirstMainConfigScreen extends Screen {
    public Screen parent;
    public ThirstMainConfigScreen(Component component, Screen parent) {
        super(component);
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button commonConfig = Button.builder(Component.nullToEmpty("Common Config"), (btn) -> {
            Minecraft.getInstance().setScreen(
                    AutoConfig.getConfigScreen(CommonConfig.class, this).get()
            );
        }).bounds(40, 40, 120, 20).build();

        Button clientConfig = Button.builder(Component.nullToEmpty("Client Config"), (btn) -> {
            Minecraft.getInstance().setScreen(
                    AutoConfig.getConfigScreen(ClientConfig.class, this).get()
            );
        }).bounds(40, 80, 120, 20).build();

        Button containerConfig = Button.builder(Component.nullToEmpty("Container Config"), (btn) -> {
            Minecraft.getInstance().setScreen(ContainerConfig.containerConfig(this));
        }).bounds(40, 120, 120, 20).build();

        Button keyWordConfig = Button.builder(Component.nullToEmpty("Key Word Config"), (btn) -> {
            Minecraft.getInstance().setScreen(
                    AutoConfig.getConfigScreen(KeyWordConfig.class, this).get()
            );
        }).bounds(40, 160, 120, 20).build();


        this.addRenderableWidget(commonConfig);
        this.addRenderableWidget(clientConfig);
        this.addRenderableWidget(containerConfig);
    }
}
