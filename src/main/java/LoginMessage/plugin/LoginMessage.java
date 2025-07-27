package LoginMessage.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LoginMessage extends JavaPlugin {

    private List<Component> joinMessages;
    private Component[] bookPages;
    private String bookTitle;
    private String bookAuthor;
    private boolean giveBook;

    private static final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadFromConfig();
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getCommand("rulesreload").setExecutor(new ReloadCommand(this));
    }

    public void loadFromConfig() {
        FileConfiguration config = getConfig();

        this.joinMessages = config.getStringList("join-messages").stream()
                .map(mm::deserialize).toList();

        this.bookPages = config.getStringList("book-pages").stream()
                .map(mm::deserialize).toArray(Component[]::new);

        this.bookTitle = config.getString("book-title", "The Rules");
        this.bookAuthor = config.getString("book-author", "The WSO Admins");
        this.giveBook = config.getBoolean("give-book", true);
    }

    public List<Component> getJoinMessages() {
        return joinMessages;
    }

    public Component[] getBookPages() {
        return bookPages;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public boolean shouldGiveBook() {
        return giveBook;
    }

    static class JoinListener implements Listener {
        private final LoginMessage plugin;

        public JoinListener(LoginMessage plugin) {
            this.plugin = plugin;
        }

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            for (Component message : plugin.getJoinMessages()) {
                player.sendMessage(message);
            }

            if (!plugin.shouldGiveBook()) return;

            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta) book.getItemMeta();
            if (meta == null) return;

            meta.title(Component.text(plugin.getBookTitle()));
            meta.author(Component.text(plugin.getBookAuthor()));
            meta.pages(plugin.getBookPages());

            book.setItemMeta(meta);
            player.getInventory().addItem(book);
        }
    }

    static class ReloadCommand implements CommandExecutor {
        private final LoginMessage plugin;

        public ReloadCommand(LoginMessage plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            plugin.reloadConfig();
            plugin.loadFromConfig();
            sender.sendMessage(Component.text("Reloaded join messages and book config."));
            return true;
        }
    }
}
