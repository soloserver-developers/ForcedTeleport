/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2021, NAFU_at All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package page.nafuchoco.forcedteleport;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import page.nafuchoco.soloservercore.SoloServerApi;

import java.util.Date;
import java.util.logging.Level;

public final class ForcedTeleport extends JavaPlugin implements Listener {
    private static final Date START_DATE = new Date(1613142000); // 2021/2/13 0:00
    private static final Date END_DATE = new Date(1613401140); // 2021/2/15 22:00

    private SoloServerApi soloServerApi;


    @Override
    public void onEnable() {
        // Plugin startup logic
        soloServerApi = SoloServerApi.getSoloServerApi();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Date firstPlayed = new Date(event.getPlayer().getFirstPlayed());
        Date lastPlayed = new Date(event.getPlayer().getLastPlayed());
        if (firstPlayed.after(START_DATE) && firstPlayed.before(END_DATE) && lastPlayed.before(END_DATE)) {
            try {
                Location location = soloServerApi.getPlayerSpawn(event.getPlayer());
                event.getPlayer().teleport(location);
                if (event.getPlayer().hasPermission("mofucraft.english"))
                    event.getPlayer().sendMessage(ChatColor.RED + "Random Teleport did not work if you join this server 13th" +
                            " to 15th for the first time, so you have been moved to the right point. Should you leave " +
                            "something at previous place (close to central of the world), please contact us.");
                else
                    event.getPlayer().sendMessage(ChatColor.RED +
                            "13日から15日までに初参加したプレイヤーはランダムテレポートが動作していなかったため、" +
                            "本来の地点へ移動しました。前の地点(中央付近)に荷物が置いてある場合などはお問い合わせください。");
                event.getPlayer().sendMessage("https://mofucraft.net/contact");
            } catch (NullPointerException e) {
                getServer().getLogger().log(Level.WARNING, "該当するプレイヤーのプレイヤーデータがDB上で見つかりませんでした。", e);
            }
        }
    }
}
