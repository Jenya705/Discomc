package space.cubicworld.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.ModuleStore;

import java.util.List;
import java.util.logging.Level;

public class ReloadCommand extends DiscomcAdminCommand{

    public ReloadCommand() {
        super("reload all configs and modules", "discomc.reload");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        DiscomcPlugin.getDiscomcPlugin().loadSettings();
        ModuleStore.getModules().forEach((name, module) -> {
            try {
                DiscomcPlugin.logger().info(String.format("Reloading %s module", name));
                module.load();
            } catch(Exception e){
                DiscomcPlugin.logger().log(Level.WARNING, String.format("Disabling %s module:", name), e);
                module.setEnabled(false);
            }
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }
}
