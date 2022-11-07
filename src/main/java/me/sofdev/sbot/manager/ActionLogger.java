package me.sofdev.sbot.manager;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.CertainMessageEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ActionLogger {

    public static DiscordApi api = null;

    public static String fullDateHour() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        return dateFormat.format(now);
    }

    public static void logDemote(CertainMessageEvent event) {
        try{
            User performer = event.getMessage().getUserAuthor().get();
            StringBuffer buffer = new StringBuffer();
            for (User mentionedUser : event.getMessage().getMentionedUsers()) {
                buffer.append(mentionedUser.getDiscriminatedName());
                buffer.append(", ");
            }
            System.out.println(
                    "SBot Logger | DEMOTE LOG : " + fullDateHour() +
                            "\nPerformed by: " + performer.getId() + " : " + performer.getDiscriminatedName() +
                            "\nUsers Affected: " + buffer);
            api.getChannelById("1017099641931698308").get().asTextChannel().get().sendMessage(
                    "SBot Logger | DEMOTE LOG : " + fullDateHour() +
                            "\nPerformed by: " + performer.getId() + " : " + performer.getDiscriminatedName() +
                            "\nUsers Affected: " + buffer);
        } catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void logPromote(CertainMessageEvent event) {
        try{
            User performer = event.getMessage().getUserAuthor().get();
            StringBuffer buffer = new StringBuffer();
            for (User mentionedUser : event.getMessage().getMentionedUsers()) {
                buffer.append(mentionedUser.getDiscriminatedName());
                buffer.append(", ");
            }
            System.out.println(
                    "SBot Logger | PROMOTE LOG : " + fullDateHour() +
                            "\nPerformed by: " + performer.getId() + " : " + performer.getDiscriminatedName() +
                            "\nUsers Affected: " + buffer);
            api.getChannelById("1017099641931698308").get().asTextChannel().get().sendMessage(
                    "SBot Logger | PROMOTE LOG : " + fullDateHour() +
                            "\nPerformed by: " + performer.getId() + " : " + performer.getDiscriminatedName() +
                            "\nUsers Affected: `" + buffer + "`");
        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void log(List<String> message, String channelID) {
        api.getChannelById(channelID).get().asTextChannel().get().sendMessage(
                message.toString().replace("[]", "")
                        .replace("%INLINE%", "\n"));
    }

    public static void logEmbed(EmbedBuilder embed, String channelID) {
        api.getChannelById(channelID).get().asTextChannel().get().sendMessage(embed);
    }
}
