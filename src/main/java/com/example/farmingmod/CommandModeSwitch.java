package com.example.farmingmod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.BlockPos;

import java.util.List;

public class CommandModeSwitch extends CommandBase {
    @Override
    public List<String> addTabCompletionOptions(
            ICommandSender sender,
            String[] args,
            BlockPos pos
    ) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(
                    args,
                    "netherwarts", "wheat", "test","wildrose"
            );
        }
        return null;
    }


    @Override
    public String getCommandName() {
        return "modeswitch"; // 命令名：/modeswitch
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/modeswitch <mode>" ;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 1){
            FullTestMod.instance.modeSwitch(args[0]);
        }
        if (args.length != 1 ){
            sender.addChatMessage(new ChatComponentText("§c用法: /modeswitch <mode>"));
            return;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 所有人都能用客户端命令
    }
}

