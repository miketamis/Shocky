import java.util.Random;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import pl.shockah.HTTPQuery;
import pl.shockah.StringTools;
import pl.shockah.shocky.Data;
import pl.shockah.shocky.Module;
import pl.shockah.shocky.Shocky;
import pl.shockah.shocky.cmds.Command;
import pl.shockah.shocky.cmds.Command.EType;

public class ModulePHP extends Module {
	protected Command cmd;
	
	public String name() {return "php";}
	public void onEnable() {
		Data.config.setNotExists("php-url","http://localhost/shocky/shocky.php");
		Command.addCommands(cmd = new CmdPHP());
	}
	public void onDisable() {
		Command.removeCommands(cmd);
	}
	
	public String parse(PircBotX bot, EType type, Channel channel, User sender, String code) {
		if (code == null) return "";
		
		StringBuilder sb = new StringBuilder("$channel = \""+channel.getName()+"\"; $bot = \""+bot.getNick().replace("\"","\\\"")+"\"; $sender = \""+sender.getNick().replace("\"","\\\"")+"\";");
		
		User[] users = channel.getUsers().toArray(new User[channel.getUsers().size()]);
		sb.append(" $randnick = \""+users[new Random().nextInt(users.length)].getNick().replace("\"","\\\"")+"\";");
		
		code = sb.toString()+code;
		
		HTTPQuery q = new HTTPQuery(Data.config.getString("php-phpurl")+"?"+HTTPQuery.parseArgs("code",code));
		q.connect(true,false);
		
		sb = new StringBuilder();
		for (String line : q.read()) {
			if (sb.length() != 0) sb.append(" | ");
			sb.append(line);
		}
		
		return sb.toString();
	}
	
	public class CmdPHP extends Command {
		public String command() {return "php";}
		public String help(PircBotX bot, EType type, Channel channel, User sender) {return "\nphp {code} - runs PHP code";}
		public boolean matches(PircBotX bot, EType type, String cmd) {
			return cmd.equals(command());
		}
		
		public void doCommand(PircBotX bot, EType type, Channel channel, User sender, String message) {
			String[] args = message.split(" ");
			if (args.length < 2) {
				Shocky.send(bot,type,EType.Notice,EType.Notice,EType.Notice,EType.Console,channel,sender,help(bot,type,channel,sender));
				return;
			}
			
			Shocky.send(bot,type,channel,sender,parse(bot,type,channel,sender,StringTools.implode(args,1," ")));
		}
	}
}