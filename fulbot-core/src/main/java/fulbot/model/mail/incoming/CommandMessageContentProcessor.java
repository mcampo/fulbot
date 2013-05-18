package fulbot.model.mail.incoming;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * {@link MessageContentProcessor} that decides what to do based on the presence
 * of configured commands in the message content.
 * 
 * This processor will only check the first line of the message content for an
 * exact match with any command.
 */
public class CommandMessageContentProcessor implements MessageContentProcessor {

	private Collection<String> addCommands;
	private Collection<String> removeCommands;

	public CommandMessageContentProcessor(Collection<String> addCommands, Collection<String> removeCommands) {
		Validate.notNull(addCommands, "The list of 'add' commands is required");
		Validate.notNull(removeCommands, "The list of 'remove' commands is required");
		this.addCommands = addCommands;
		this.removeCommands = removeCommands;
	}

	@Override
	public void process(String content, String sender, Set<String> attendance) {
		String command = content.split("\n")[0].trim();

		if (matchesAny(command, addCommands)) {
			attendance.add(sender);
		}

		if (matchesAny(command, removeCommands)) {
			attendance.remove(sender);
		}
	}

	/**
	 * Checks if the given command matches any of the commands in the given
	 * collection
	 * 
	 * @param command
	 * @param commandsToMatch
	 * @return
	 */
	private boolean matchesAny(String command, Collection<String> commandsToMatch) {
		for (String commandToMatch : commandsToMatch) {
			if (commandToMatch.equalsIgnoreCase(command)) {
				return true;
			}
		}
		return false;
	}

}
