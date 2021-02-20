package space.cubicworld.multichat;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Webhook;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcModule;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.discord.DiscordModule;

public class MultiChatModule implements DiscomcModule {

    private boolean enabled;

    @Getter
    private WebhookClient webhookClient;

    @Getter
    @Setter
    private MultiChatConfiguration configuration;

    @Override
    public void load() {
        FileConfiguration config = DiscomcPlugin.getDiscomcPlugin().getConfig();
        setEnabled(config.getBoolean("multiChat.enabled"));
        if (!isEnabled()) return;
        setConfiguration(new MultiChatConfiguration(config));
    }

    @Override
    public void enable() {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscordModule discordModule = discomcPlugin.getDiscordModule();
        DiscomcSave discomcSave = discomcPlugin.getDiscomcSave();
        if (discomcSave.getMultiChatChannelID() == 0) {
            discomcSave.setMultiChatChannelID(
                    discordModule.createTextChannel("discomc-multiChat").getIdLong());
        }
        if ("".equals(discomcSave.getMultiChatWebhookURL())){
            discomcPlugin.getLogger().severe("MultiChatWebhookURL is not set! Check save.json! MultiChatModule is disabled!");
            setEnabled(false);
        }
        else {
            webhookClient = WebhookClient.withUrl(discomcSave.getMultiChatWebhookURL());
        }
        discomcPlugin.getDiscordModule().getJda().addEventListener(new MultiChatDiscordHandler());
        discomcPlugin.getServer().getPluginManager().registerEvents(new MultiChatMinecraftHandler(), DiscomcPlugin.getDiscomcPlugin());
    }

    @Override
    public void disable() {
        if (webhookClient != null) webhookClient.close(); // closing webhook connection
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getDescription() {
        return "chat between discord and minecraft";
    }
}
