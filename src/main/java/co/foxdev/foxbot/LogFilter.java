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

package co.foxdev.foxbot;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.pircbotx.Colors;

public class LogFilter extends Filter<ILoggingEvent>
{
    @Override
    public FilterReply decide(ILoggingEvent event)
    {
        if (event.getMarker().getName().equals("pircbotx.output") && (event.getMessage().contains("NICKSERV IDENTIFY") || event.getMessage().contains("PASS ")))
        {
            return FilterReply.DENY;
        }

        if (event.getMessage().contains("\u0003"))
        {
            FoxBot.getInstance().getLogger().info(Colors.removeFormattingAndColors(event.getMessage()));
            return FilterReply.DENY;
        }

        return FilterReply.NEUTRAL;
    }
}
