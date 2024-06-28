// 包本体
package com.blbilink.blbilogin;
import com.blbilink.blbilogin.load.*;
import com.blbilink.blbilogin.modules.*;
// 导入 Bukkit 配置文件包
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
// 导入数组列表包
import java.util.ArrayList;
import java.util.List;

public final class BlbiLogin extends JavaPlugin implements Listener {
    public final List<String> noLoginPlayerList = new ArrayList<>();
    private Sqlite sqlite;
    public static BlbiLogin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
        // Plugin startup logic
        getLogger().info("波比登录系统开始注入。");
        // 注册Bukkit事件监听器
        Bukkit.getPluginManager().registerEvents(this, this);
        // 初始化插件
        Load.loadConfig(this);
        Load.loadLanguage(this);
        // 初始化sqlite数据库
        sqlite = new Sqlite();

        getCommand("login").setExecutor(new Commands());
        getCommand("register").setExecutor(new Commands());
        getCommand("blbilogin").setExecutor(new Commands());
    }
    public Sqlite getSqlite() {
        return sqlite;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!noLoginPlayerList.contains(e.getPlayer().getName())) {
            String msgPlayerLogin = Load.getMessage("logPlayerJoin", "检测到玩家 %player% 进入服务器, 已封锁玩家移动.",e.getPlayer().getName());
            this.getLogger().info(msgPlayerLogin);
            noLoginPlayerList.add(e.getPlayer().getName());

        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (noLoginPlayerList.contains(e.getPlayer().getName()) && Configvar.noLoginPlayerCantMove) {
            String msgNoLoginTryMove = Load.getMessage("logNoLoginTryMove", "未登录玩家 %player% 尝试移动，已进行回弹.",e.getPlayer().getName());
            this.getLogger().info(msgNoLoginTryMove);
            e.setCancelled(true);
        }
    }

//    @EventHandler
//    public void onPlayerInteract(PlayerInteractEvent e) {
//        if (noLoginPlayerList.contains(e.getPlayer().getName()) && e.getAction() == Action.LEFT_CLICK_AIR) {
//            this.getLogger().info("玩家 " + e.getPlayer().getName() + "成功登录, 已解除封锁.");
//            noLoginPlayerList.remove(e.getPlayer().getName());
//        }
//    }

//    @EventHandler
//    public void onPlayerCommandSend(PlayerCommandPreprocessEvent e) {
//        e.getPlayer().sendMessage("您已处于登录状态");
//        if (noLoginPlayerList.contains(e.getPlayer().getName()) && e.getMessage().startsWith("/l")) {
//
//        } else {
//            e.getPlayer().sendMessage("您已处于登录状态");
//        }
//    }
}