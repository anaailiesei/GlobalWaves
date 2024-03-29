package commands.normalUser.general;

import entities.user.Artist;
import entities.user.Host;
import entities.user.NormalUser;
import entities.user.User;
import libraries.users.NormalUsersLibrary;
import libraries.users.UsersLibrariesStats;
import managers.normalUser.AppManager;
import commands.normalUser.pageNavigation.PageType;

public final class Subscribe {
    private static State state;
    private static User subscribedToUser;

    private Subscribe() {
    }

    /**
     * Execute the subscribe/unsubscribe command
     * If the user isn't subscribed to the current artist's/host's page, subscribe
     * If already subscribed, unsubscribe
     *
     * @param username The user that wants to subscribe
     */
    public static void execute(final String username) {
        NormalUser user = NormalUsersLibrary.getInstance().getUserByName(username);
        checkConditions(user);
        if (state.equals(State.noPage) || state.equals(State.noUser)) {
            return;
        }

        assert user != null;
        AppManager app = user.getApp();
        if (state.equals(State.subscribed)) {
            if (app.getPage().equals(PageType.hostPage)) {
                ((Host) subscribedToUser).addObserver(user);
            } else {
                ((Artist) subscribedToUser).addObserver(user);
            }
            user.addSubscription(subscribedToUser);
        } else {
            if (app.getPage().equals(PageType.hostPage)) {
                ((Host) subscribedToUser).removeObserver(user);
            } else {
                ((Artist) subscribedToUser).removeObserver(user);
            }
            user.removeSubscription(subscribedToUser);
        }
    }

    /**
     * Checks the conditions for performing this operation
     * For a successful operation, the user should exist, and
     * the page they're on should be either "artist" or "host"
     * This also sets the subscribed user if found (host/artist)
     *
     * @param user The user for which we check the conditions
     */
    private static void checkConditions(final NormalUser user) {
        if (user == null) {
            state = State.noUser;
            return;
        }
        AppManager app = user.getApp();
        PageType page = app.getPage().pageType();
        if (page.equals(PageType.homePage) || page.equals(PageType.likedContentPage)) {
            state = State.noPage;
            return;
        }
        subscribedToUser = UsersLibrariesStats.getUserByName(app.getPageOwner());
        if (user.isSubscribedTo(subscribedToUser)) {
            state = State.unsubscribed;
        } else {
            state = State.subscribed;
        }
    }

    /**
     * Function for the output message of this command
     *
     * @param username the name of the user that performs this command
     * @return A string with the message
     */
    public static String toString(final String username) {
        return switch (state) {
            case noUser -> "The username " + username + " doesn't exist.";
            case noPage -> "To subscribe you need to be on the page of an artist or host.";
            case subscribed -> username
                    + " subscribed to "
                    + subscribedToUser.getName()
                    + " successfully.";
            case unsubscribed -> username
                    + " unsubscribed from "
                    + subscribedToUser.getName()
                    + " successfully.";
        };
    }

    private enum State {
        noPage, noUser, subscribed, unsubscribed
    }
}
