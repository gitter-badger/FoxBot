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

import co.foxdev.foxbot.FoxBot;
import co.foxdev.foxbot.utils.Utils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class CommandBukkitSearch extends Command
{
	private final FoxBot foxbot;

	public CommandBukkitSearch(FoxBot foxbot)
	{
		super("bukkitsearch", "command.bukkitsearch", "plugin", "bukkit");
		this.foxbot = foxbot;
	}

	@Override
	public void execute(MessageEvent event, String[] args)
	{
		User sender = event.getUser();
		Channel channel = event.getChannel();

		if (args.length > 0)
		{
			String plugin = args[0];
			String url = String.format("http://api.bukget.org/3/search/plugin_name/like/%s?size=1", plugin);

			Connection conn = Jsoup.connect(url).timeout(500).followRedirects(true).ignoreContentType(true);
			String json;

			try
			{
				json = conn.get().text().replace("[", "").replace("]", "");
			}
			catch (IOException ex)
			{
				foxbot.log(ex);
				channel.sendMessage(Utils.colourise(String.format("(%s) &cAn error occurred while querying the Bukget API!", Utils.munge(sender.getNick()))));
				return;
			}

			JSONObject jsonObj = new JSONObject(json);

			if (jsonObj.length() == 0)
			{
				channel.sendMessage(Utils.colourise(String.format("(%s) &cNo results found!", Utils.munge(sender.getNick()))));
				return;
			}

			String name = jsonObj.getString("plugin_name");
			String description = jsonObj.getString("description");
			String pluginUrl = String.format("http://dev.bukkit.org/bukkit-plugins/%s/", jsonObj.getString("slug"));

			if (description.isEmpty())
			{
				description = "No description";
			}

			channel.sendMessage(Utils.colourise(String.format("(%s) &2Name:&r %s &2Description:&r %s &2URL:&r %s", Utils.munge(sender.getNick()), name, description, pluginUrl)));
			return;
		}
		foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sbukkitsearch <plugin>", foxbot.getConfig().getCommandPrefix()));
	}
}