package commands;

public enum CommandType {
    search, select, load, playPause, status, createPlaylist, addRemoveInPlaylist, like,
    showPlaylists, repeat, shuffle, next, prev, switchVisibility,
    showPreferredSongs, getTop5Songs, getTop5Playlists, forward, backward, follow,
    switchConnectionStatus, getOnlineUsers, addUser, addAlbum, showAlbums, printCurrentPage,
    addEvent, addMerch, getAllUsers, deleteUser, addPodcast, addAnnouncement, removeAnnouncement,
    showPodcasts, removeAlbum, changePage, removePodcast, removeEvent, getTop5Albums,
    getTop5Artists, wrapped, buyPremium, cancelPremium, adBreak, subscribe,
    getNotifications, buyMerch, seeMerch, updateRecommendations, previousPage, loadRecommendations,
    nextPage, endProgram
}
