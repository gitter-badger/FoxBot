/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package co.foxdev.foxbot.commands;

import co.foxdev.foxbot.utils.Utils;
import org.jsoup.Jsoup;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import co.foxdev.foxbot.FoxBot;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHaspaid extends Command
{
    private final FoxBot foxbot;

    public CommandHaspaid(FoxBot foxbot)
    {
        super("haspaid", "command.ping", "mcpaid");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();
        Channel channel = event.getChannel();

        if (args.length == 1)
        {
            try
            {
                channel.sendMessage(Jsoup.connect("https://minecraft.net/haspaid.jsp?user=" + args[0]).timeout(500).get().text().contains("true") ? Utils.colourise(String.format("(%s) The account \"%s\" is a &2premium&r Minecraft account!", Utils.munge(sender.getNick()), args[0])) : Utils.colourise(String.format("(%s) The account \"%s\" is &cNOT&r a premium Minecraft account!", Utils.munge(sender.getNick()), args[0])));
            }
            catch (Exception ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                foxbot.sendMessage(channel, String.format("(%s) &cSomething went wrong...", Utils.munge(sender.getNick())));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %shaspaid <user>", foxbot.getConfig().getCommandPrefix()));
    }
}