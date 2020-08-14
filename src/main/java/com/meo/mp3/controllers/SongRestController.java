package com.meo.mp3.controllers;

import com.meo.mp3.models.interactive.Review;
import com.meo.mp3.models.songs.Song;
import com.meo.mp3.services.IUserService;
import com.meo.mp3.models.users.account.User;
import com.meo.mp3.services.IUserService;
import com.meo.mp3.services.IReviewService;
import com.meo.mp3.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Timestamp;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/song")
public class SongRestController {
    private Long user_id;

    @Autowired
    private IUserService userService;

    @Autowired
    private SongService songService;

    @Autowired
    private IReviewService reviewService;

    @PostMapping("/user_id")
    @ResponseBody
    public void getUser_id(@RequestBody Long id){
        user_id = id;
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Song> getList(){
        return songService.findAll();
    }

    @RequestMapping(value = "/{id}/detail",method = RequestMethod.GET , produces = {MediaType.APPLICATION_JSON_VALUE})
    public Song getById(@PathVariable("id") Long id) {
        Song song = songService.findById(id);
//        Review review = song.getReview();
//        review.setViews(review.getViews()+ 1);
//        song.setReview(review);
        return songService.save(song);
    }
    @RequestMapping(value = "/save",method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Song save(@RequestBody Song song){
        song.setPostTime(new Timestamp(System.currentTimeMillis()));
        song.setReview(reviewService.createNew());
        User user = userService.findById(user_id);
        song.setUser(user);
        return songService.save(song);
    }

    @RequestMapping(value = "/{id}/delete",method = RequestMethod.DELETE,produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Song delete(@PathVariable("id") Long id){
        return songService.delete(id);
    }

    @GetMapping("/{songName}/search")
    public ResponseEntity<List<Song>> findSongByName(@PathVariable String songName){
        List<Song> songList = songService.getSongsByNameContains(songName);
        return new ResponseEntity<List<Song>>(songList, HttpStatus.OK);
    }

    @GetMapping("/topten")
    public ResponseEntity<List<Song>> getTenSongsByPostTime(){
        List<Song> songList = songService.getTop10SongByPostTime();
        return new ResponseEntity<List<Song>>(songList, HttpStatus.OK);
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getAllSongByUser(@PathVariable Long id){
        List<Song> songList = songService.getSongsByUserId(id);
        return new ResponseEntity<List<Song>>(songList, HttpStatus.OK);
    }

    @GetMapping("/topsix")
    public ResponseEntity<List<Song>> getSixSongsByPostTime(){
        List<Song> songList = songService.getTop6SongByPostTime();
        return new ResponseEntity<List<Song>>(songList, HttpStatus.OK);
    }
}
