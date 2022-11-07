package me.sofdev.sbot.manager;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.CertainMessageEvent;

import java.io.File;

public class ConfigManager {

    public DiscordApi api = null;

    /**
     * Principal
     */

    public final String token = "Use your token";

    public final String prefix = "s!";

    public final String staffUpdatesChannel = "1028882360458018947";

    public final String serverID = "1017097280161058866";
    public final String staffRulesChannelID = "1026230639189561507";
    public final String staffAnnouncesChannelID = "1026230031103557672";

    /**
     * Roles
     */

    public final String staffRoleID = "1017562824630866030";
    public final String helperRoleID = "1017098419984142486";
    public final String modRoleID = "1017098417996054528";
    public final String srmodRoleID = "1025946072134205451";
    public final String jradminroleID = "1025946627346796664";
    public final String adminroleID = "1017098414984548435";
    public final String coownerroleID = "1017098411310333952";
    public final String discordManagerRoleID = "1025946766283124799";
    public final String ownerRoleID = "1017098406822432768";

    /**
     *  Mensajes
     */

    public final String NoPuedes = "> :x: No puedes hacer eso!";
    public final String NoTePuedesD = "> :x: No te puedes demotear a ti mismo!";
    public final String NoTePuedesP = "> :x: No te puedes promotear a ti mismo!";
    public final String NoEsStaff = "> :x: El usuario no es staff!";
    public final String YaEsStaff = "> :x: El usuario ya es staff!";
    public final String SoloOwner = "> :x: Solo Sofia puede usar esto!";

    public String HasPromoteado(CertainMessageEvent event) {
        return "> :white_check_mark: has promoteado a `" + event.getMessage().getMentionedUsers().size() + "` usuarios.";
    }

    public String HasDemoteado(CertainMessageEvent event) {
        return "> :white_check_mark: has demoteado a `" + event.getMessage().getMentionedUsers().size() + "` usuarios.";
    }
}
