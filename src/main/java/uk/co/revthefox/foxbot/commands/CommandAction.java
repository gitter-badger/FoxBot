package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class CommandAction extends Command
{
    private FoxBot foxbot;

    public CommandAction(FoxBot foxbot)
    {
        super("action", "command.action");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0 || (args[0].startsWith("#") && args.length == 1))
        {
            foxbot.getBot().sendNotice(sender, String.format("Wrong number of args! use %saction [#channel] <action> " +
                    "[-s]",
                    foxbot.getConfig().getCommandPrefix()));
            return;
        }

        StringBuilder message;

        if (args[0].startsWith("#"))
        {
            message = new StringBuilder(args[1]);

            for (int arg = 2; arg < args.length; arg++)
            {
                if (!args[arg].equalsIgnoreCase("-s"))
                {
                    message.append(" ").append(args[arg]);
                }
            }

            if (foxbot.getBot().getChannel(args[0]).isInviteOnly())
            {
                foxbot.getBot().sendNotice(sender, String.format("%s is invite only!", args[0]));
                return;
            }

            foxbot.getBot().joinChannel(args[0]);

            if (!args[args.length - 1].equalsIgnoreCase("-s"))
            {
                foxbot.getBot().sendAction(args[0], message.toString());
                foxbot.getBot().partChannel(foxbot.getBot().getChannel(args[0]));
                foxbot.getBot().sendNotice(sender, String.format("Action sent to %s, and channel has been left",
                        args[0]));
                return;
            }

            foxbot.getBot().sendAction(args[0], message.toString());
            foxbot.getBot().sendNotice(sender, String.format("Action sent to %s", args[0]));
            return;
        }

        message = new StringBuilder(args[0]);

        for (int arg = 1; arg < args.length; arg++)
        {
            if (!args[arg].equals("-s"))
            {
                message.append(" ").append(args[arg]);
            }
        }
        foxbot.getBot().sendAction(channel, message.toString());
    }
}