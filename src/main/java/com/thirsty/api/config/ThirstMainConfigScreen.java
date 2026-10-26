package com.thirsty.api.config;

import me.shedaniel.autoconfig.AutoConfigClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import org.jspecify.annotations.NonNull;

public class ThirstMainConfigScreen extends Screen {
    public Screen parent;
    public ThirstMainConfigScreen(Component component, Screen parent) {
        super(component);
        this.parent = parent;
    }

    @Override
    protected void init() {
        Button commonConfig = Button.builder(Component.nullToEmpty("Common Config"), (btn) -> {
            Minecraft.getInstance().setScreenAndShow(
                    AutoConfigClient.getConfigScreen(CommonConfig.class, this).get()
            );
        }).bounds(40, 40, 120, 20).build();

        Button clientConfig = Button.builder(Component.nullToEmpty("Client Config"), (btn) -> {
            Minecraft.getInstance().setScreenAndShow(
                    AutoConfigClient.getConfigScreen(ClientConfig.class, this).get()
            );
        }).bounds(40, 80, 120, 20).build();

        Button containerConfig = Button.builder(Component.nullToEmpty("Container Config"), (btn) -> {
            Minecraft.getInstance().setScreenAndShow(AutoConfigClient.getConfigScreen(ContainerConfig.class, this).get());
        }).bounds(40, 120, 120, 20).build();

        Button keyWordConfig = Button.builder(Component.nullToEmpty("Key Word Config"), (btn) -> {
            Minecraft.getInstance().setScreenAndShow(
                    AutoConfigClient.getConfigScreen(KeyWordConfig.class, this).get()
            );
        }).bounds(40, 160, 120, 20).build();

        Button itemSettingsConfig = Button.builder(Component.nullToEmpty("Item Settings Config"), (btn) -> {
            Minecraft.getInstance().setScreenAndShow(AutoConfigClient.getConfigScreen(ItemSettingsConfig.class, this).get());
        }).bounds(40, 200, 120, 20).build();


        this.addRenderableWidget(commonConfig);
        this.addRenderableWidget(clientConfig);
        this.addRenderableWidget(containerConfig);
        this.addRenderableWidget(keyWordConfig);
        this.addRenderableWidget(itemSettingsConfig);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.parent);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        graphics.text(this.font, "Thirst Was Taken Config", 40, 40 - this.font.lineHeight - 10, 0xFFFFFFFF, true);
        this.extractMenuBackground(graphics);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }
}