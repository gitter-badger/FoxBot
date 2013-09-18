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

package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandVoice extends Command
{
    private FoxBot foxbot;

    public CommandVoice(FoxBot foxbot)
    {
        super("voice", "command.voice");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        Channel channel = event.getChannel();
        User sender = event.getUser();

        if (args.length > 0)
        {
            if (foxbot.getPermissionManager().userHasQuietPermission(sender, "command.voice.others"))
            {
                for (String target : args)
                {
                    if (!channel.getVoices().contains(foxbot.getUser(target)))
                    {
                        foxbot.voice(channel, foxbot.getUser(target));
                    }
                }
                return;
            }
            foxbot.sendNotice(sender, "You do not have permission to voice other users!");
            return;
        }

        if (!channel.getVoices().contains(sender))
        {
            foxbot.voice(channel, sender);
            return;
        }
        foxbot.sendNotice(sender, "You are already voice!");
    }
}