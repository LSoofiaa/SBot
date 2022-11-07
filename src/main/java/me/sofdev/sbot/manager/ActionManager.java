package me.sofdev.sbot.manager;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.CertainMessageEvent;

import javax.swing.text.html.Option;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ActionManager {

    private ArrayList<User> userArrayList = new ArrayList<>();
    public static ConfigManager config = new ConfigManager();

    public void embed(String title, String description, User user, TextChannel channel) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("_       _ " + title);
        embed.setDescription(description);
        embed.setTimestampToNow();
        embed.setColor(Color.RED);
        embed.setThumbnail(user.getAvatar());

        channel.sendMessage(embed);
    }

    public void promoteAction(CertainMessageEvent event, DiscordApi api) {
        config.api = api;
        if (!event.getMessage().getAuthor().canManageRolesOnServer()) {
            event.getMessage().reply(config.NoPuedes);
            return;
        }
        ActionLogger.logPromote(event);


        for (User user : event.getMessage().getMentionedUsers()) {
            if (user.getMentionTag().equals(event.getMessage().getUserAuthor().get().getMentionTag())) {
                event.getMessage().reply(config.NoTePuedesP);
                return;
            }
            for (Role role : user.getRoles(api.getServerById(config.serverID).get())) {
                if (role.getIdAsString().equals(config.helperRoleID)) {
                        embed("Promote",
                                "Usuario: " + user.getDiscriminatedName() + "\n" +
                                "Rol -> Nuevo Rol: Helper -> Mod", user, api.getChannelById(config.staffUpdatesChannel)
                                        .get().asTextChannel()
                                        .get());
                        user.removeRole(api.getRoleById(config.helperRoleID).get());
                        user.addRole(api.getRoleById(config.modRoleID).get());
                } else if (role.getIdAsString().equals(config.modRoleID)) {
                    embed("Promote",
                            "Usuario: " + user.getDiscriminatedName() + "\n" +
                                    "Rol -> Nuevo Rol: Mod -> SrMod", user, api.getChannelById(config.staffUpdatesChannel)
                                    .get().asTextChannel()
                                    .get());
                    user.removeRole(api.getRoleById(config.modRoleID).get());
                    user.addRole(api.getRoleById(config.srmodRoleID).get());
                } else if (role.getIdAsString().equals(config.srmodRoleID)) {
                    embed("Promote",
                            "Usuario: " + user.getDiscriminatedName() + "\n" +
                                    "Rol -> Nuevo Rol: SrMod -> JrAdmin", user, api.getChannelById(config.staffUpdatesChannel)
                                    .get().asTextChannel()
                                    .get());
                    user.removeRole(api.getRoleById(config.srmodRoleID).get());
                    user.addRole(api.getRoleById(config.jradminroleID).get());
                } else if (role.getIdAsString().equals(config.jradminroleID)) {
                    embed("Promote",
                            "Usuario: " + user.getDiscriminatedName() + "\n" +
                                    "Rol -> Nuevo Rol: JrAdmin -> Admin", user, api.getChannelById(config.staffUpdatesChannel)
                                    .get().asTextChannel()
                                    .get());
                    user.removeRole(api.getRoleById(config.jradminroleID).get());
                    user.addRole(api.getRoleById(config.adminroleID).get());
                } else if (role.getIdAsString().equals(config.adminroleID)) {
                    embed("Promote",
                            "Usuario: " + user.getDiscriminatedName() + "\n" +
                                    "Rol -> Nuevo Rol: Admin -> Co-Owner", user, api.getChannelById(config.staffUpdatesChannel)
                                    .get().asTextChannel()
                                    .get());
                    user.removeRole(api.getRoleById(config.adminroleID).get());
                    user.addRole(api.getRoleById(config.coownerroleID).get());
                } else if (role.getIdAsString().equals(config.coownerroleID)) {
                    embed("Promote",
                            "Usuario: " + user.getDiscriminatedName() + "\n" +
                                    "Rol -> Nuevo Rol: Co-Owner -> Discord Manager", user, api.getChannelById(config.staffUpdatesChannel)
                                    .get().asTextChannel()
                                    .get());
                    user.removeRole(api.getRoleById(config.coownerroleID).get());
                    user.addRole(api.getRoleById(config.discordManagerRoleID).get());
                } else if (role.getIdAsString().equals(config.discordManagerRoleID)) {
                    embed("Promote",
                            "Usuario: " + user.getDiscriminatedName() + "\n" +
                                    "Rol -> Nuevo Rol: Discord Manager -> Owner", user, api.getChannelById(config.staffUpdatesChannel)
                                    .get().asTextChannel()
                                    .get());
                    user.removeRole(api.getRoleById(config.discordManagerRoleID).get());
                    user.addRole(api.getRoleById(config.ownerRoleID).get());
                }
            }
        }
        event.getMessage().reply(config.HasPromoteado(event));
    }

    public void demoteAction(CertainMessageEvent event, DiscordApi api) {
        if (!event.getMessage().getAuthor().canManageRolesOnServer()) {
            event.getMessage().reply(config.NoPuedes);
            return;
        }
        ActionLogger.logDemote(event);

        boolean successDemote = false;

        for (User user : event.getMessage().getMentionedUsers()) {
            if (user.getMentionTag().equals(event.getMessage().getUserAuthor().get().getMentionTag())) {
                event.getMessage().reply(config.NoTePuedesD);
                return;
            }
            if (!userArrayList.contains(user)) {
                userArrayList.add(user);
                user.sendMessage(">>> ¡Hola " + user.getMentionTag() + "!, este mensaje fue enviado automáticamente de Vqlenh Community, solo te queríamos dar la noticia de que fuiste demoteado, si quieres saber la razón puedes abrir ticket (<#1017122074336169984>). :sob:");
            }
            Role highestRole = api.getServerById(config.serverID).get().getHighestRole(user).get();
            Role staffRole = api.getServerById(config.serverID).get().getRoleById(config.staffRoleID).get();
            Server server = api.getServerById(config.serverID).get();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("_       _ Demote       ");
            embed.setDescription(
                    "Usuario: " + user.getDiscriminatedName() + "\n" +
                            "Razón: `no especificada`" + "\n" +
                            "Rol: " + highestRole.getName() + "\n" +
                            "Baneado: :x:"
            );
            embed.setTimestampToNow();
            embed.setColor(Color.RED);
            embed.setThumbnail(user.getAvatar());

            if (highestRole.getIdAsString().equals("1017098419984142486") && user.getRoles(server).contains(staffRole)
                    || highestRole.getIdAsString().equals("1017098417996054528") && user.getRoles(server).contains(staffRole)
                    || highestRole.getIdAsString().equals("1025946072134205451") && user.getRoles(server).contains(staffRole)
                    || highestRole.getIdAsString().equals("1025946627346796664") && user.getRoles(server).contains(staffRole)
                    || highestRole.getIdAsString().equals("1017098414984548435") && user.getRoles(server).contains(staffRole)
                    || highestRole.getIdAsString().equals("1017098411310333952") && user.getRoles(server).contains(staffRole)
                    || highestRole.getIdAsString().equals("1025946766283124799") && user.getRoles(server).contains(staffRole)
                    || highestRole.getIdAsString().equals("1025946766283124799") && user.getRoles(server).contains(staffRole)
                    || highestRole.getIdAsString().equals("1017098407820660736") && user.getRoles(server).contains(staffRole)) {
                api.getChannelById(config.staffUpdatesChannel)
                        .get().asTextChannel()
                        .get().sendMessage(embed);
                user.removeRole(highestRole);
                user.removeRole(api.getRoleById("1017562824630866030").get(), "Demote");
                successDemote = true;
            } else {
                if (!user.getRoles(server).contains(staffRole)) {
                    event.getChannel().sendMessage(config.NoEsStaff);
                    successDemote = false;
                }
            }
        }
        if (successDemote) {
            event.getMessage().reply(config.HasDemoteado(event));
        }
    }
}
