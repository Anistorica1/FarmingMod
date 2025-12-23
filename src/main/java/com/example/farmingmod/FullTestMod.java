package com.example.farmingmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import java.util.HashMap;
import java.util.Map;
@Mod(modid = "farmingmod_v1", name = "Farming Mod", version = "1.0")
public class FullTestMod {

    private final Minecraft mc = Minecraft.getMinecraft();

    private boolean running = false;
    private int tickCounter = 0;
    private int phase = 0;
    private boolean lastRKeyState = false;
    private int counter = 0;
    private boolean markFirst = true;
    public static FullTestMod instance;
    private boolean cropfirst = false;
    private double tempx = 0, tempz = 0;
    private static Map<String, Integer> dict = new HashMap<String, Integer>();
    static {
        dict.put("netherwarts", 0);
        dict.put("wheat", 1);
        dict.put("test", 2);
        dict.put("wildrose",3);
    }
    private String mode = "netherwarts";
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandModeSwitch());
    }
    public FullTestMod() {
        instance = this;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // 检测 O 键是否从未按下 -> 按下一瞬间
        boolean currentRKey = Keyboard.isKeyDown(Keyboard.KEY_LBRACKET);
        if (currentRKey && !lastRKeyState) {
            running = !running;
            resetKeys();
            tickCounter = 0;
            phase = 0;
            counter = 9;
            cropfirst = true;
            mc.thePlayer.addChatMessage(new ChatComponentText(
                    "§e[FullMod] 自动动作已 " + (running ? "§a开启" : "§c关闭")
            ));
        }
        lastRKeyState = currentRKey;

        if (!running) return;

        tickCounter++;

        // 防止空指针
        if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null) return;
        BlockPos pos = mc.objectMouseOver.getBlockPos();
        switch (dict.get(mode)) {
            case 0:
                netherWartsFarming();
                break;
            case 1:
                wheatFarming();
                break;
            case 2:
                test();
                break;
            case 3:
                wildroseFarming();
                break;
        }

    }

    private void clickBlock(BlockPos pos) {
        if (mc.objectMouseOver == null) return;
        mc.playerController.onPlayerDamageBlock(pos, mc.objectMouseOver.sideHit);
        mc.thePlayer.swingItem(); // 1.8.9 使用 swingItem()
    }

    private void press(KeyBinding key) {
        KeyBinding.setKeyBindState(key.getKeyCode(), true);
    }

    private void release(KeyBinding key) {
        KeyBinding.setKeyBindState(key.getKeyCode(), false);
    }

    private void resetKeys() {
        release(mc.gameSettings.keyBindLeft);
        release(mc.gameSettings.keyBindRight);
        release(mc.gameSettings.keyBindForward);
        release(mc.gameSettings.keyBindSneak);
        release(mc.gameSettings.keyBindAttack);
        release(mc.gameSettings.keyBindBack);
    }
    public void modeSwitch(String mode) {
        this.mode = mode;
        mc.thePlayer.addChatMessage(new ChatComponentText(
                "mode has been switched to: " + mode
        ));
    }
    public void netherWartsFarming(){
        if (mc.thePlayer.posY == 70) {
            phase = 5;
            if (tickCounter == 200)
            {
                mc.thePlayer.sendChatMessage("/warp garden");
                phase = 0;
                tickCounter = 0;
                counter = 9;
                markFirst = true;
            }

        }
        if (mc.thePlayer.posY == 31) {
            phase = 5;
            if (tickCounter == 200)
            {
                mc.thePlayer.sendChatMessage("/l");
                phase = 0;
                tickCounter = 0;
                counter = 9;
                markFirst = true;
            }

        }
        if(mc.thePlayer.posY == 71){
        }
        else {
            phase = 5;
            if (tickCounter == 200)
            {
                mc.thePlayer.sendChatMessage("/skyblock");
                phase = 0;
                tickCounter = 0;
                counter = 9;
                markFirst = true;
            }

        }
        switch (phase) {

            case 0:
                if (tickCounter == 1 && counter == 9)
                    mc.thePlayer.sendChatMessage("/warp garden");

                if (tickCounter >= 30) {
                    phase++;
                    tickCounter = 0;
                    counter--;
                    if (counter == 0) counter = 9;
                }
                break;

            case 1: // 向左走 10 秒
                if (tickCounter == 1) {
                    press(mc.gameSettings.keyBindSneak);
                }
                if (tickCounter == 8) {
                    release(mc.gameSettings.keyBindSneak);
                    press(mc.gameSettings.keyBindLeft);
                }
                press(mc.gameSettings.keyBindAttack);

                if (tempz == mc.thePlayer.posZ  && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindLeft);
                    phase++;
                    tickCounter = 0;
                }
                tempz = mc.thePlayer.posZ;
                break;

            case 2: // 向前走 2 秒
                if (tickCounter == 1) press(mc.gameSettings.keyBindForward);
                press(mc.gameSettings.keyBindAttack);

                if (tempx == mc.thePlayer.posX && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindForward);
                    phase++;
                    tickCounter = 0;
                }
                tempx = mc.thePlayer.posX;
                break;

            case 3: // 向右走 10 秒
                if (tickCounter == 1) press(mc.gameSettings.keyBindRight);
                press(mc.gameSettings.keyBindAttack);

                if (tempz == mc.thePlayer.posZ && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindRight);
                    phase++;
                    tickCounter = 0;
                }
                tempz = mc.thePlayer.posZ;
                break;

            case 4: // 向前走 2 秒，然后回到 phase 0
                if (tickCounter == 1) press(mc.gameSettings.keyBindForward);
                press(mc.gameSettings.keyBindAttack);

                if (tempx == mc.thePlayer.posX && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindForward);
                    phase = 0;
                    tickCounter = 0;
                }
                tempx = mc.thePlayer.posX;
                break;

            case 5: //异常模块
                resetKeys();

                if (markFirst) {
                    markFirst = false;
                    tickCounter = 0;
                }
                if (tickCounter >= 400)
                {
                    mc.thePlayer.sendChatMessage("/warp garden");
                    phase = 0;
                    tickCounter = 0;
                    counter = 9;
                    markFirst = true;
                }

                break;
        }
    }
    public void wheatFarming(){
        if (mc.thePlayer.posY == 70) {
            phase = 5;
            if (tickCounter == 200)
            {
                mc.thePlayer.sendChatMessage("/warp garden");
                phase = 0;
                tickCounter = 0;
                counter = 9;
                markFirst = true;
            }

        }
        if (mc.thePlayer.posY == 31) {
            phase = 5;
            if (tickCounter == 200)
            {
                mc.thePlayer.sendChatMessage("/l");
                phase = 0;
                tickCounter = 0;
                counter = 9;
                markFirst = true;
            }

        }
        if(mc.thePlayer.posY == 71){
        }
        else {
            phase = 5;
            if (tickCounter == 200)
            {
                mc.thePlayer.sendChatMessage("/skyblock");
                phase = 0;
                tickCounter = 0;
                counter = 9;
                markFirst = true;
            }

        }
        switch (phase) {

            case 0:
                if (tickCounter == 1 && counter == 9)
                    mc.thePlayer.sendChatMessage("/warp garden");

                if (tickCounter >= 30) {
                    phase++;
                    tickCounter = 0;
                    counter--;
                    if (counter == 0) counter = 9;
                }
                break;

            case 1: // 向左走 10 秒
                if (tickCounter == 1) {
                    press(mc.gameSettings.keyBindSneak);
                }
                if (tickCounter == 8) {
                    release(mc.gameSettings.keyBindSneak);
                    press(mc.gameSettings.keyBindLeft);
                }
                press(mc.gameSettings.keyBindAttack);

                if (tempz == mc.thePlayer.posZ  && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindLeft);
                    phase++;
                    tickCounter = 0;
                }
                tempz = mc.thePlayer.posZ;
                break;

            case 2: // 向前走 2 秒
                if (tickCounter == 1) press(mc.gameSettings.keyBindForward);
                press(mc.gameSettings.keyBindAttack);

                if (tempx == mc.thePlayer.posX && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindForward);
                    phase++;
                    tickCounter = 0;
                }
                tempx = mc.thePlayer.posX;
                break;

            case 3: // 向右走 10 秒
                if (tickCounter == 1) press(mc.gameSettings.keyBindRight);
                press(mc.gameSettings.keyBindAttack);

                if (tempz == mc.thePlayer.posZ && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindRight);
                    phase++;
                    tickCounter = 0;
                }
                tempz = mc.thePlayer.posZ;
                break;

            case 4: // 向前走 2 秒，然后回到 phase 0
                if (tickCounter == 1) press(mc.gameSettings.keyBindForward);
                press(mc.gameSettings.keyBindAttack);

                if (tempx == mc.thePlayer.posX && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindForward);
                    phase = 0;
                    tickCounter = 0;
                }
                tempx = mc.thePlayer.posX;
                break;

            case 5: //异常模块
                resetKeys();

                if (markFirst) {
                    markFirst = false;
                    tickCounter = 0;
                }
                if (tickCounter >= 400)
                {
                    mc.thePlayer.sendChatMessage("/warp garden");
                    phase = 0;
                    tickCounter = 0;
                    counter = 9;
                    markFirst = true;
                }

                break;
        }
    }
    public void test(){
        if (tickCounter ==1){mc.thePlayer.addChatMessage(new ChatComponentText(
                "test1"
        ));}
        if (tickCounter == 100){
            mc.thePlayer.addChatMessage(new ChatComponentText(
                    "test2"
            ));
        }
    }
    public void wildroseFarming(){
        if (mc.thePlayer.posY == 70) {
        phase = 5;
        if (tickCounter == 200)
        {
            mc.thePlayer.sendChatMessage("/warp garden");
            phase = 0;
            tickCounter = 0;
            counter = 15;
            markFirst = true;
        }

    }
        if (mc.thePlayer.posY == 31) {
            phase = 5;
            if (tickCounter == 200)
            {
                mc.thePlayer.sendChatMessage("/l");
                phase = 0;
                tickCounter = 0;
                counter = 15;
                markFirst = true;
            }

        }
        if(mc.thePlayer.posY == 71){
        }
        else {
            phase = 5;
            if (tickCounter == 200)
            {
                mc.thePlayer.sendChatMessage("/skyblock");
                phase = 0;
                tickCounter = 0;
                counter = 15;
                markFirst = true;
            }

        }
        switch (phase) {

            case 0:
                if (cropfirst){counter =15;cropfirst = false;}
                if (tickCounter == 1 && counter == 15)
                    mc.thePlayer.sendChatMessage("/warp garden");

                if (tickCounter >= 30) {
                    phase++;
                    tickCounter = 0;
                    counter--;
                    if (counter == 0) counter = 15;
                }
                break;

            case 1: // 向左走 10 秒
                if (tickCounter == 1) {
                    press(mc.gameSettings.keyBindSneak);
                }
                if (tickCounter == 8) {
                    release(mc.gameSettings.keyBindSneak);
                    press(mc.gameSettings.keyBindLeft);
                }
                press(mc.gameSettings.keyBindAttack);

                if (tempz == mc.thePlayer.posZ  && tickCounter % 2 == 0 && tickCounter > 10) {
                    release(mc.gameSettings.keyBindLeft);
                    phase++;
                    tickCounter = 0;
                }
                tempz = mc.thePlayer.posZ;
                break;

            case 2: // 向前走 2 秒
                if (tickCounter == 1) press(mc.gameSettings.keyBindForward);
                press(mc.gameSettings.keyBindAttack);

                if (tempx == mc.thePlayer.posX && tickCounter % 2 == 0 && tickCounter > 20) {
                    release(mc.gameSettings.keyBindForward);
                    phase++;
                    tickCounter = 0;
                }
                tempx = mc.thePlayer.posX;
                break;

            case 3: // 向右走 10 秒
                if (tickCounter == 1) press(mc.gameSettings.keyBindBack);
                press(mc.gameSettings.keyBindAttack);

                if (tempz == mc.thePlayer.posZ && tickCounter % 2 == 0 && tickCounter > 20) {
                    release(mc.gameSettings.keyBindBack);
                    phase++;
                    tickCounter = 0;
                }
                tempz = mc.thePlayer.posZ;
                break;

            case 4: // 向前走 2 秒，然后回到 phase 0
                if (tickCounter == 1) press(mc.gameSettings.keyBindRight);
                press(mc.gameSettings.keyBindAttack);

                if (tempx == mc.thePlayer.posX && tickCounter % 2 == 0 && tickCounter > 20) {
                    release(mc.gameSettings.keyBindRight);
                    phase = 0;
                    tickCounter = 0;
                }
                tempx = mc.thePlayer.posX;
                break;

            case 5: //异常模块
                resetKeys();

                if (markFirst) {
                    markFirst = false;
                    tickCounter = 0;
                }
                if (tickCounter >= 400)
                {
                    mc.thePlayer.sendChatMessage("/warp garden");
                    phase = 0;
                    tickCounter = 0;
                    counter = 15;
                    markFirst = true;
                }

                break;
        }}
}
