package entities.user;

import entities.audio.collections.Album;
import fileio.input.UserInput;
import libraries.audio.AlbumsLibrary;
import lombok.Getter;
import profile.artist.Event;
import profile.artist.Merch;
import statistics.ListenTrackerArtist;

import java.util.*;

public final class Artist extends User {
    private final LinkedHashSet<Album> albums = new LinkedHashSet<>();
    @Getter
    private final LinkedHashSet<Event> events = new LinkedHashSet<>();
    @Getter
    private final LinkedHashSet<Merch> merchandise = new LinkedHashSet<>();
    @Getter
    private final ListenTrackerArtist listenTracker = new ListenTrackerArtist();
    @Getter
    private final Map<String, Float> songsRevenue = new TreeMap<>();
    @Getter
    private float merchRevenue = 0;
    private int pageViewers = 0;
    @Getter
    private int totalLikes;

    public Artist(final String username, final int age, final String city) {
        UserInput userInput = new UserInput(username, age, city);
        setUserInput(userInput);
    }

    /**
     * Adds an album to the artist's collection of albums.
     *
     * @param album The album to be added.
     */
    public synchronized void addAlbum(final Album album) {
        albums.add(album);
    }

    /**
     * Checks if an album with the given name exists in the artist's albums.
     *
     * @param name The name of the album to check.
     * @return {@code true} if the album {@code exists}, false otherwise.
     */
    public boolean albumExists(final String name) {
        return albums.stream().anyMatch(album -> album.getName().equals(name));
    }

    public HashSet<Album> getAlbums() {
        return albums;
    }

    /**
     * Increments the counter for the number of viewers on the artist's page.
     */
    public void incrementPageViewersCount() {
        pageViewers++;
    }

    /**
     * Decrements the counter for the number of viewers on the artist's page.
     */
    public void decrementPageViewersCount() {
        pageViewers--;
    }

    /**
     * Adds an event to the artist's collection of events.
     *
     * @param event The event to be added.
     */
    public void addEvent(final Event event) {
        events.add(event);
    }

    /**
     * Checks if an event with the given name exists in the artist's events.
     *
     * @param eventName The name of the event to check.
     * @return true if the event exists, false otherwise.
     */
    public boolean eventExists(final String eventName) {
        return events.stream().anyMatch(event -> event.getName().equals(eventName));
    }

    /**
     * Adds merchandise to the artist's collection of merchandise.
     *
     * @param merch The merchandise to be added.
     */
    public void addMerch(final Merch merch) {
        this.merchandise.add(merch);
    }

    /**
     * Checks if the merchandise with the given name exists
     *
     * @param merchName The name of the merch to search
     * @return {@code true} if it exists, {@code false} otherwise
     */
    public boolean merchExists(final String merchName) {
        return merchandise.stream().anyMatch(event -> event.getName().equals(merchName));
    }

    /**
     * Removes an album from an artist
     *
     * @param album The album to be deleted
     */
    public void removeAlbum(final Album album) {
        album.removeAllLikes();
        AlbumsLibrary.getInstance().removeAlbum(album);
        albums.remove(album);
    }

    /**
     * Checks if the artist is deletable
     * To be deletable, an artist shouldn't have any of their songs playing or part of a
     * entities.user's playlist, an album that's playing in a entities.user's player, or a viewer on their page
     *
     * @return {@code true} if the artist is deletable, {@code false} otherwise
     */
    @Override
    public boolean isDeletable() {
        if (pageViewers > 0) {
            return false;
        }
        if (albums == null) {
            return true;
        }
        for (Album album : albums) {
            if (!album.isDeletable()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public HashMap<String, Object> wrapped() {
        return listenTracker.topListensForEach();
    }

    /**
     * Gets an album from the artist's collection by its name
     *
     * @param albumName The name of the album to search for
     * @return the album if it's found, {@code null} otherwise
     */
    public Album getAlbumByName(final String albumName) {
        return albums.stream().filter(album -> album.getName().equals(albumName))
                .findFirst().orElse(null);
    }

    /**
     * Retrieves an event by its name from the artist's events.
     *
     * @param eventName The name of the event to retrieve.
     * @return The Event object if found, or null if not found.
     */
    public Event getEventByName(final String eventName) {
        return events.stream().filter(event ->
                event.getName().equals(eventName)).findFirst().orElse(null);
    }

    /**
     * Removes the event with the given name from the artist's collection of events.
     * If the event is not found, no action is taken.
     *
     * @param eventName The name of the event to be removed.
     */
    public void removeEvent(final String eventName) {
        Event event = getEventByName(eventName);
        events.remove(event);
    }

    /**
     * Increments the total likes count for the artist.
     */
    public void addLike() {
        totalLikes++;
    }

    /**
     * Decrements the total likes count for the artist.
     */
    public void removeLike() {
        totalLikes--;
    }

}