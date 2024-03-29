package commands.normalUser.player;

import commands.normalUser.searchBar.audio.SelectAudio;
import entities.audio.Audio;
import entities.audio.collections.Collection;
import entities.user.NormalUser;
import lombok.Getter;
import managers.CheckClass;
import managers.normalUser.PlayerManager;
import managers.normalUser.SearchBarManager;
import statistics.listenTrackers.ListenTrackerNormalUser;

/**
 * Implements the load operation
 */
public final class LoadAudio extends Load {
    private final SelectAudio select;
    private final PlayerManager playerManager;
    private final SearchBarManager searchBarManager;
    /**
     * -- GETTER --
     *  Checks if source was successfully loaded
     */
    @Getter
    private boolean successfullyLoaded = false;

    public LoadAudio(final PlayerManager playerManager,
                     final SearchBarManager searchBarManager,
                     final Shuffle shuffle,
                     final SelectAudio selectAudio,
                     final ListenTrackerNormalUser listenTracker) {
        super(playerManager,
                shuffle,
                listenTracker);
        this.searchBarManager = searchBarManager;
        this.select = selectAudio;
        this.playerManager = playerManager;
    }

    /**
     * Sets the entities.user for the load operation
     *
     * @param newUser The entities.user to be set
     * @return the current instance
     * @see NormalUser
     */
    public LoadAudio setUser(final NormalUser newUser) {
        super.setUser(newUser);
        return this;
    }

    /**
     * Executes the load operation if the source was selected
     */
    @Override
    public void execute() {
        Audio selectedObject = select.getSelectedObject();
        if (!searchBarManager.getStatus().equals(SearchBarManager.SearchBarStatus.selecting)) {
            successfullyLoaded = false;
            super.setMessage(toString());
            return;
        }
        successfullyLoaded = super.execute(selectedObject);
        super.setMessage(toString());
    }

    @Override
    public String toString() {
        if (!successfullyLoaded) {
            return "Please select a source before attempting to load.";
        } else if (CheckClass.extendsCollection(playerManager.getLoadedObject().getClass())) {
            Collection<?> loadedCollection = (Collection<?>) playerManager
                    .getLoadedObject();
            if (loadedCollection.isEmpty()) {
                return "You can't load an empty audio collection!";
            }
        }
        return "Playback loaded successfully.";
    }
}
