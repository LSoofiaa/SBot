package me.sofdev.sbot;

import me.sofdev.sbot.manager.ActionLogger;
import me.sofdev.sbot.manager.ActionManager;
import me.sofdev.sbot.manager.ConfigManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.entity.user.User;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

import static me.sofdev.sbot.manager.ActionLogger.api;

public class SBot {

    public static ActionManager actions = new ActionManager();
    public static ConfigManager config = new ConfigManager();

    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken(config.token).login().join();
        ActionLogger.api = api;
        Server server = api.getServerById(config.serverID).get();
        System.out.println("El bot ha sido activado exitosamente!");

        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().toLowerCase(Locale.ROOT).startsWith(config.prefix + "promote")) {
                actions.promoteAction(event, api);
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).startsWith(config.prefix + "demote")) {
                actions.demoteAction(event, api);
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).startsWith(config.prefix + "staffapply")) {
                User user = event.getMessage().getUserAuthor().get();
                if (isStaff(user)) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("Staff Apply");
                    embed.setDescription(
                        "1 - `¿Que edad tienes?`" + "\n" +
                        "2 - `¿Que comandos de staff conoces?`" + "\n" +
                        "3 - `¿Porque te gustaría ser staff?`" + "\n" +
                        "4 - `¿Que harías si otro staff abusa?`" + "\n" +
                        "5 - `¿En qué ayudarías al servidor?`" + "\n"
                    );
                    embed.setTimestampToNow();
                    embed.setColor(Color.ORANGE);
                    event.getMessage().reply(embed);
                } else {
                    event.getMessage().reply(config.NoPuedes);
                }
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).startsWith(config.prefix + "newstaff")) {
                User user = event.getMessage().getMentionedUsers().get(0);
                if (event.getMessage().getAuthor().canManageRolesOnServer()) {
                    if (isStaff(user)) {
                        event.getMessage().reply(config.YaEsStaff);
                    } else {
                        ServerUpdater serverUpdater = new ServerUpdater(server)
                                .addRoleToUser(user, api.getRoleById(config.staffRoleID).get()).addRoleToUser(user, api.getRoleById(config.helperRoleID).get());
                        event.getMessage().delete();
                        serverUpdater.update();

                        event.getChannel().sendMessage("¡Felicidades " + user.getMentionTag() + "! :tada:, Bienvenido al staff.");
                    }
                } else {
                    event.getMessage().reply(config.NoPuedes);
                }
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).equals(config.prefix + "help")) {
                if (isStaff(event.getMessage().getUserAuthor().get())) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("SBot | Ayuda / Help");
                    embed.setDescription(
                            "Comandos: " + "\n" +
                                    "_ _ → `1`: " + config.prefix + "promote <Usuario/s>" + "\n" +
                                    "_ _ → `2`: " + config.prefix + "demote <Usuario/s>" + "\n" +
                                    "_ _ → `3`: " + config.prefix + "newstaff <Usuario>" + "\n" +
                                    "_ _ → `4`: " + config.prefix + "staffapply" + "\n" +
                                    "_ _ → `5`: " + config.prefix + "dev (Solo Sofia)" + "\n" +
                                    "Para conocer mas un comando puedes escribir `" + config.prefix + "<Comando>`" +
                                    "Ejemplo: `" + config.prefix + "help promote`"
                    );
                    embed.setTimestampToNow();
                    embed.setColor(Color.ORANGE);
                    event.getMessage().reply(embed);
                } else {
                    event.getMessage().reply(config.NoPuedes);
                }
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).startsWith(config.prefix + "dev")) {
                if (isBotOwner(event.getMessage().getUserAuthor().get())) {
                    event.getMessage().reply("No hay nada para dev.");
                } else {
                    event.getMessage().reply(config.SoloOwner);
                }
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).equals(config.prefix + "help newstaff")) {
                if (isStaff(event.getMessage().getUserAuthor().get())) {
                    event.getMessage().reply("> El comando `" + config.prefix + "`newstaff <Usuario>`, funciona para otorgarle los roles de staff automaticamente al ser aceptado en su apply.");
                } else {
                    event.getMessage().reply(config.NoPuedes);
                }
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).equals(config.prefix + "help promote")) {
                if (isStaff(event.getMessage().getUserAuthor().get())) {
                    event.getMessage().reply("> El comando `" + config.prefix + "promote <Usuario/s>`, funciona para promotear a los staff mencionados.");
                } else {
                    event.getMessage().reply(config.NoPuedes);
                }
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).equals(config.prefix + "help demote")) {
                if (isStaff(event.getMessage().getUserAuthor().get())) {
                    event.getMessage().reply("> El comando `" + config.prefix + "demote <Usuario/s>`, funciona para demotear a los staff mencionados.");
                } else {
                    event.getMessage().reply(config.NoPuedes);
                }
            } else if (event.getMessageContent().toLowerCase(Locale.ROOT).equals(config.prefix + "help staffapply")) {
                if (isStaff(event.getMessage().getUserAuthor().get())) {
                    event.getMessage().reply("> El comando `" + config.prefix + "staffapply`, envia el mensaje de las staff applys (Preguntas)");
                } else {
                    event.getMessage().reply(config.NoPuedes);
                }
            }
        });
    }

    public static boolean isBotOwner(User user) {
        return user.isBotOwner();
    }

    public static boolean isStaff(User user) {
        return user.getRoles(api.getServerById(config.serverID).get()).contains(api.getRoleById("1017562824630866030").get());
    }
}
