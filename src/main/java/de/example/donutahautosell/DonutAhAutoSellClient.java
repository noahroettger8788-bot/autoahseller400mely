package de.example.donutahautosell;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class DonutAhAutoSellClient implements ClientModInitializer {
    private static KeyBinding sellKey;

    @Override
    public void onInitializeClient() {
        sellKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.donut_ah_autosell.sell",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (sellKey.wasPressed()) {
                sellHeldItem(client);
            }
        });
    }

    private static void sellHeldItem(MinecraftClient client) {
        if (client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ItemStack held = client.player.getMainHandStack();
        if (held.isEmpty()) {
            client.player.sendMessage(Text.literal("[AH AutoSell] Du hältst keinen Gegenstand in der Haupthand."), true);
            return;
        }

        // Sends the server command as a normal chat command.
        client.getNetworkHandler().sendChatCommand("ah sell 400m");
        client.player.sendMessage(Text.literal("[AH AutoSell] Verkaufsbefehl gesendet: /ah sell 400m"), true);
    }
}
