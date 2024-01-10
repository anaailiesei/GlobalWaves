package commands.admin;

import entities.user.NormalUser;
import libraries.users.NormalUsersLibrary;

public final class AddPremiumUser {
    private static State state;

    private AddPremiumUser() {
    }

    /**
     * Execute the command for adding a premium user
     *
     * @param username The username of the user that should be added
     */
    public static void execute(final String username) {
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        checkConditions(user);
        if (!state.equals(State.addedPremium)) {
            return;
        }
        assert user != null;
        user.setPremium(true);
    }

    /**
     * Check the conditions for performing this operation
     * User should already exist, and they shouldn't have bought a subscription
     * yet for a successful operation.
     *
     * @param user The premium user that should be added
     */
    private static void checkConditions(final NormalUser user) {
        if (user == null) {
            state = State.noUser;
        } else if (user.isPremium()) {
            state = State.alreadyPremium;
        } else {
            state = State.addedPremium;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username The username of the added premium user
     * @return A string with the message
     */
    public static String toString(final String username) {
        if (state.equals(State.noUser)) {
            return "The username " + username + " doesn't exist.";
        } else if (state.equals(State.alreadyPremium)) {
            return username + " is already a premium user.";
        }
        return username + " bought the subscription successfully.";
    }

    private enum State {
        noUser, alreadyPremium, addedPremium
    }
}
