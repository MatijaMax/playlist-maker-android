package com.example.myapplication.sqlite.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.sqlite.model.Artist;
import com.example.myapplication.sqlite.model.Genre;
import com.example.myapplication.sqlite.model.Helper;
import com.example.myapplication.sqlite.model.Playlist;
import com.example.myapplication.sqlite.model.Song;
import com.example.myapplication.sqlite.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 15;
    // Database Name
    private static final String DATABASE_NAME = "PlaylistManager";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_GENRES = "genres";

    private static final String TABLE_ARTISTS = "artists";

    private static final String TABLE_SONGS = "songs";

    private static final String TABLE_PLAYLISTS = "playlists";

    private static final String TABLE_HELPERS = "helpers";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";



    // Table Create Statements
    // Actors table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS "
            + "users" + "(" + "id" + " INTEGER PRIMARY KEY," + "name"
            + " TEXT UNIQUE," + "password" + " TEXT" + ")";

    private static final String CREATE_TABLE_GENRES = "CREATE TABLE IF NOT EXISTS "
            + "genres" + "(" + "id" + " INTEGER PRIMARY KEY," + "name"
            + " TEXT UNIQUE" + ")";

    private static final String CREATE_TABLE_ARTISTS = "CREATE TABLE IF NOT EXISTS "
            + "artists" + "(" + "id" + " INTEGER PRIMARY KEY," + "name"
            + " TEXT UNIQUE," + "genre"
            + " TEXT" + ")";

    private static final String CREATE_TABLE_SONGS = "CREATE TABLE IF NOT EXISTS "
            + "songs" + "(" + "id" + " INTEGER PRIMARY KEY," + "name"
            + " TEXT UNIQUE," + "artist"
            + " TEXT," + "genre"
            + " TEXT"+ ")";

    private static final String CREATE_TABLE_PLAYLISTS = "CREATE TABLE IF NOT EXISTS "
            + "playlists" + "(" + "id" + " INTEGER PRIMARY KEY," + "name"
            + " TEXT UNIQUE," + "username"
            + " TEXT" + ")";

    private static final String CREATE_TABLE_HELPERS = "CREATE TABLE IF NOT EXISTS "
            + "helpers" + "(" + "id" + " INTEGER PRIMARY KEY," + "playlistid"
            + " INTEGER," + "songid"
            + " INTEGER," + "displayname"
            + " TEXT "  + ")";



    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_GENRES);
        db.execSQL(CREATE_TABLE_ARTISTS);
        db.execSQL(CREATE_TABLE_SONGS);
        db.execSQL(CREATE_TABLE_PLAYLISTS);
        db.execSQL(CREATE_TABLE_HELPERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELPERS);


        // create new tables
        onCreate(db);
    }


    //USER FUNCTIONS
    public long createUser(User user) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());//kljuc mora da se slaze sa nazivom kolone
        values.put(KEY_PASSWORD, user.getPassword());

        // insert row
        long user_id = db.insert(TABLE_USERS, null, values);


        //now we know id obtained after writing user to a database, update existing user
        user.setId(user_id);

        return user_id;
    }

    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where name = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    //GENRE FUNCTIONS

    public long createGenre(Genre genre) {

        if (genre.getName().equalsIgnoreCase("none")) {
            return -1; // Indicate failure to create genre
        }
        if (genre.getName().isEmpty()){
            genre.setName("none");
        }
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, genre.getName());//kljuc mora da se slaze sa nazivom kolone
        // insert row
        long genre_id = db.insert("genres", null, values);

        //now we know id obtained after writing user to a database, update existing user
        genre.setId(genre_id);

        return genre_id;
    }

    public int updateGenre(Genre genre) {
        if (genre.getName().equalsIgnoreCase("none")) {
            return -1; // Indicate failure to update genre
        }
        if (genre.getName().isEmpty()){
            genre.setName("none");
        }

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, genre.getName());
        // Updating row
        return db.update("genres", values, KEY_ID + " = ?",
                new String[]{String.valueOf(genre.getId())});
    }

    public void deleteGenre(long genre_id) {
        db.delete("genres", KEY_ID + " = ?",
                new String[]{String.valueOf(genre_id)});
    }

    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + "genres";

        Cursor c = db.rawQuery(selectQuery, null);

        int idIndex = c.getColumnIndex(KEY_ID);
        int nameIndex = c.getColumnIndex(KEY_NAME);


        if (idIndex == -1 || nameIndex == -1) {
            return genres;
        }

        if (c.moveToFirst()) {
            do {
                Genre genre = new Genre();
                genre.setId(c.getLong(idIndex));
                genre.setName(c.getString(nameIndex));


                genres.add(genre);
            } while (c.moveToNext());
        }

        c.close();
        return genres;
    }


    public Genre getGenreById(long genre_id) {
        String selectQuery = "SELECT  * FROM " + "genres" + " WHERE " + "id" + " = " + genre_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            int idIndex = c.getColumnIndex(KEY_ID);
            int nameIndex = c.getColumnIndex(KEY_NAME);

            if (idIndex == -1 || nameIndex == -1) {
                c.close();
                return null;
            }

            Genre genre = new Genre();
            genre.setId(c.getLong(idIndex));
            genre.setName(c.getString(nameIndex));
            return genre;
        }

        return null;
    }
    //ARTISTS

    public long createArtist(Artist artist) {

        if (artist.getName().equalsIgnoreCase("none")) {
            return -1; // Indicate failure to create artist
        }

        if (artist.getName().isEmpty()){
            artist.setName("none");
        }

        ContentValues values = new ContentValues();
        values.put("name", artist.getName());
        values.put("genre", artist.getGenre());

        // Insert row
        long artistId = db.insert("artists", null, values);

        // Now we know the id obtained after writing artist to the database, update existing artist
        artist.setId(artistId);

        return artistId;
    }

    public int updateArtist(Artist artist) {
        if (artist.getName().equalsIgnoreCase("none")) {
            return -1; // Indicate failure to update artist
        }
        if (artist.getName().isEmpty()){
            artist.setName("none");
        }

        ContentValues values = new ContentValues();
        values.put("name", artist.getName());
        values.put("genre", artist.getGenre());

        // Updating row
        return db.update("artists", values, "id = ?",
                new String[]{String.valueOf(artist.getId())});
    }

    public void deleteArtist(long artistId) {
        db.delete("artists", "id = ?",
                new String[]{String.valueOf(artistId)});
    }

    public List<Artist> getAllArtists() {
        List<Artist> artists = new ArrayList<>();
        String selectQuery = "SELECT * FROM artists";

        Cursor c = db.rawQuery(selectQuery, null);

        int idIndex = c.getColumnIndex("id");
        int nameIndex = c.getColumnIndex("name");
        int genreIndex = c.getColumnIndex("genre");

        if (idIndex == -1 || nameIndex == -1 || genreIndex == -1) {
            return artists;
        }

        if (c.moveToFirst()) {
            do {
                Artist artist = new Artist();
                artist.setId(c.getLong(idIndex));
                artist.setName(c.getString(nameIndex));
                artist.setGenre(c.getString(genreIndex));

                artists.add(artist);
            } while (c.moveToNext());
        }

        c.close();
        return artists;
    }

    public Artist getArtistById(long artistId) {
        String selectQuery = "SELECT * FROM artists WHERE id = " + artistId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int nameIndex = c.getColumnIndex("name");
            int genreIndex = c.getColumnIndex("genre");

            if (idIndex == -1 || nameIndex == -1 || genreIndex == -1) {
                c.close();
                return null;
            }

            Artist artist = new Artist();
            artist.setId(c.getLong(idIndex));
            artist.setName(c.getString(nameIndex));
            artist.setGenre(c.getString(genreIndex));
            c.close();
            return artist;
        }

        return null;
    }

    public int updateGenreNameForAll(String genre, String newGenre) {
        ContentValues values = new ContentValues();
        values.put("genre", newGenre);
        db.update("songs", values, "genre = ?", new String[]{genre});
        return db.update("artists", values, "genre = ?", new String[]{genre});
    }

    public int updateGenreToNoneForAll(String genre) {
        ContentValues values = new ContentValues();
        values.put("genre", "none");
        db.update("songs", values, "genre = ?", new String[]{genre});
        return db.update("artists", values, "genre = ?", new String[]{genre});
    }

    public int updateArtistNameForAll(String artist, String newArtist) {
        ContentValues values = new ContentValues();
        values.put("artist", newArtist);
        return db.update("songs", values, "artist = ?", new String[]{artist});
    }

    public int updateArtistToNoneForAll(String artist) {
        ContentValues values = new ContentValues();
        values.put("artist", "none");
        return db.update("songs", values, "artist = ?", new String[]{artist});
    }

    //SONGS
    public long createSong(Song song) {

        ContentValues values = new ContentValues();
        values.put("name", song.getName());
        values.put("artist", song.getArtist());
        values.put("genre", song.getGenre());

        // Insert row
        long songId = db.insert("songs", null, values);

        // Now we know the id obtained after writing song to the database, update existing song
        song.setId(songId);

        return songId;
    }

    public int updateSong(Song song) {
        if (song.getName().equalsIgnoreCase("none")) {
            return -1; // Indicate failure to update song
        }

        ContentValues values = new ContentValues();
        values.put("name", song.getName());
        values.put("artist", song.getArtist());
        values.put("genre", song.getGenre());

        // Updating row
        return db.update("songs", values, "id = ?",
                new String[]{String.valueOf(song.getId())});
    }

    public void deleteSong(long songId) {
        db.delete("songs", "id = ?",
                new String[]{String.valueOf(songId)});
    }



    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String selectQuery = "SELECT * FROM songs";

        Cursor c = db.rawQuery(selectQuery, null);

        int idIndex = c.getColumnIndex("id");
        int nameIndex = c.getColumnIndex("name");
        int artistIndex = c.getColumnIndex("artist");
        int genreIndex = c.getColumnIndex("genre");

        if (idIndex == -1 || nameIndex == -1 || artistIndex == -1 || genreIndex == -1) {
            return songs;
        }

        if (c.moveToFirst()) {
            do {
                Song song = new Song();
                song.setId(c.getLong(idIndex));
                song.setName(c.getString(nameIndex));
                song.setArtist(c.getString(artistIndex));
                song.setGenre(c.getString(genreIndex));

                songs.add(song);
            } while (c.moveToNext());
        }

        c.close();
        return songs;
    }

    public Song getSongById(long songId) {
        String selectQuery = "SELECT * FROM songs WHERE id = " + songId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int nameIndex = c.getColumnIndex("name");
            int artistIndex = c.getColumnIndex("artist");
            int genreIndex = c.getColumnIndex("genre");

            if (idIndex == -1 || nameIndex == -1 || artistIndex == -1 || genreIndex == -1) {
                c.close();
                return null;
            }

            Song song = new Song();
            song.setId(c.getLong(idIndex));
            song.setName(c.getString(nameIndex));
            song.setArtist(c.getString(artistIndex));
            song.setGenre(c.getString(genreIndex));
            c.close();
            return song;
        }

        return null;
    }

    public Song getSongByName(String name) {
        String selectQuery = "SELECT * FROM songs WHERE name = '" + name + "'";
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int nameIndex = c.getColumnIndex("name");
            int artistIndex = c.getColumnIndex("artist");
            int genreIndex = c.getColumnIndex("genre");

            if (idIndex == -1 || nameIndex == -1 || artistIndex == -1 || genreIndex == -1) {
                c.close();
                return null;
            }

            Song song = new Song();
            song.setId(c.getLong(idIndex));
            song.setName(c.getString(nameIndex));
            song.setArtist(c.getString(artistIndex));
            song.setGenre(c.getString(genreIndex));
            c.close();
            return song;
        }

        return null;
    }

    //PLAYLISTS
    public long createPlaylist(Playlist playlist) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, playlist.getName());
        values.put("username", playlist.getUsername());
        long playlistId = db.insert(TABLE_PLAYLISTS, null, values);
        playlist.setId(playlistId);
        return playlistId;
    }

    public Playlist getPlaylistById(long playlistId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLAYLISTS, new String[]{KEY_ID, KEY_NAME, "username"},
                KEY_ID + "=?", new String[]{String.valueOf(playlistId)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Playlist playlist = new Playlist(cursor.getString(1), cursor.getString(2));
            playlist.setId(cursor.getLong(0));
            cursor.close();
            return playlist;
        }
        return null;
    }

    public Playlist getPlaylistByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLAYLISTS, new String[]{KEY_ID, KEY_NAME, "username"},
                KEY_NAME + "=?", new String[]{name},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Playlist playlist = new Playlist(cursor.getString(1), cursor.getString(2));
            playlist.setId(cursor.getLong(0));
            cursor.close();
            return playlist;
        }
        return null;
    }

    public List<Playlist> getAllPlaylists(String user) {
        List<Playlist> playlists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PLAYLISTS + " WHERE " + "username" + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});

        if (cursor.moveToFirst()) {
            do {
                Playlist playlist = new Playlist(cursor.getString(1), cursor.getString(2));
                playlist.setId(cursor.getLong(0));
                playlists.add(playlist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return playlists;
    }

    public int updatePlaylist(Playlist playlist) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, playlist.getName());
        values.put("username", playlist.getUsername());

        return db.update(TABLE_PLAYLISTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(playlist.getId())});
    }

    public void deletePlaylist(long playlistId) {
        db.delete(TABLE_PLAYLISTS, KEY_ID + " = ?",
                new String[]{String.valueOf(playlistId)});
    }

    //HELPER


    public long createHelper(Helper helper) {
        if (helperExists(helper.getPlaylistId(), helper.getSongId())) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put("playlistid", helper.getPlaylistId());
        values.put("songid", helper.getSongId());
        values.put("displayname", helper.getDisplayName());

        long helperId = db.insert(TABLE_HELPERS, null, values);

        helper.setId(helperId);

        return helperId;
    }

    private boolean helperExists(long playlistId, long songId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HELPERS + " WHERE playlistid = ? AND songid = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(playlistId), String.valueOf(songId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int updateHelper(Helper helper) {
        ContentValues values = new ContentValues();
        values.put("playlistid", helper.getPlaylistId());
        values.put("songid", helper.getSongId());
        values.put("displayname", helper.getDisplayName());

        return db.update("helpers", values, "id = ?",
                new String[]{String.valueOf(helper.getId())});
    }

    public void deleteHelper(long helperId) {
        db.delete("helpers", "id = ?",
                new String[]{String.valueOf(helperId)});
    }

    public List<Helper> getAllHelpers() {
        List<Helper> helpers = new ArrayList<>();
        String selectQuery = "SELECT * FROM helpers";

        Cursor c = db.rawQuery(selectQuery, null);

        int idIndex = c.getColumnIndex("id");
        int playlistIdIndex = c.getColumnIndex("playlistid");
        int songIdIndex = c.getColumnIndex("songid");
        int displayNameIndex = c.getColumnIndex("displayname");


        if (idIndex == -1 || playlistIdIndex == -1 || songIdIndex == -1 || displayNameIndex == -1) {
            return helpers;
        }

        if (c.moveToFirst()) {
            do {
                Helper helper = new Helper();
                helper.setId(c.getLong(idIndex));
                helper.setPlaylistId(c.getLong(playlistIdIndex));
                helper.setSongId(c.getLong(songIdIndex));
                helper.setDisplayName((c.getString(displayNameIndex)));

                helpers.add(helper);
            } while (c.moveToNext());
        }

        c.close();
        return helpers;
    }

    public List<Helper> getHelpersByPlaylistId(long playlistId) {
        List<Helper> helpers = new ArrayList<>();
        String selectQuery = "SELECT * FROM helpers WHERE playlistId = ?";

        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(playlistId)});

        int idIndex = c.getColumnIndex("id");
        int playlistIdIndex = c.getColumnIndex("playlistid");
        int songIdIndex = c.getColumnIndex("songid");
        int displayNameIndex = c.getColumnIndex("displayname");

        if (idIndex == -1 || playlistIdIndex == -1 || songIdIndex == -1 || displayNameIndex == -1) {
            c.close();
            return helpers;
        }

        if (c.moveToFirst()) {
            do {
                Helper helper = new Helper();
                helper.setId(c.getLong(idIndex));
                helper.setPlaylistId(c.getLong(playlistIdIndex));
                helper.setSongId(c.getLong(songIdIndex));
                helper.setDisplayName(c.getString(displayNameIndex));

                helpers.add(helper);
            } while (c.moveToNext());
        }

        c.close();
        return helpers;
    }



    public Helper getHelperById(long helperId) {
        String selectQuery = "SELECT * FROM helpers WHERE id = " + helperId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int playlistIdIndex = c.getColumnIndex("playlistid");
            int songIdIndex = c.getColumnIndex("songid");
            int displayNameIndex = c.getColumnIndex("displayname");

            if (idIndex == -1 || playlistIdIndex == -1 || songIdIndex == -1 || displayNameIndex == -1) {
                c.close();
                return null;
            }

            Helper helper = new Helper();
            helper.setId(c.getLong(idIndex));
            helper.setPlaylistId(c.getLong(playlistIdIndex));
            helper.setSongId(c.getLong(songIdIndex));
            helper.setDisplayName((c.getString(displayNameIndex)));
            c.close();
            return helper;
        }

        return null;
    }

    public void deleteHelpersBySongId(long songId) {
        db.delete("helpers", "songid = ?", new String[]{String.valueOf(songId)});
    }

    public void deleteHelpersByPlaylistId(long playlistId) {
        db.delete("helpers", "playlistid = ?", new String[]{String.valueOf(playlistId)});
    }


    public void populateInitialData() {
        if (!isDataPresent()) {
            insertInitialData();
        }
    }
    private boolean isDataPresent() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM users", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    private void insertInitialData() {
        // Define initial data
        User user1 = new User();
        user1.setName("user1");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setName("user2");
        user2.setPassword("password2");

        User user3 = new User();
        user3.setName("user3");
        user3.setPassword("password3");

        Genre genre1 = new Genre();
        genre1.setName("genre1");

        Genre genre2 = new Genre();
        genre2.setName("genre2");

        Genre genre3 = new Genre();
        genre3.setName("genre3");

        Artist artist1 = new Artist();
        artist1.setName("ARTIST1");
        artist1.setGenre("genre1");

        Artist artist2 = new Artist();
        artist2.setName("ARTIST2");
        artist2.setGenre("genre2");

        Song song1 = new Song();
        song1.setName("SONG1");
        song1.setArtist("ARTIST1");
        song1.setGenre("genre1");

        Song song2 = new Song();
        song2.setName("SONG2");
        song2.setArtist("ARTIST2");
        song2.setGenre("genre2");

        Playlist p1 = new Playlist();
        p1.setName("PL1");
        p1.setUsername("user1");

        Playlist p2 = new Playlist();
        p2.setName("PL2");
        p2.setUsername("user1");

        Playlist p3 = new Playlist();
        p3.setName("PL3");
        p3.setUsername("user2");

        Helper h1 = new Helper();
        h1.setDisplayName("test");
        h1.setPlaylistId(1);
        h1.setSongId(1);


        // Insert data into the database
        createUser(user1);
        createUser(user2);
        createUser(user3);

        createGenre(genre1);
        createGenre(genre2);
        createGenre(genre3);

        createArtist(artist1);
        createArtist(artist2);

        createSong(song1);
        createSong(song2);

        createPlaylist(p1);
        createPlaylist(p2);
        createPlaylist(p3);

        createHelper(h1);




    }


}
